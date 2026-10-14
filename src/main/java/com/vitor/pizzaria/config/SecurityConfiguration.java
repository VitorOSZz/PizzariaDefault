package com.vitor.pizzaria.config;

import com.vitor.pizzaria.auth.LoginAttemptService;
import com.vitor.pizzaria.model.entity.UserAccount;
import com.vitor.pizzaria.repository.UserAccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableJdbcHttpSession
class SecurityConfiguration {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DefaultCookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setUseHttpOnlyCookie(true);
        serializer.setSameSite("Lax");
        return serializer;
    }

    @Bean
    UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> {
            String normalizedEmail = normalizeUsername(username);
            UserAccount user = userAccountRepository.findByEmail(normalizedEmail)
                    .orElseThrow(() -> new UsernameNotFoundException(normalizedEmail));

            if (!Boolean.TRUE.equals(user.getActive())) {
                throw new DisabledException(normalizedEmail);
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + roleName(user.getRole())))
            );
        };
    }

    @Bean
    SecurityFilterChain securityWebFilterChain(
            HttpSecurity http,
            LoginAttemptService loginAttemptService,
            @Value("${app.security.login-redirect-url:/pizzaria-default/admin}") String loginRedirectUrl
    ) throws Exception {
        http
                .csrf(csrf -> {
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/favicon.ico").permitAll()
                        .requestMatchers("/universal/**", "/pizzaria-default/styles/**", "/pizzaria-default/scripts/**", "/fonts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/{slug}/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/{slug}", "/{slug}/", "/{slug}/checkout", "/{slug}/checkout/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/{slug}/api/catalog").permitAll()
                        .requestMatchers("/{slug}/admin", "/{slug}/admin/").hasRole("ADMIN")
                        .requestMatchers("/{slug}/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect(loginUrlFor(request.getRequestURI()))))
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler(loginAttemptService, loginRedirectUrl))
                        .failureHandler(authenticationFailureHandler(loginAttemptService))
                        .permitAll())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .addFilterBefore(loginRateLimitFilter(loginAttemptService), UsernamePasswordAuthenticationFilter.class);

        http.headers(header -> header
                .frameOptions(frame -> frame.deny())
                .httpStrictTransportSecurity(hsts -> hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31_536_000))
                .referrerPolicy(referrer -> referrer
                        .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .contentSecurityPolicy(csp -> csp
                        .policyDirectives(
                                "default-src 'self'; " +
                                        "base-uri 'self'; " +
                                        "object-src 'none'; " +
                                        "frame-ancestors 'none'; " +
                                        "form-action 'self'; " +
                                        "script-src 'self'; " +
                                        "img-src 'self' https://dicotbzyonknofyzyuxv.supabase.co https://picsum.photos https://fastly.picsum.photos; " +
                                        "style-src 'self' https://fonts.googleapis.com https://fonts.gstatic.com; " +
                                        "font-src 'self' https://fonts.googleapis.com https://fonts.gstatic.com; "
                        ))
                .addHeaderWriter(new StaticHeadersWriter(
                        "Permissions-Policy",
                        "camera=(), microphone=(), geolocation=(), payment=()"
                ))
        );

        return http.build();
    }

    private OncePerRequestFilter loginRateLimitFilter(LoginAttemptService loginAttemptService) {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain
            ) throws ServletException, IOException {
                if (isLoginPost(request) && loginAttemptService.isBlocked(
                        normalizeUsername(request.getParameter("username")),
                        request.getRemoteAddr()
                )) {
                    response.sendRedirect("/login?error=rate_limit");
                    return;
                }

                filterChain.doFilter(request, response);
            }
        };
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler(
            LoginAttemptService loginAttemptService,
            String loginRedirectUrl
    ) {
        SavedRequestAwareAuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();
        delegate.setDefaultTargetUrl(loginRedirectUrl);
        delegate.setAlwaysUseDefaultTargetUrl(false);

        return (request, response, authentication) -> {
            loginAttemptService.recordSuccess(normalizeUsername(request.getParameter("username")), request.getRemoteAddr());
            delegate.onAuthenticationSuccess(request, response, authentication);
        };
    }

    private AuthenticationFailureHandler authenticationFailureHandler(LoginAttemptService loginAttemptService) {
        return (request, response, exception) -> {
            loginAttemptService.recordFailure(normalizeUsername(request.getParameter("username")), request.getRemoteAddr());
            response.sendRedirect("/login?error");
        };
    }

    private String loginUrlFor(String requestUri) {
        if (requestUri == null || requestUri.isBlank() || "/".equals(requestUri)) {
            return "/login";
        }

        String[] pathParts = requestUri.split("/");
        if (pathParts.length > 2 && !pathParts[1].isBlank()) {
            return "/" + pathParts[1] + "/login";
        }

        return "/login";
    }

    private boolean isLoginPost(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod()) && "/login".equals(request.getRequestURI());
    }

    private String normalizeUsername(String username) {
        return username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
    }

    private String roleName(Object role) {
        if (role == null) {
            return "USER";
        }

        return role.toString().trim().toUpperCase(Locale.ROOT);
    }
}

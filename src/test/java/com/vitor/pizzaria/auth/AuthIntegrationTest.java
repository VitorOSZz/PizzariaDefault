package com.vitor.pizzaria.auth;

import com.vitor.pizzaria.model.entity.UserAccount;
import com.vitor.pizzaria.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Autenticação e autorização")
class AuthIntegrationTest {

    private static final String ADMIN_EMAIL = "admin@pizzaria.test";
    private static final String USER_EMAIL = "user@pizzaria.test";
    private static final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void resetState() {
        loginAttemptService.clear();
        jdbcTemplate.update("delete from spring_session_attributes");
        jdbcTemplate.update("delete from spring_session");
        userAccountRepository.deleteAll();

        saveUser(ADMIN_EMAIL, "ADMIN", true);
        saveUser(USER_EMAIL, "USER", true);
    }

    @Test
    @DisplayName("página de login usa CSRF e headers de segurança")
    void loginPageHasCsrfAndSecurityHeaders() throws Exception {
        mockMvc.perform(get("/login").secure(true))
                .andExpect(status().isOk())
                .andExpect(view().name("universal/auth"))
                .andExpect(content().string(containsString("name=\"_csrf\"")))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("Referrer-Policy", "strict-origin-when-cross-origin"))
                .andExpect(header().string("Cache-Control", containsString("no-store")))
                .andExpect(header().string("Content-Security-Policy", containsString("default-src 'self'")))
                .andExpect(header().string("Content-Security-Policy", containsString("object-src 'none'")))
                .andExpect(header().string("Content-Security-Policy", containsString("base-uri 'self'")))
                .andExpect(header().string("Content-Security-Policy", containsString("form-action 'self'")))
                .andExpect(header().string("Content-Security-Policy", containsString("frame-ancestors 'none'")))
                .andExpect(header().string("Permissions-Policy", containsString("geolocation=()")))
                .andExpect(header().string("Strict-Transport-Security", containsString("max-age=31536000")));
    }

    @Test
    @DisplayName("login padrão do Spring Security cria JSESSIONID e persiste sessão JDBC")
    void formLoginCreatesJsessionIdAndJdbcSession() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "ADMIN@PIZZARIA.TEST")
                        .param("password", PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pizzaria-default/admin"))
                .andExpect(cookie().exists("JSESSIONID"))
                .andExpect(cookie().httpOnly("JSESSIONID", true))
                .andReturn();

        Integer sessionCount = jdbcTemplate.queryForObject("select count(*) from spring_session", Integer.class);
        assertTrue(sessionCount != null && sessionCount > 0);
    }

    @Test
    @DisplayName("login sem CSRF é rejeitado")
    void loginRequiresCsrfToken() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", ADMIN_EMAIL)
                        .param("password", PASSWORD))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("senha errada redireciona para erro sem criar JSESSIONID autenticado")
    void wrongPasswordRedirectsToLoginError() throws Exception {
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", ADMIN_EMAIL)
                        .param("password", "wrong-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @DisplayName("usuário inexistente redireciona para erro")
    void unknownUserRedirectsToLoginError() throws Exception {
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "unknown@pizzaria.test")
                        .param("password", PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @DisplayName("admin redireciona para login do slug sem sessão")
    void adminRedirectsToTenantLoginWithoutSession() throws Exception {
        mockMvc.perform(get("/pizzaria-default/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pizzaria-default/login"));
    }

    @Test
    @DisplayName("rota nova nasce protegida por padrão")
    void unknownRouteIsProtectedByDefault() throws Exception {
        mockMvc.perform(get("/pizzaria-default/api/private"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pizzaria-default/login"));
    }

    @Test
    @DisplayName("admin acessa página administrativa com sessão Spring válida")
    void adminPageLoadsWithValidSpringSession() throws Exception {
        MvcResult loginResult = loginSuccessfully();

        mockMvc.perform(get("/pizzaria-default/admin")
                        .cookie(loginResult.getResponse().getCookie("JSESSIONID")))
                .andExpect(status().isOk())
                .andExpect(view().name("pizzaria-default/admin"))
                .andExpect(header().string("Cache-Control", containsString("no-store")));
    }

    @Test
    @DisplayName("usuário autenticado sem role admin recebe forbidden")
    void adminRejectsAuthenticatedUserWithoutAdminRole() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", USER_EMAIL)
                        .param("password", PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        mockMvc.perform(get("/pizzaria-default/admin")
                        .cookie(loginResult.getResponse().getCookie("JSESSIONID")))
                .andExpect(status().isForbidden())
                .andExpect(header().string("Server", not(containsString("Spring"))));
    }

    @Test
    @DisplayName("rate limit bloqueia POST /login após falhas repetidas")
    void loginIsRateLimitedAfterRepeatedFailuresForUsernameAndIp() throws Exception {
        for (int attempt = 0; attempt < 5; attempt++) {
            mockMvc.perform(post("/login")
                            .with(csrf())
                            .param("username", "rate-limit@pizzaria.test")
                            .param("password", "wrong-password"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?error"));
        }

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "rate-limit@pizzaria.test")
                        .param("password", "wrong-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=rate_limit"));
    }

    private MvcResult loginSuccessfully() throws Exception {
        return mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", ADMIN_EMAIL)
                        .param("password", PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andReturn();
    }

    private void saveUser(String email, String role, boolean active) {
        OffsetDateTime now = OffsetDateTime.now();
        UserAccount user = new UserAccount();
        user.setId(UUID.randomUUID());
        user.setName(email);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(PASSWORD));
        user.setRole(role);
        user.setActive(active);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userAccountRepository.save(user);
    }
}

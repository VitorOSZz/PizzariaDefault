package com.vitor.pizzaria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class ApplicationBeans {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}

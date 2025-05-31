package com.myapp.poc.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Profile("default")
@Configuration
@EnableWebSecurity
public class DefaultSecurityConfig {
    //@Profile("minimal")
    //no extra SecurityFilterChain bean defined.
}

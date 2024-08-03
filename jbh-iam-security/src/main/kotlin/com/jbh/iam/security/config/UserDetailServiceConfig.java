package com.jbh.iam.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserDetailServiceConfig {

  private final Logger logger = LoggerFactory.getLogger(UserDetailServiceConfig.class);

  @Bean
  public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {

    logger.info("Creating InMemoryUserDetailsManager");
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(
        User.withUsername("user")
            .password(bCryptPasswordEncoder.encode("userPass"))
            .roles("USER")
            .build());
    manager.createUser(
        User.withUsername("admin")
            .password(bCryptPasswordEncoder.encode("adminPass"))
            .roles("ADMIN", "USER")
            .build());
    return manager;
  }
}

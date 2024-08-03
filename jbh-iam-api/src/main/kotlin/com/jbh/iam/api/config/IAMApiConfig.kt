package com.jbh.iam.api.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.jbh.iam.api.repositories"])
@EntityScan(basePackages = ["com.jbh.iam.api.domain"])
class IAMApiConfig {
}
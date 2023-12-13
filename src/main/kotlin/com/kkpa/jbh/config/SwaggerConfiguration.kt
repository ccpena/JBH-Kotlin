package com.kkpa.jbh.config

import com.google.common.collect.Lists
import com.kkpa.jbh.util.AuditConstants
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.ApiKeyVehicle
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.Collections

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .securitySchemes(Lists.newArrayList(apiKey()))
        .securityContexts(Collections.singletonList(securityContext()))
        .globalOperationParameters(
            listOf(
                ParameterBuilder()
                    .name(AuditConstants.USER_ID_HEADER)
                    .description("Jbh Party Id")
                    .modelRef(ModelRef("string"))
                    .parameterType("header")
                    .required(true)
                    .build(),
                ParameterBuilder()
                    .name(AuditConstants.EMAIL_HEADER)
                    .description("User email")
                    .modelRef(ModelRef("string"))
                    .parameterType("header")
                    .required(false)
                    .build(),
                ParameterBuilder()
                    .name("Authorization")
                    .description("Authorization")
                    .modelRef(ModelRef("string"))
                    .parameterType("header")
                    .required(false)
                    .build()
            )
        )

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            "My REST API", "Some custom description of API.", "API TOS", "Terms of service",
            "myeaddress@company.com", "License of API", "API license URL"
        )
    }

    private fun apiKey(): ApiKey {
        return ApiKey("Authorization", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("/anyPath.*"))
            .build()
    }

    fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return Lists.newArrayList(
            SecurityReference("AUTHORIZATION", authorizationScopes)
        )
    }

    @Bean
    fun security(): SecurityConfiguration {
        return SecurityConfiguration(
            null, null, null, null, // appName Needed for authenticate button to work
            "BEARER", // apiKeyValue
            ApiKeyVehicle.HEADER, "Authorization", null
        ) // realm Needed for authenticate button to work
    }
}

package com.kkpa.jbh.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.concurrent.TimeUnit

//@Configuration
class WebMvcConfig : WebMvcConfigurer {

    private val MAX_AGE_SECS: Long = TimeUnit.MINUTES.toSeconds(10)

    //TODO AllowCredentials to TRUE.

    /**
     * CORS is a security mechanism that restricts how a web browser makes a request from one domain (origin) to a different domain. By default, browsers prevent such requests to protect users from potential security vulnerabilities like malicious code injection or unauthorized data access.
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
            .allowCredentials(false)
            .maxAge(MAX_AGE_SECS)
    }

    /*private fun corsConfigurationSource(): CorsConfigurationSource? {
     val list: MutableList<String> = ArrayList()
     list.add("*")
     val corsConfiguration = CorsConfiguration()
     corsConfiguration.allowCredentials = true
     corsConfiguration.allowedOrigins = Arrays.stream(corsPropList.getOrigins()).toList()
     corsConfiguration.allowedHeaders = list
     corsConfiguration.allowedMethods = list
     val source = UrlBasedCorsConfigurationSource()
     source.registerCorsConfiguration("/**", corsConfiguration)
     return source
 }*/
  */
}

package com.jbh.iam.viewgateway

import com.jbh.iam.api.config.IAMApiConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import java.util.*

@SpringBootApplication(scanBasePackages = ["com.jbh.iam", "com.jbh.iam.api"])
@Import(IAMApiConfig::class)
class JBHIAMApplication

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    runApplication<JBHIAMApplication>(*args)
}
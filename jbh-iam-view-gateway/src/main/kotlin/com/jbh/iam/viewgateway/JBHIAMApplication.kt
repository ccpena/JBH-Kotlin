package com.jbh.iam.viewgateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.TimeZone

@SpringBootApplication(scanBasePackages = ["com.jbh.iam"])
class JBHIAMApplication

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    runApplication<JBHIAMApplication>(*args)
}
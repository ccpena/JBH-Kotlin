dependencies {
    implementation(project(":jbh-iam-common"))
    implementation("org.springframework.boot:spring-boot-starter-security") {
        version {
            strictly("2.7.7")
        }
    }
    implementation("org.springframework.security:spring-security-config:6.2.0")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    // Add other security-related dependencies
}
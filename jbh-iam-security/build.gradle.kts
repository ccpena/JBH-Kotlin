dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    
    implementation(project(":jbh-iam-common"))
    implementation(project(":jbh-iam-core"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security") {
        version {
            strictly("2.7.7")
        }
    }
    implementation("org.springframework.security:spring-security-config:6.2.0")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    // Add other security-related dependencies
}
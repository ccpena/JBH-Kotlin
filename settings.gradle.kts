plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "jbh"
include("jbh-iam-common")
include("jbh-iam-security")
include("jbh-iam-api")
include("jbh-iam-view-gateway")

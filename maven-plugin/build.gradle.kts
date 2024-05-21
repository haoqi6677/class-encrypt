plugins {
    `java-library`
    id("de.benediktritter.maven-plugin-development") version "0.4.3"
}
description = "给class加解密"


dependencies {
    implementation(project(path = ":lib"))

    implementation("org.apache.maven:maven-core:3.9.6")
    implementation("org.apache.maven:maven-plugin-api:3.9.6")
    compileOnly("org.apache.maven.plugin-tools:maven-plugin-annotations:3.13.0")
    implementation("org.apache.maven.plugin-tools:maven-plugin-tools-generators:3.13.0")

}

mavenPlugin {
    mojos {
        create("reCompilePlugin") {
            implementation = "fuck.world.plugin.maven.ReCompilePlugin"
            description = "给class加解密"
            parameters {
                parameter("outputDirectory", "java.io.File") {
                    defaultValue = "\${project.build.outputDirectory}"
                    isRequired = false
                }
                parameter("pubKey", "java.lang.String") {
                    defaultValue = ""
                    isRequired = true
                }
                parameter("priKey", "java.lang.String") {
                    defaultValue = ""
                    isRequired = true
                }
                parameter("encrypt", "java.lang.Boolean") {
                    isRequired = false
                }
            }
        }
    }
}

apply(rootProject.file("gradle/publish.gradle.kts"))
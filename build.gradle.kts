plugins {
    java
    groovy
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "groovy")

    group = "fuck.world"
    version = "001-SNAPSHOT"

    repositories {
        maven {
            url = uri("http://192.168.1.23:36307/nexus/repository/maven-snapshots/")

            // 如果仓库需要身份验证，配置用户名和密码
            credentials {
                username = "admin"
                password = "password123"
            }
            isAllowInsecureProtocol = true
        }

        maven(url = "https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
        mavenCentral()
    }


    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
//        withJavadocJar()
//        withSourcesJar()
    }

    sourceSets {
        main {
            groovy {
                srcDir("src/main/groovy")
            }
            java {
                srcDir("src/main/java")
            }
            resources {
                srcDir("src/main/resources")
            }
        }
    }

    tasks.test {
        enabled = false
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
    }
    tasks.compileGroovy {
        options.encoding = "UTF-8"
    }
}















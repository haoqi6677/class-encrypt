apply(plugin = "java-library")
apply(plugin = "maven-publish")


configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            // 设置 groupId、artifactId 和 version，如果不指定，默认会使用项目的相关信息
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            // 指定发布的内容来源于 java 组件，即 main 源集编译后的 jar 文件
            from(components["java"])

            // 如果还需要发布源码 jar，可以创建并添加额外的构件
            /*tasks.register<Jar>("sourceJar") {
                archiveClassifier.set("sources")
                from(sourceSets["main"].allSource)
            }
            artifact(tasks["sourceJar"])*/
        }
    }

    // 配置仓库
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
    }
}
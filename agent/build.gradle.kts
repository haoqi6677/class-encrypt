dependencies {
    implementation(project(path = ":lib"))
}
tasks.jar {
    manifest {
        attributes(
//            "Main-Class" to "fuck.world.agent.AgentMain",
//            "Agent-Class" to "fuck.world.agent.AgentMain",
            "Premain-Class" to "fuck.world.agent.AgentMain",
            // true表示能重定义此代理所需的类，默认值为 false（可选）
            "Can-Redefine-Classes" to "true",
            //true 表示能重转换此代理所需的类，默认值为 false （可选）
            "Can-Retransform-Classes" to "true",
            // true表示能设置此代理所需的本机方法前缀，默认值为 false（可选）
            "Can-Set-Native-Method-Prefix" to "true"
        )
    }

    // 将 lib 项目的 JAR 包包含进来
    dependsOn(":lib:jar")
    from(project(":lib").tasks.named("jar").map { zipTree(it.outputs.files.singleFile) })
}
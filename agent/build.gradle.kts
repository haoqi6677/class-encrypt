dependencies {
    api(project(path = ":lib"))
}
tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "fuck.world.agent.AgentMain",
            "Agent-Class" to "fuck.world.agent.AgentMain",
            "Premain-Class" to "fuck.world.agent.AgentMain",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true",
            "Can-Set-Native-Method-Prefix" to "true"
        )
    }

    // 将 lib 项目的 JAR 包包含进来
    dependsOn(":lib:jar")
    from(project(":lib").tasks.named("jar").map { zipTree(it.outputs.files.singleFile) })
}
plugins {
    `java-library`
    id("java-gradle-plugin")
}
description = "给class加解密"

dependencies {
    implementation(project(path = ":lib"))
}

gradlePlugin {
    plugins {
        create("re-compile-gradle") {
            id = "fuck.world.re-compile-gradle"
            implementationClass = "fuck.world.plugin.gradle.ReCompilePlugin"
            description = "给class加解密"
        }
    }
}

apply(rootProject.file("gradle/publish.gradle.kts"))



tasks.withType<GenerateMavenPom>().configureEach {
    //generatePomFileForMavenPublication
    //
    if (name != "generatePomFileForPluginMavenPublication"
        && name != "generatePomFileForRe-compile-gradlePluginMarkerMavenPublication"
    ) {
        enabled = false
    }
}

tasks.withType<PublishToMavenRepository>().configureEach {
    //publishMavenPublicationToMavenRepository
    //
    if (name != "publishPluginMavenPublicationToMavenRepository"
        && name != "publishRe-compile-gradlePluginMarkerMavenPublicationToMavenRepository"
    ) {
        enabled = false
    }
}
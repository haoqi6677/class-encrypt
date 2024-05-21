dependencies {
    implementation(project(path = ":lib"))

    testImplementation("net.bytebuddy:byte-buddy:1.14.14")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    enabled = true
    useJUnitPlatform()
}
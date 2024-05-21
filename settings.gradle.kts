rootProject.name = "class-encrypt"
include("lib")
include("agent")
include("gradle-plugin")
include("maven-plugin")
project(":gradle-plugin").name = "re-compile-gradle"
project(":maven-plugin").name = "re-compile-maven"
include("test")

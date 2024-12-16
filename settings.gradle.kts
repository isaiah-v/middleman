buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.github.spullara.mustache.java:compiler:0.9.10")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "middleman"

include("server")
include("tools")
include("scripts")

project(":server").name = "middleman-server"
project(":tools").name = "middleman-tools"
project(":scripts").name = "middleman-scripts"
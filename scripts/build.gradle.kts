import com.github.mustachejava.DefaultMustacheFactory
import java.io.File

plugins {
    kotlin("jvm") version "2.0.21"
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.spullara.mustache.java:compiler:0.9.10")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("processTemplates") {
    val inputDir = file("src/main/resources/templates")
    val outputDir = file("$buildDir/generated/templates")

    inputs.dir(inputDir)
    outputs.dir(outputDir)

    doLast {
        val mustacheFactory = DefaultMustacheFactory()
        inputDir.listFiles()?.forEach { templateFile ->
            val mustache = mustacheFactory.compile(templateFile.reader(), templateFile.name)
            val outputFile = File(outputDir, templateFile.name.replace(".mustache", ".html"))
            outputFile.parentFile.mkdirs()
            outputFile.writer().use { writer ->
                mustache.execute(writer, mapOf("key" to "value")).flush()
            }
        }
    }
}

tasks.build {
    dependsOn("processTemplates")
}

kotlin {
    jvmToolchain(21)
}
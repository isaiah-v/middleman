import com.github.mustachejava.DefaultMustacheFactory
import java.io.File

plugins {
    kotlin("jvm") version "2.0.21"
}

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
    val inputDir = projectDir.resolve("src/main/resources/templates")
    val outputDir = layout.buildDirectory.dir ("generated/templates").get().asFile

    inputs.dir(inputDir)
    outputs.dir(outputDir)

    doLast {
        val toolsModule = project(":middleman-tools")
        val serverModule = project(":middleman-server")

        val scope = mapOf(
            "winLibsPath" to "..\\lib",
            "toolsJar" to "${toolsModule.name}-${toolsModule.version}.jar",
            "serverJar" to "${serverModule.name}-${serverModule.version}.jar"
        )

        val mustacheFactory = DefaultMustacheFactory()
        inputDir.listFiles()?.forEach { templateFile ->
            val mustache = mustacheFactory.compile(templateFile.reader(), templateFile.name)
            val outputFile = File(outputDir, templateFile.name.removeSuffix(".mustache"))
            outputFile.parentFile.mkdirs()
            outputFile.writer().use { writer ->
                mustache.execute(writer, scope).flush()
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
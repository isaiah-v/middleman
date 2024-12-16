group = "org.ivcode.middleman"
version = "1.0.0-SNAPSHOT"

subprojects.forEach {
    it.group = project.group
    it.version = project.version
}

tasks.register<Copy>("copyToolsJar") {
    val tools = project(":middleman-tools")

    from(tools.tasks.getByName("shadowJar").outputs)
    into(layout.buildDirectory.dir("lib"))

    dependsOn(tools.tasks.getByName("shadowJar"))
}

tasks.register<Copy>("copyServerJar") {
    val server = project(":middleman-server")

    from(server.tasks.getByName("bootJar").outputs)
    into(layout.buildDirectory.dir("lib"))

    dependsOn(server.tasks.getByName("bootJar"))
}

tasks.register<Copy>("copyBatchFile") {
    val scripts = project(":middleman-scripts")

    from(scripts.layout.buildDirectory.dir("/generated/templates"))
    into(layout.buildDirectory.dir("bin"))

    dependsOn(scripts.tasks.getByName("processTemplates"))
}

tasks.register("build") {
    dependsOn("copyToolsJar", "copyServerJar", "copyBatchFile")
}

tasks.register("clean") {
    delete(layout.buildDirectory)
    dependsOn(":middleman-tools:clean", ":middleman-server:clean", ":middleman-scripts:clean")
}
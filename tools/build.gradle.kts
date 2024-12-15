plugins {
    kotlin("jvm") version "2.0.21"
}

group = "org.ivcode"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")

    implementation("com.xenomachina:kotlin-argparser:2.0.7")

    implementation("commons-codec:commons-codec:1.17.1")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
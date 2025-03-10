plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

group = "tech.e_psi_lon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.radsteve.net/public")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.kore)
    implementation(libs.kotter)
    implementation(libs.packed)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

application {
    mainClass.set("tech.e_psi_lon.ore_crops.MainKt")
}

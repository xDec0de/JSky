plugins {
    `java-library`
    `java-test-fixtures`
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.jetbrains.annotations)

    testCompileOnly(libs.jetbrains.annotations)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

java {
    withSourcesJar()
}

tasks {
    jar {
        archiveFileName.set("JSky-${project.version}.jar")
    }
}

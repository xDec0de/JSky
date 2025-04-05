plugins {
	java
	id("com.gradleup.shadow") version libs.versions.shadow
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(project(":base"))
	compileOnly(libs.jetbrains.annotations)
	implementation(libs.snakeyaml)
}

java {
	withSourcesJar()
}

tasks {
	shadowJar {
		archiveFileName.set("JSky-yaml-${project.version}.jar")
		// We do NOT relocate dependencies. Why? https://docs.codersky.net/jsky/about-modules#about-jsky-dependencies
	}
}

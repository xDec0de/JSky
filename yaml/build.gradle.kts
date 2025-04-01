plugins {
	`java-library`
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
	jar {
		archiveFileName.set("JSky-yaml-${project.version}.jar")
	}
}

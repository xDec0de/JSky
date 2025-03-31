plugins {
	`java-library`
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(libs.jetbrains.annotations)
}

java {
	withSourcesJar()
}

tasks {
	jar {
		archiveFileName.set("JSky-${project.version}.jar")
		archiveClassifier = "default"
	}
}

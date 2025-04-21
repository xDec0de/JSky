plugins {
	`java-library`
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(libs.jetbrains.annotations)

	testCompileOnly(libs.jetbrains.annotations)
	testImplementation(platform(libs.junit.bom))
	testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
	withSourcesJar()
}

tasks {
	jar {
		archiveFileName.set("JSky-${project.version}.jar")
	}
}

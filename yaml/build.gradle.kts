plugins {
	java
	`java-test-fixtures`
	id("com.gradleup.shadow") version libs.versions.shadow
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(project(":base"))
	compileOnly(libs.jetbrains.annotations)
	implementation(libs.snakeyaml)

	testImplementation(project(":base"))
	testImplementation(testFixtures(project(":base")))
	testCompileOnly(libs.jetbrains.annotations)
	testImplementation(libs.snakeyaml)
	testImplementation(platform(libs.junit.bom))
	testImplementation("org.junit.jupiter:junit-jupiter")
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

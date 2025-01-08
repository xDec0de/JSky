
group = "net.codersky"
version = "1.0.0-SNAPSHOT"

plugins {
	id("java")
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(libs.jetbrains.annotations)
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
	useJUnitPlatform()
}
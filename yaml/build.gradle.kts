plugins {
	id("java")
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(libs.jetbrains.annotations)
	compileOnly(project(":base"))
}

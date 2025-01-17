plugins {
	id("java")
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(project(":base"))
	compileOnly(libs.jetbrains.annotations)
	implementation(libs.snakeyaml)
}

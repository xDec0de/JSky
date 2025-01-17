
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
}

tasks {

	wrapper {
		gradleVersion = "8.8"
		distributionType = Wrapper.DistributionType.ALL
	}

	val libsPath = "libs"

	// Configure the existing build task
	named("build") {
		dependsOn(subprojects.map { it.tasks.named("build") })
		doLast {
			val buildOut = project.layout.buildDirectory.dir(libsPath).get().asFile.apply {
				if (!exists()) mkdirs()
			}
			subprojects.forEach { subproject ->
				val subIn = subproject.layout.buildDirectory.dir(libsPath).get().asFile
				if (subIn.exists()) {
					copy {
						from(subIn) {
							include("JSky-*.jar")
							exclude("*-javadoc.jar", "*-sources.jar")
						}
						into(buildOut)
					}
				}
			}
		}
	}

	// Configure the existing clean task
	named("clean") {
		dependsOn(subprojects.map { it.tasks.named("clean") })
		doFirst {
			delete(rootProject.layout.buildDirectory)
		}
	}

	defaultTasks("build")
}
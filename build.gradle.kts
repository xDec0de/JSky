
group = "net.codersky"
version = "1.0.0-SNAPSHOT"

plugins {
	java
}

tasks {

	wrapper {
		gradleVersion = "8.8"
		distributionType = Wrapper.DistributionType.ALL
	}

	named("build") {
		dependsOn(subprojects.map { it.tasks.named("build") })
		doLast {
			val buildOut = project.layout.buildDirectory.dir("libs").get().asFile.apply {
				if (!exists()) mkdirs()
			}

			subprojects.forEach { subproject ->
				val subIn = subproject.layout.buildDirectory.dir("libs").get().asFile
				val jarFiles = subIn.listFiles()?.filter { it.name.endsWith(".jar") }

				if (jarFiles.isNullOrEmpty())
					println("No JAR found in subproject: ${subproject.name}")
				else {
					jarFiles.forEach { jarFile ->
						var fileName = "JSky-${subproject.name}-${version}.jar";
						if (subproject.name.equals("base"))
							fileName = "JSky-${version}.jar";
						copy {
							from(jarFile)
							into(buildOut)
							rename { fileName }
						}
					}
				}
			}
		}
	}

	named("clean") {
		dependsOn(subprojects.map { it.tasks.named("clean") })
		doFirst {
			delete(rootProject.layout.buildDirectory)
		}
	}

	defaultTasks("build")
}
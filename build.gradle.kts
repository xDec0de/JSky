
group = "net.codersky"
version = "1.0.0-SNAPSHOT"

plugins {
	java
	`maven-publish`
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

subprojects {

	apply(plugin = "java-library")
	apply(plugin = "maven-publish")

	tasks.register<Jar>("sourcesJar") {
		archiveClassifier.set("sources")
		from(sourceSets.main.get().allSource)
	}

	version = rootProject.version.toString()

	publishing {
		publishing {
			repositories {
				maven {
					val snapshot = version.toString().endsWith("SNAPSHOT")
					url = uri("https://repo.codersky.net/" + if (snapshot) "snapshots" else "releases")
					name = if (snapshot) "cskSnapshots" else "cskReleases"
					credentials(PasswordCredentials::class)
					authentication {
						create<BasicAuthentication>("basic")
					}
				}
			}
		}

		publications {
			create<MavenPublication>("maven") {
				groupId = "${rootProject.group}.${project.group.toString().lowercase()}"
				artifactId = project.name // Use the subproject name as the artifactId

					packaging = "jar"
				}

				// Include the main JAR
				artifact(tasks["jar"])
				// Include the sources JAR
				artifact(tasks["sourcesJar"])
			}
		}
	}
}

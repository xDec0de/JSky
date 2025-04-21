import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	java
	`maven-publish`
	id("com.gradleup.shadow") version libs.versions.shadow
	jacoco
}

group = "net.codersky"
version = "1.0.0-SNAPSHOT"

repositories {
	mavenCentral()
}

jacoco {
	toolVersion = "0.8.11"
}

tasks {
	wrapper {
		gradleVersion = "8.8"
		distributionType = Wrapper.DistributionType.ALL
	}

	named("assemble") {
		dependsOn(subprojects.map { it.tasks.named("assemble") })
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

	register<JacocoReport>("codeCoverageReport") {
		group = "verification"
		description = "Generates combined coverage report"

		dependsOn("build")

		executionData.setFrom(fileTree(project.rootDir).include("**/build/jacoco/*.exec"))

		sourceDirectories.setFrom(files(subprojects.flatMap {
			it.sourceSets.main.get().allSource.srcDirs
		}))

		classDirectories.setFrom(files(subprojects.flatMap {
			it.sourceSets.main.get().output.classesDirs.files
		}))

		reports {
			xml.required.set(true)
			html.required.set(true)
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
	repositories {
		mavenCentral()
	}

	apply(plugin = "java-library")
	apply(plugin = "maven-publish")
	apply(plugin = "com.gradleup.shadow")
	apply(plugin = "jacoco")

	tasks {
		named("assemble") {
			dependsOn("shadowJar")
		}

		register<Jar>("sourcesJar") {
			archiveClassifier.set("sources")
			from(sourceSets.main.get().allSource)
		}

		withType<Test>().configureEach {
			useJUnitPlatform()

			testLogging {
				events = setOf(
					TestLogEvent.FAILED,
					TestLogEvent.SKIPPED,
					TestLogEvent.PASSED
				)
				exceptionFormat = TestExceptionFormat.FULL
				showExceptions = true
				showCauses = true
				showStackTraces = true
			}

			configure<JacocoTaskExtension> {
				isEnabled = true
				excludes = listOf("jdk.internal.*")
			}
		}
	}

	version = rootProject.version.toString()

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

		publications {
			create<MavenPublication>("maven") {
				groupId = "${rootProject.group}.${project.group.toString().lowercase()}"
				artifactId = project.name // Use the subproject name as the artifactId

				pom {
					packaging = "jar"
				}

				// Include the main JAR
				artifact(tasks["shadowJar"]) {
					classifier = ""
				}
				// Include the sources JAR
				artifact(tasks["sourcesJar"])
			}
		}
	}
}

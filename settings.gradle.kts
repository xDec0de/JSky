rootProject.name = "JSky"

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("libs.versions.toml"))
		}
	}
}

include("base")
include("yaml")

# JSky ![Code quality](https://app.codacy.com/project/badge/Grade/6b62b33f416e4125be5e60c1ecb34c58) ![Test coverage](https://app.codacy.com/project/badge/Coverage/6b62b33f416e4125be5e60c1ecb34c58) ![Workflow status](https://github.com/xDec0de/JSky/actions/workflows/test.yml/badge.svg)

A lightweight multipurpose Java utility library

Detailed code quality and coverage stats can be found
[here](https://app.codacy.com/gh/xDec0de/JSky).

## Modules

JSky is split into two artifacts so consumers only pull in what they use.

| Module | Coordinates | Depends on |
| :---: | :--- | :---: |
| `base` | `net.codersky.jsky:base` | — |
| `yaml` | `net.codersky.jsky:yaml` | `base` + SnakeYAML |

The `yaml` module ships its SnakeYAML dependency unrelocated on purpose; consumers that already provide SnakeYAML can exclude it.

## Using JSky

Snapshots are published to `https://repo.codersky.net/snapshots`. Releases will go to `/releases` once the API stabilizes.

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://repo.codersky.net/snapshots")
}

dependencies {
    implementation("net.codersky.jsky:base:1.0.0-SNAPSHOT")
    implementation("net.codersky.jsky:yaml:1.0.0-SNAPSHOT") // optional
}
```

### Gradle (Groovy DSL)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://repo.codersky.net/snapshots' }
}

dependencies {
    implementation 'net.codersky.jsky:base:1.0.0-SNAPSHOT'
    implementation 'net.codersky.jsky:yaml:1.0.0-SNAPSHOT' // optional
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>codersky-snapshots</id>
        <url>https://repo.codersky.net/snapshots</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>net.codersky.jsky</groupId>
        <artifactId>base</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>net.codersky.jsky</groupId>
        <artifactId>yaml</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Building from source

JSky compiles with Java 25 and uses the Gradle wrapper, so you only need a JDK installed.

```sh
git clone https://github.com/xDec0de/JSky.git
cd JSky
./gradlew build
```

Common tasks:

| Task | Description |
| :---: | :---: |
| `./gradlew build` | Compile, test, and assemble all artifacts |
| `./gradlew test` | Run the test suite |
| `./gradlew codeCoverageReport` | Generate the aggregated JaCoCo coverage report |
| `./gradlew :base:assemble` | Assemble only the `base` artifact |
| `./gradlew :yaml:assemble` | Assemble only the `yaml` artifact |

The aggregated coverage report lands at `build/reports/jacoco/codeCoverageReport/codeCoverageReport.xml`, with a browsable HTML version next to it.

## License

JSky is released under the [MIT License](LICENSE).

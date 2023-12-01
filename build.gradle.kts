plugins {
    id("java")
}

java.toolchain {
    languageVersion = JavaLanguageVersion.of(22)
}

group = "me.djtheredstoner"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

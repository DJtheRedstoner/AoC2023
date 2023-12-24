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

dependencies {
    implementation("org.ow2.sat4j:org.ow2.sat4j.core:2.3.6")
    implementation(project.files("com.microsoft.z3.jar"))
}

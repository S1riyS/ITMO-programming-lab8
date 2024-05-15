val MAIN_CLASS = "s1riys.lab8.server.Main"

plugins {
    application
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":apps:common"))
    implementation("com.google.guava:guava:32.1.1-jre")
    implementation("org.apache.commons:commons-lang3:3.14.0")

    implementation("au.com.bytecode:opencsv:2.4")

    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")

    implementation("org.postgresql:postgresql:42.7.0")
    implementation("javax.persistence:javax.persistence-api:2.2")
    implementation("org.hibernate:hibernate-core:6.1.5.Final")
    implementation("org.hibernate:hibernate-validator:7.0.5.Final")

    implementation("io.github.cdimascio:dotenv-java:2.3.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set(MAIN_CLASS)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}


tasks.jar {
    manifest.attributes["Main-Class"] = MAIN_CLASS
    val dependencies = configurations.runtimeClasspath.get().map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
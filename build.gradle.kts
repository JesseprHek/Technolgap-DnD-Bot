import org.gradle.jvm.tasks.Jar

plugins {
    idea // Plugin to add support for the IntelliJ IDE
    application // Plugin to tell Gradle "Hey, this is a runnable application! Package it like one"
    id("java") // Tell Gradle this is a java project
}

group = "org.dndbot"
version = "6.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // We add the JDA dependency here, allowing gradle to download it from maven central. Make sure to use the latest version if you can!
    implementation("net.dv8tion:JDA:5.0.0-beta.20")

    // https://mvnrepository.com/artifact/commons-cli/commons-cli - Additional CLI library for argument parsing
    implementation("commons-cli:commons-cli:1.5.0")

}

tasks.test {
    useJUnitPlatform()
}

application {
    // Tell Gradle what the main class is that it should run, this differs per project based on your packages and classes.
    mainClass.set("org.mineacademy.Main")
}

// We make a task "farJar", this is a task that will create a jar file that contains all the dependencies.
val fatJar = task("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Implementation-Title"] = "MineAcademyBot"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "org.mineacademy.Main" // Same mainclass as the application plugin setting
    }
    from(configurations.runtimeClasspath.get().map{ if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
}

tasks {
    "build" {
        // And we tell during the build task, that it DEPENDS on the fatJar task, so it will always run the fatJar task before the build task.
        dependsOn(fatJar)
    }
}
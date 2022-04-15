plugins {
    `java-library`
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "de.helixdevs"
version = "1.0.3"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    api("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")

    implementation("org.jetbrains:annotations:23.0.0")
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    runServer {
        minecraftVersion("1.18.2")
    }
    test {
        useJUnitPlatform()
    }
}

bukkit {
    name = "EntityTraveller"
    description = "With this plugin you can travel with the leashed entities."
    main = "de.helixdevs.entitytraveller.EntityTravellerPlugin"
    apiVersion = "1.15"
    website = "https://www.spigotmc.org/resources/entity-traveller.101290/"
    authors = listOf("CyntrixAlgorithm")
    permissions {
        register("entitytraveller.update") {
            default = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP
            description = "Gives the player the permission to receive notification when a newer plugin version is available."
        }
    }
}
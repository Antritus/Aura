import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml
import xyz.jpenilla.resourcefactory.paper.paperPluginYaml
import xyz.jpenilla.runpaper.task.RunServer

plugins {
	`my-conventions`
	id("io.papermc.paperweight.userdev") version "2.0.0-beta.19" apply false
	id("xyz.jpenilla.run-paper") version "3.0.2" // Adds runServer task for testing
	id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.3.0" // Generates plugin.yml based on the Gradle config
	id("com.gradleup.shadow") version "9.2.2"
	id("maven-publish")
}

java.disableAutoTargetJvm() // Allow consuming JVM 21 projects (i.e. paper_1_21_8) even though our release is 17

repositories {
	maven("https://jitpack.io")
}

dependencies {
	compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

	compileOnly("com.github.AstralLiteratureClub:MoreForJava:1.0.2")
	compileOnly("com.github.AstralLiteratureClub:MessageManager:2.4.1")
	compileOnly("com.github.AstralLiteratureClub:GUIMan:1.3.1-6") // Currently developing newer version of this. It has recoded API and supports < 1.20.5 versions as it now supports 1.20.5+ only
	compileOnly("com.github.AstralLiteratureClub:CloudPlusPlus:1.3.0")

	implementation(project(":api"))

	// Shade the reobf variant
	runtimeOnly(project(":paper_1_17_1", configuration = "reobf"))
	runtimeOnly(project(":paper_1_19_4", configuration = "reobf"))

//	runtimeOnly(project(":v1_20", configuration = "reobf"))
	// For Paper 1.20.5+, we don't need to use the reobf variant.
	// If you still support spigot, you will need to use the reobf variant,
	// and remove the Mojang-mapped metadata from the manifest below.
	runtimeOnly(project(":v1_20_5"))
	runtimeOnly(project(":v1_21"))
}

tasks.assemble {
	dependsOn(tasks.shadowJar)
}

tasks.jar {
	manifest.attributes(
		"paperweight-mappings-namespace" to "mojang",
	)
}

tasks.shadowJar {
	mergeServiceFiles()
	// Needed for mergeServiceFiles to work properly in Shadow 9+
	filesMatching("META-INF/services/**") {
		duplicatesStrategy = DuplicatesStrategy.INCLUDE
	}
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = project.group.toString();
			artifactId = project.name
			version = project.version.toString()
			from(components["java"])
		}
	}
}

// Configure plugin.yml generation
// - name, version, and description are inherited from the Gradle project.
bukkitPluginYaml {
	main = "bet.astral.aura.AuraPlugin"
	load = BukkitPluginYaml.PluginLoadOrder.STARTUP
	authors.add("Laakkonen A.")
	apiVersion = "1.17"
}

paperPluginYaml {
	main = "bet.astral.aura.AuraPlugin"
	bootstrapper = "bet.astral.aura.AuraBootstrap"
	authors.add("Laakkonen A.")
	apiVersion = "1.17"
}

tasks.runServer {
	minecraftVersion("1.21")
}

tasks.register("run1_21_11", RunServer::class) {
	minecraftVersion("1.21.11")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21_11")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}
tasks.register("run1_21_10", RunServer::class) {
	minecraftVersion("1.21.10")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21_10")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}
tasks.register("run1_21_9", RunServer::class) {
	minecraftVersion("1.21.9")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21_9")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}
tasks.register("run1_21_8", RunServer::class) {
	minecraftVersion("1.21.8")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21_8")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}
tasks.register("run1_21_7", RunServer::class) {
	minecraftVersion("1.21.7")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21_7")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}
tasks.register("run1_21_6", RunServer::class) {
	minecraftVersion("1.21.6")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21_6")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}
tasks.register("run1_21_5", RunServer::class) {
	minecraftVersion("1.21.5")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21_5")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}
tasks.register("run1_21_4", RunServer::class) {
	minecraftVersion("1.21.4")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21_4")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}
tasks.register("run1_21_3", RunServer::class) {
	minecraftVersion("1.21.3")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21_3")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}
tasks.register("run1_21_1", RunServer::class) {
	minecraftVersion("1.21.1")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21_1")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}
tasks.register("run1_21", RunServer::class) {
	minecraftVersion("1.21")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_21")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}

tasks.register("run1_20_6", RunServer::class) {
	minecraftVersion("1.20.6")
	pluginJars.from(tasks.shadowJar.flatMap { it.archiveFile })
	runDirectory = layout.projectDirectory.dir("run1_20_6")
	systemProperties["Paper.IgnoreJavaVersion"] = true
}

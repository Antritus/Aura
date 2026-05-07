plugins {
	`my-conventions`
	java
}

repositories {
	maven("https://repo.codemc.io/repository/nms/")
}

dependencies {
	implementation(project(":api"))
	implementation(project(":legacy"))
	implementation("it.unimi.dsi:fastutil:8.2.2")
	compileOnly("org.jetbrains:annotations:24.0.0")

	compileOnly("org.spigotmc:spigot:1.13-R0.1-SNAPSHOT")
//	compileOnly(files("../libs/spigot-1.13.jar"))
}

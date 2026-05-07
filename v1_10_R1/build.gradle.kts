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
	compileOnly("org.jetbrains:annotations:24.0.0")

	compileOnly("org.spigotmc:spigot:1.10.2-R0.1-SNAPSHOT")
//	compileOnly(files("../libs/spigot-1.10.2.jar"))
}

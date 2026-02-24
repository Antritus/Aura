plugins {
	`my-conventions`
	java
}

dependencies {
	implementation(project(":api"))
	implementation(project(":legacy"))
	implementation("it.unimi.dsi:fastutil:8.2.2")
	compileOnly("org.jetbrains:annotations:24.0.0")

	compileOnly(files("../libs/spigot-1.16.3.jar"))
}

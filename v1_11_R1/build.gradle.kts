plugins {
	`my-conventions`
	java
}

dependencies {
	implementation(project(":api"))
	implementation(project(":legacy"))
	compileOnly("org.jetbrains:annotations:24.0.0")

	compileOnly(files("../libs/spigot-1.11.2.jar"))
}

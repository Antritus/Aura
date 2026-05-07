plugins {
  `my-conventions`
  id("io.papermc.paperweight.userdev")
}

dependencies {
  implementation(project(":api"))

	paperweight.paperDevBundle("26.1.1.build.+") // What the fuck is this naming scheme
	// paperweight.foliaDevBundle("1.21.10-R0.1-SNAPSHOT")
  // paperweight.devBundle("com.example.paperfork", "1.21.10-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile>().configureEach {
	// Override release for newer MC
	options.release = 25
}

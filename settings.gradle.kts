plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "aura"

include("api")

// 1.17
include("v1_17_R1")
// 1.18
include("v1_18_R1")
// 1.18.2
include("v1_18_R2")
// 1.19
include("v1_19_R1")
// 1.19.3
include("v1_19_R2")
// 1.19.4
include("v1_19_R3")
// 1.20.1
include("v1_20_R1")
// 1.20.2
include("v1_20_R2")
// 1.20.3
include("v1_20_R3")
// 1.20.5 -> 1.21.*
include("v1_20_R4")


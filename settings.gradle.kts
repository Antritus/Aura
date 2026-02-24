plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "aura"

include("api")

include("legacy")

// 1.9
include("v1_9_R1")
// 1.9.3
include("v1_9_R2")
// 1.10
include("v1_10_R1")
// 1.11
include("v1_11_R1")
// 1.12
include("v1_12_R1")
// 1.13
include("v1_13_R1")
// 1.13.2
include("v1_13_R2")
// 1.14.4
include("v1_14_R1")
// 1.15
include("v1_15_R1")
// 1.16
include("v1_16_R1")
// 1.16.2
include("v1_16_R2")
// 1.16.3+
include("v1_16_R3")
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

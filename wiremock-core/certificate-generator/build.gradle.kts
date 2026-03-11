plugins {
  id("wiremock.common-conventions")
  id("com.gradleup.shadow")
}

tasks.shadowJar {
  archiveClassifier = ""
  description = "Create a shadow JAR of all dependencies"
  minimize()
  configurations = listOf(
    project.configurations.compileClasspath.get(),
  )
}

tasks.jar {
  enabled = false
}

dependencies {
  compileOnly(libs.bouncycastle.bcpkix)
  compileOnly(libs.bouncycastle.bcprov)
}

publishing {
  publications {
    create<MavenPublication>("shadow") {
      artifactId = tasks.shadowJar.get().archiveBaseName.get()
      artifact(tasks.shadowJar)
      pom {
        name = "WireMock Bouncy Castle Wrapper"
        description = "A bouncy castle wrapper with reduced size"
      }
    }
  }
}

// Disable the plain jar from being published/consumed
configurations.apiElements {
  outgoing.artifacts.clear()
  outgoing.artifact(tasks.shadowJar)
}

configurations.runtimeElements {
  outgoing.artifacts.clear()
  outgoing.artifact(tasks.shadowJar)
}

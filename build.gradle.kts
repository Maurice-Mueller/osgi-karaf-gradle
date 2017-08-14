import org.gradle.jvm.tasks.Jar

buildscript {
  project.extra.set("junit_platform", "1.0.0-M4") //must match the version in enum Versions
  project.extra.set("kotlin_version", "1.1.3-2") //must match the version in enum Versions

  repositories {
    mavenCentral()
  }

  dependencies {
    classpath("org.junit.platform:junit-platform-gradle-plugin:${project.extra.get("junit_platform")}")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra.get("kotlin_version")}")
  }
}

project.version = "1.0.0-dev"

enum class Versions(val version: String) {
  KOTLIN("1.1.3-2"), //must match the version in buildscript{}
  JUNIT_PLATFORM("1.0.0-M4"), //must match the version in buildscript{}
  SPEK("1.1.2"),
  //change the following versions only if really necessary
  JUNIT_LAUNCHER(JUNIT_PLATFORM.version),
  SPEK_ENGINE(SPEK.version),
  SPEK_API(SPEK.version),
  KOTLIN_TEST(KOTLIN.version),
  KOTLIN_REFLECT(KOTLIN.version),
  CXF("3.1.10"),
  OSGi("6.0.0"),
  JACKSON("2.8.5");

  override fun toString(): String = version
}

//val startParameter by test //example to access a start parameter ./gradlew -Ptest=MyValue

repositories {
  mavenCentral()
}

apply {
  plugin("org.junit.platform.gradle.plugin")
}

plugins {
  kotlin("jvm")
  java
  application
  idea
}

application {
  mainClassName = "com.esentri.ex2b.Activator"
}

dependencies {
  compile(kotlin("stdlib"))
  compile("org.apache.cxf:cxf-rt-frontend-jaxrs:${Versions.CXF}")
  compile("org.apache.cxf:cxf-rt-transports-http:${Versions.CXF}")
  compile("org.apache.cxf:cxf-rt-transports-http-jetty:${Versions.CXF}")
  compile("org.apache.cxf:cxf-rt-rs-client:${Versions.CXF}")
  compile("com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:${Versions.JACKSON}")
  compile("org.osgi:org.osgi.core:${Versions.OSGi}")
  compile("org.osgi:osgi.cmpn:${Versions.OSGi}")
  testCompile("org.jetbrains.kotlin:kotlin-test:${Versions.KOTLIN_TEST}")
  testCompile("org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN_REFLECT}")
  testCompile("org.jetbrains.spek:spek-api:${Versions.SPEK_API}")
  testCompile("org.junit.platform:junit-platform-launcher:${Versions.JUNIT_LAUNCHER}")
  testRuntime("org.jetbrains.spek:spek-junit-platform-engine:${Versions.SPEK_ENGINE}")
}

val fatJar = task("fatJar", type = Jar::class) {
  baseName = "${project.name}-fat"

  manifest {
    attributes["Manifest-Version"] = 1.0
    attributes["Bundle-ManifestVersion"] = 2
    attributes["Bundle-Name"] = baseName
    attributes["Bundle-SymbolicName"] = baseName
    attributes["Bundle-Description"] = "Nice project"
    attributes["Bundle-Version"] = "1.0.0"
    attributes["Bundle-Activator"] = application.mainClassName
    attributes["Import-Package"] = "org.osgi.framework;com.esentri.ex2.service"
  }
  from(configurations.runtime.map({ if (it.isDirectory) it else zipTree(it) }))
  with(tasks["jar"] as CopySpec)
}

val karafDockerImageName = "karaf-server"
val karafDockerContainerName = "karaf"

val startKaraf = task("startKaraf", type = Exec::class) {
  commandLine = arrayListOf("docker", "start", karafDockerContainerName)
}

task("stopKaraf", type = Exec::class) {
  commandLine = arrayListOf("docker", "stop", karafDockerContainerName)
}

task("deployKaraf", type = Exec::class) {
  commandLine = arrayListOf("echo", "started successfully")
}

val runKaraf = task("runKaraf", type = Exec::class) {

  println("Did you mean: startKaraf?")

  val exposedPorts = arrayListOf("1099", "8101", "8181", "44444")

  var portParameters = arrayListOf<String>()
  exposedPorts.forEach {
    portParameters.add("-p")
    portParameters.add(it)
  }

  commandLine = arrayListOf("docker", "run", "-d", "--name", karafDockerContainerName, "-p", "1099:1099", karafDockerImageName)
}

val buildKaraf = task("buildKaraf") {
  doLast {
    exec {
      val context = "src/docker"
      val dockerFile = "src/docker/Karaf.Dockerfile"
      commandLine = arrayListOf("docker", "build", "-t", karafDockerImageName, "-f", dockerFile, context)
    }
    runKaraf.execute()
    println("hello")
    exec {
      println("hello2")
      commandLine = arrayListOf("docker", "exec", "-t", karafDockerContainerName, "/opt/karaf/wait-for-karaf-start.sh")
    }
    exec {
      println("hello3")
      commandLine = arrayListOf("docker", "exec", "-t", karafDockerContainerName, "/opt/karaf/karaf-features-install.sh")
    }
  }
}

val rmKarafContainer = task("rmKarafContainer", type = Exec::class) {
  commandLine = arrayListOf("docker", "rm", karafDockerContainerName)
}

task("bashKaraf", type = Exec::class) {
  commandLine = arrayListOf("docker", "exec", "-itd", karafDockerContainerName, "echo", "hello")
}

tasks {
  "build" {
    dependsOn(fatJar)
  }
  "deployKaraf" {
    dependsOn(rmKarafContainer, buildKaraf, runKaraf)
  }
}

task("copyJarToDocker", type = Exec::class) {
  commandLine = arrayListOf("docker", "cp", "build/libs/${project.name}-fat-1.0.0-dev.jar", "${karafDockerContainerName}:/opt/karaf/builds/")
}

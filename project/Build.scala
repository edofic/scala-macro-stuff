import sbt._
import Keys._

object MacroBuild extends Build {
  val baseSettings = Project.defaultSettings ++ Seq(
    scalaVersion := "2.11.0-SNAPSHOT",
    scalaOrganization := "org.scala-lang.macro-paradise",
    resolvers += Resolver.sonatypeRepo("snapshots")
  )

  val macros = Project(
    id = "macros",
    base = file("macros"),
    settings = baseSettings ++ Seq(
      libraryDependencies <+= (scalaOrganization,scalaVersion){ _ % "scala-reflect" % _ }
    )
  )

  val core = Project(
    id = "core",
    base = file("core"),
    settings = baseSettings
  ) dependsOn macros
}

ThisBuild / version := "0.1.0-SNAPSHOT"
libraryDependencies += "org.scalameta" %% "munit" % "1.1.0" % Test
ThisBuild / scalaVersion := "3.9.0"

lazy val root = (project in file("."))
  .settings(
    name := "Integradora1"
  )

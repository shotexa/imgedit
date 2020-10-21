ThisBuild / organization := "com.shotexa"
ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.3"
ThisBuild / name := "imgedit"  
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Xfatal-warnings"
)
Global / onChangedBuildSource := ReloadOnSourceChanges
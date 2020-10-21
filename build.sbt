ThisBuild / organization := "com.shotexa"
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

ThisBuild / libraryDependencies ++= Seq(
  "com.lihaoyi" % "ammonite" % "2.2.0" % "test" cross CrossVersion.full
)

Test / sourceGenerators += Def.task {
  val file = (sourceManaged in Test).value / "amm.scala"
  IO.write(file, """object amm extends App { ammonite.Main.main(args) }""")
  Seq(file)
}.taskValue

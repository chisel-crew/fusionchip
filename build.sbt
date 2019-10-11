val scalaVer      = "2.12.10"
val json4sVersion = "3.6.7"

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

libraryDependencies ++= Seq(
  "edu.berkeley.cs" %% "chisel3"        % "3.2-SNAPSHOT",
  "edu.berkeley.cs" %% "firrtl"         % "1.2-SNAPSHOT",
  "org.json4s"      %% "json4s-jackson" % json4sVersion
)

scalacOptions ++= Seq(
  "-Xsource:2.11",
  "-language:reflectiveCalls"
)

scalacOptions --= Seq(
  "-Xfatal-warnings"
)

lazy val commonSettings = Seq(
  organization := "edu.berkeley.cs",
  version := "1.2",
  scalaVersion := scalaVer,
  crossScalaVersions := Seq(scalaVer, "2.11.12"),
  parallelExecution in Global := false,
  traceLevel := 15,
  maxErrors := 5,
  libraryDependencies ++= Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value),
  addCompilerPlugin("org.scalamacros"          % "paradise"      % "2.1.1" cross CrossVersion.full)
)

lazy val macros = (project in file("macros")).settings(commonSettings)
lazy val fusion = (project in file("."))
  .settings(commonSettings)
  .dependsOn(macros)
//.aggregate(macros) // <-- means the running task on rocketchip is also run by aggregate tasks

addCommandAlias("com", "all compile test:compile")
addCommandAlias("lint", "; compile:scalafix --check ; test:scalafix --check")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
//addCommandAlias("fmt", "; scalafmtSbt; scalafmtAll; test:scalafmtAll")
addCommandAlias("fmt", "; scalafmtSbt")
addCommandAlias("chk", "; scalafmtSbtCheck; scalafmtCheck; test:scalafmtCheck")
addCommandAlias("cov", "; clean; coverage; test; coverageReport")

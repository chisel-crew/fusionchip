resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

lazy val commonSettings = Seq(
// Refine scalac params from tpolecat
  scalacOptions --= Seq(
    "-Xfatal-warnings"
  ),
  scalacOptions ++= Seq(
    "-Xsource:2.11",
    "-language:reflectiveCalls",
    "-language:postfixOps",
    "-unchecked",
    "-deprecation"
  )
)

lazy val commonDeps = libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-jackson" % Version.json4s
)

lazy val chiselDeps = libraryDependencies ++= Seq(
  "edu.berkeley.cs" %% "chisel3" % Version.chisel,
  "edu.berkeley.cs" %% "firrtl"  % Version.firrtl
)

lazy val macros = (project in file("macros"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value)
  )

lazy val core = (project in file("core"))
  .settings(
    name := "core",
    scalaVersion := "2.12.10",
    maxErrors := 3,
    commonSettings,
    commonDeps,
    chiselDeps,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
  )
  .dependsOn(macros)

lazy val blocks = (project in file("sifive-blocks"))
  .dependsOn(core)
  .settings(commonSettings)

lazy val root = (project in file("."))
  .dependsOn(core, blocks)
  .settings(
    name := "fusion",
    commonSettings
  )

// Aliases
addCommandAlias("rel", "reload")
addCommandAlias("com", "all compile test:compile it:compile")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
addCommandAlias("fmt", "all scalafmtSbt scalafmtAll")
//addCommandAlias("fmt", "all scalafmtSbt")

scalafixDependencies in ThisBuild += "com.nequissimus" %% "sort-imports" % "0.5.0"

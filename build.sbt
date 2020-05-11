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
    "-language:postfixOps"
  )
)

lazy val commonDeps = libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-jackson" % Version.json4s
)

lazy val chiselDeps = libraryDependencies ++= Seq(
  "edu.berkeley.cs" %% "chisel3" % Version.chisel,
  "edu.berkeley.cs" %% "firrtl"  % Version.firrtl
)

lazy val zioDeps = libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test"     % Version.zio % "test",
  "dev.zio" %% "zio-test-sbt" % Version.zio % "test"
)

lazy val macros = (project in file("macros"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value)
  )

lazy val root = (project in file("."))
  .settings(
    organization := "Neurodyne",
    name := "fusion",
    version := "0.0.1",
    scalaVersion := "2.12.10",
    maxErrors := 3,
    commonSettings,
    commonDeps,
    zioDeps,
    chiselDeps,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )
  .dependsOn(macros)

// Aliases
addCommandAlias("rel", "reload")
addCommandAlias("com", "all compile test:compile it:compile")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
// addCommandAlias("fmt", "all scalafmtSbt scalafmtAll")
addCommandAlias("fmt", "all scalafmtSbt")

scalafixDependencies in ThisBuild += "com.nequissimus" %% "sort-imports" % "0.5.0"

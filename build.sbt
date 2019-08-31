lazy val versions = new {
  lazy val thisBuild          = "0.1.0-SNAPSHOT"
  lazy val jodaTime           = "2.10.3"
  lazy val xirr               = "0.1"
  lazy val scalaCsv           = "1.3.6"
  lazy val junit              = "0.11"
  lazy val scalaVerify        = "0.1.0"
}

lazy val dependencies = new {
  // Dotty libs
  val yapo                = "io.okama"                      %% "yapo-protobuf-interface"      % versions.thisBuild

  // Scala libs
  val scalaCsv            = "com.github.tototoshi"          %% "scala-csv"                    % versions.scalaCsv
  val grpcNetty           = "io.grpc"                       %  "grpc-netty"                   % scalapb.compiler.Version.grpcJavaVersion
  val scalaPB             = "com.thesamet.scalapb"          %% "scalapb-runtime-grpc"         % scalapb.compiler.Version.scalapbVersion


  // Java libs
  val jodaTime            = "joda-time"                     %  "joda-time"                    % versions.jodaTime
  val xirr                = "org.decampo"                   %  "xirr"                         % versions.xirr

  // Test libs
  val scalaVerify         = "com.eed3si9n.verify"           %% "verify"                       % versions.scalaVerify % Test
}

lazy val commonSettings = Seq(
  description := "Flexible and easy-to-use Scala 3 library for analysis & manipulation with financial & economic data",
  version := versions.thisBuild,

  scalaVersion := "0.17.0-RC1",

  homepage := Some(new URL("https://github.com/okama-io/yapo")),
  startYear := Some(2017),
  licenses := Seq("MIT" -> new URL("https://github.com/okama-io/yapo/blob/master/LICENSE")),
  javacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-source", "1.8",
    "-target", "1.8",
    "-Xlint:unchecked",
    "-Xlint:deprecation"),
  scalacOptions ++= List(
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-deprecation",
    "-language:_",
    "-target:jvm-1.8")
)

lazy val `yapo-root` = project.in(file("."))
  .settings(commonSettings: _*)
  .aggregate(core, interface)

lazy val core = project.in(file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "yapo-core",

    mainClass in (Compile, run) := Some("Runner"),

    libraryDependencies ++= Seq(
      dependencies.scalaCsv,
      dependencies.yapo,
    ).map(_.withDottyCompat(scalaVersion.value)) ++ Seq(
      dependencies.jodaTime,
      dependencies.xirr,
      dependencies.scalaVerify,
    ),

    testFrameworks += new TestFramework("verify.runner.Framework")
  )

lazy val interface = project.in(file("protobuf-interface"))
  .settings(
    scalaVersion := "2.12.9",
    name := "yapo-protobuf-interface",
    organization := "io.okama",

    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    ),

    libraryDependencies ++= Seq(
      dependencies.grpcNetty,
      dependencies.scalaPB
    )
  )

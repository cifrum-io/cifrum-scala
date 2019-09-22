lazy val versions = new {
  lazy val thisBuild          = "0.1.0-SNAPSHOT"
  lazy val jodaTime           = "2.10.3"
  lazy val jodaConvert        = "2.2.1"
  lazy val xirr               = "0.1"
  lazy val scalaCsv           = "1.3.6"
  lazy val scalaVerify        = "0.2.0+11-a21f4546"
  lazy val typesafeConfig     = "1.3.4"
  lazy val guice              = "4.2.2"
}

lazy val dependencies = new {
  // Dotty libs
  val yapoInterface       = "io.okama"                      %  "yapo-protobuf-interface_2.13" % versions.thisBuild

  // Scala libs
  val scalaCsv            = "com.github.tototoshi"          %  "scala-csv_2.13"               % versions.scalaCsv
  val grpcNetty           = "io.grpc"                       %  "grpc-netty"                   % scalapb.compiler.Version.grpcJavaVersion
  val scalaPB             = "com.thesamet.scalapb"          %% "scalapb-runtime-grpc"         % scalapb.compiler.Version.scalapbVersion
  val typesafeConfig      = "com.typesafe"                  %  "config"                       % versions.typesafeConfig

  // Java libs
  val jodaConvert         = "org.joda"                      %  "joda-convert"                 % versions.jodaConvert % Compile
  val jodaTime            = "joda-time"                     %  "joda-time"                    % versions.jodaTime
  val xirr                = "org.decampo"                   %  "xirr"                         % versions.xirr
  val guice               = "com.google.inject"             %  "guice"                        % versions.guice

  // Test libs
  val scalaVerify         = "com.eed3si9n.verify"           %  "verify_0.19"                  % versions.scalaVerify % Test
}

lazy val commonSettings = Seq(
  description := "Flexible and easy-to-use Scala 3 library for analysis & manipulation with financial & economic data",
  version := versions.thisBuild,
  organization := "io.okama",

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
    scalaVersion := "0.19.0-RC1",

    libraryDependencies ++= Seq(
      dependencies.scalaCsv,
      dependencies.yapoInterface,
    ).map(_.withDottyCompat(scalaVersion.value)) ++ Seq(
      dependencies.guice,
      dependencies.jodaTime,
      dependencies.jodaConvert,
      dependencies.typesafeConfig,
      dependencies.xirr,
      dependencies.scalaVerify,
    ),

    testFrameworks += new TestFramework("verify.runner.Framework")
  )

lazy val interface = project.in(file("protobuf-interface"))
  .settings(commonSettings: _*)
  .settings(
    scalaVersion := "2.13.1",
    name := "yapo-protobuf-interface",

    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    ),

    libraryDependencies ++= Seq(
      dependencies.grpcNetty,
      dependencies.scalaPB,
    )
  )

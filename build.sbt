lazy val versions = new {
  lazy val thisBuild          = "0.1.0-SNAPSHOT"
  lazy val dotty              = "0.21.0-RC1"
  lazy val jodaTime           = "2.10.3"
  lazy val jodaConvert        = "2.2.1"
  lazy val xirr               = "1.1"
  lazy val scalaCsv           = "1.3.6"
  lazy val scalaVerify        = "0.2.0"
  lazy val typesafeConfig     = "1.4.0"
  lazy val guice              = "4.2.2"
}

lazy val dependencies = new {
  // Dotty libs
  val yapoInterface       = "io.cifrum"                     %  "yapo-protobuf-interface_2.13" % versions.thisBuild

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
  val scalaVerify         = "com.eed3si9n.verify"           %% "verify"                       % versions.scalaVerify % Test
}

lazy val commonSettings = Seq(
  description := "Flexible and easy-to-use Scala 3 library for analysis & manipulation with financial & economic data",
  version := versions.thisBuild,
  organization := "io.cifrum",

  homepage := Some(new URL("https://github.com/okama-io/yapo-scala")),
  startYear := Some(2017),
  licenses := Seq("MIT" -> new URL("https://github.com/okama-io/yapo-scala/blob/master/LICENSE")),
  javacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-Xlint:unchecked",
    "-Xlint:deprecation"),
  scalacOptions ++= List(
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-deprecation",
    "-language:_")
)

lazy val `yapo-root` = project.in(file("."))
  .settings(commonSettings: _*)
  .aggregate(core, timeSeries, interface)

lazy val core = project.in(file("core"))
  .dependsOn(timeSeries)
  .settings(commonSettings: _*)
  .settings(
    name := "yapo-core",
    scalaVersion := versions.dotty,

    libraryDependencies ++= Seq(
      dependencies.scalaCsv,
      dependencies.yapoInterface,
    ).map(_.withDottyCompat(scalaVersion.value)) ++ Seq(
      dependencies.scalaVerify,
    ),

    mainClass in assembly := Some("io.cifrum.grpc.GrpcRunner"),
    test in assembly := {},
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) =>
        (xs map {_.toLowerCase}) match {
          case "io.netty.versions.properties" :: xs => MergeStrategy.first
          case "index.list" :: xs => MergeStrategy.first
          case "notice.txt" :: xs => MergeStrategy.first
          case "manifest.mf" :: xs => MergeStrategy.discard
          case _ => MergeStrategy.deduplicate
        }
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },

    testFrameworks += new TestFramework("verify.runner.Framework"),
  )

lazy val timeSeries = project.in(file("timeSeries"))
  .settings(commonSettings: _*)
  .settings(
    name := "yapo-time-series",
    scalaVersion := versions.dotty,

    libraryDependencies ++= Seq(
      dependencies.scalaCsv,
    ) ++ Seq(
      dependencies.guice,
      dependencies.jodaTime,
      dependencies.jodaConvert,
      dependencies.typesafeConfig,
      dependencies.xirr,
      dependencies.scalaVerify,
    ),

    testFrameworks += new TestFramework("verify.runner.Framework"),
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

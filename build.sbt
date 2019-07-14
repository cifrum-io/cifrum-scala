lazy val versions = new {
  lazy val akkaHttpVersion    = "10.1.9"
  lazy val akkaVersion        = "2.6.0-M4"
  lazy val jodaTime           = "2.10.3"
  lazy val xirr               = "0.1"
  lazy val scalaCsv           = "1.3.6"
}

lazy val dependencies = new {
  val akkaHttp            = "com.typesafe.akka"             %% "akka-http"                    % versions.akkaHttpVersion
  val akkaStream          = "com.typesafe.akka"             %% "akka-stream"                  % versions.akkaVersion
  val akkaHttpJackson     = "com.typesafe.akka"             %% "akka-http-jackson"            % versions.akkaHttpVersion
  val scalaCsv            = "com.github.tototoshi"          %% "scala-csv"                    % versions.scalaCsv

  val jodaTime            = "joda-time"                     %  "joda-time"                    % versions.jodaTime
  val xirr                = "org.decampo"                   %  "xirr"                         % versions.xirr
}

lazy val commonSettings = Seq(
  name := "yapo",
  description := "Flexible and easy-to-use Scala 3 library for analysis & manipulation with financial & economic data",
  version := "0.1.0-SNAPSHOT",

  scalaVersion := "0.16.0-RC3",

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
  .aggregate(core)

lazy val core = project.in(file("core"))
  .settings(commonSettings: _*)
  .settings(

    libraryDependencies ++= Seq(
      dependencies.akkaHttp,
      dependencies.akkaStream,
      dependencies.akkaHttpJackson,

      dependencies.scalaCsv
    ).map(_.withDottyCompat(scalaVersion.value)) ++ Seq(
      dependencies.jodaTime,
      dependencies.xirr
    )

  )

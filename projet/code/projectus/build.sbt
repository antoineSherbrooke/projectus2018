

lazy val baseSettings = Seq(
  organization := "",
  version := "1.0-SNAPSHOT",
  name := "projectus",
  scalaVersion := "2.11.7"
)

val librariesPlay = Seq(
  javaJdbc,
  jdbc,
  cache,
  evolutions,
  javaWs
)

lazy val root = (project in file(".")).
  enablePlugins(PlayJava, PlayEbean).
  settings(baseSettings: _*).
  settings(
    libraryDependencies ++= librariesPlay,
    libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    libraryDependencies += "org.mockito" % "mockito-core" % "1.10.19",
    libraryDependencies += "commons-io" % "commons-io" % "2.4",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test,
    libraryDependencies += "junit" % "junit" % "4.12"
  )

fork in run := true


scalacOptions ++= Seq("-Xmax-classfile-name","78")
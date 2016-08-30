import org.nlogo.build.NetLogoExtension

enablePlugins(NetLogoExtension)

scalaVersion := "2.11.7"

name := "view2.5d"

netLogoClassManager := "view25d.View25DExtension"

netLogoTarget :=
  NetLogoExtension.directoryTarget(baseDirectory.value)

netLogoZipSources := false

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint", "-Xfatal-warnings",
  "-encoding", "us-ascii")

javacOptions ++= Seq("-g", "-deprecation", "-Xlint:all", "-Xlint:-serial", "-Xlint:-path",
  "-encoding", "us-ascii")

libraryDependencies ++= Seq(
  "org.jogamp.jogl" % "jogl-all" % "2.3.2",
  "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2"
)

netLogoVersion := "6.0.0-M8"
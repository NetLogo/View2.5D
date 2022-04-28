import org.nlogo.build.{ ExtensionDocumentationPlugin, NetLogoExtension }

enablePlugins(NetLogoExtension, ExtensionDocumentationPlugin)

name := "view2.5d"
version := "1.1.9"
isSnapshot := true

javacOptions ++= Seq("-g", "-deprecation", "-Xlint:all", "-Xlint:-serial", "-Xlint:-path", "-encoding", "us-ascii")

scalaVersion := "2.12.12"
scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint", "-Xfatal-warnings", "-encoding", "us-ascii")

netLogoVersion := "6.2.2"
netLogoClassManager := "view25d.View25DExtension"

libraryDependencies ++= Seq(
  "org.jogamp.jogl" % "jogl-all" % "2.3.2"
, "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2"
)

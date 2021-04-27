import org.nlogo.build.{ ExtensionDocumentationPlugin, NetLogoExtension }

enablePlugins(NetLogoExtension, ExtensionDocumentationPlugin)

scalaVersion := "2.12.12"

name := "view2.5d"

version := "1.1.7"

isSnapshot := true

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

resolvers      += "netlogo" at "https://dl.cloudsmith.io/public/netlogo/netlogo/maven/"
netLogoVersion := "6.2.0-d27b502"

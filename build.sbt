//
// Log analyzer and summary builder written in Scala built for JVM projects
//
// @package   LogAnalyzer
// @copyright Apache V2 License (see LICENSE)
// @url       https://github.com/mcross1882/LogAnalyzer
// @author    Matthew Cross <blacklightgfx@gmail.com>
// @version   1.0
//
name := "LogAnalyzer"

version := "1.0"

scalaVersion := "2.10.3"

organization := "mcross1882.loganalyzer"

packageArchetype.java_application

libraryDependencies ++= Seq(
   "org.specs2" %% "specs2" % "2.3.4" % "test",
   "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
   "joda-time" % "joda-time" % "2.0",
   "org.joda" % "joda-convert" % "1.2"
)

mainClass := Some("mcross1882.loganalyzer.Application")

resolvers ++= Seq(
    "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases" at "http://oss.sonatype.org/content/repositories/releases"
)
 
scalacOptions ++= Seq("-unchecked", "-deprecation")

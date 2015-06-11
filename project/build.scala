import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._
import com.earldouglas.xwp.XwpPlugin._
import sbtassembly.AssemblyPlugin._
import sbtassembly.AssemblyKeys._

object StubjsonapiserverBuild extends Build {
  val Organization = "net.cosmo0920"
  val Name = "StubJSONAPIServer"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0.RC1"

  lazy val project = Project (
    "stubjsonapiserver",
    file("."),
    settings = ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.7.v20150116" % "compile;container",
        "org.eclipse.jetty" % "jetty-plus" % "9.2.7.v20150116" % "compile;container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0",
        "org.scalatra" %% "scalatra-json" % "2.3.0",
        "org.json4s"   %% "json4s-jackson" % "3.2.9",
        "com.typesafe" % "config" % "1.2.1",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" %     "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar"))
      ),
      // ignore about.html in jars (needed for sbt-assembly)
      assemblyMergeStrategy in assembly <<= (assemblyMergeStrategy in assembly) { (old) => {
        case "about.html"       => sbtassembly.MergeStrategy.discard
        case "application.conf" => sbtassembly.MergeStrategy.concat
        case x => old(x) }
      },
      // copy web resources to /webapp folder
      resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map {
        (managedBase, base) =>
        val webappBase = base / "src" / "main" / "webapp"
        for {
          (from, to) <- webappBase ** "*" x rebase(webappBase, managedBase / "main" / "webapp")
        } yield {
          Sync.copy(from, to)
          to
        }
      },
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    ) ++ com.earldouglas.xwp.XwpPlugin.jetty(port = 9292)
  )
}

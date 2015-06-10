package net.cosmo0920.app

import org.scalatra._
import scalate.ScalateSupport
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import com.typesafe.config._

object StubConfig {
  implicit class RichConfig(val underlying: Config) {
    def getOptionalString(path: String): Option[String] = if (underlying.hasPath(path)) {
      Some(underlying.getString(path))
    } else {
      None
    }
  }
}

class StubJSONAPIServerServlet extends StubjsonapiserverStack
    with JacksonJsonSupport
{
  import StubConfig._
  val applicationConfig = ConfigFactory.load()
  val confURI = applicationConfig.getOptionalString("default.route")
  val apiURI = confURI.getOrElse("/")

  protected implicit val jsonFormats: Formats = DefaultFormats
  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  get(apiURI) {
    val confResponse = applicationConfig.getOptionalString("default.response")
    confResponse match {
      case Some(r) => r
      case None    => "[]"
    }
  }

}

package net.cosmo0920.app

import org.scalatra.test.specs2._
import com.typesafe.config._

trait LoadConfig {
  val applicationConfig = ConfigFactory.load()
  val appURI = applicationConfig.getString("default.route")
  val contents = applicationConfig.getString("default.response")
}

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class StubJSONAPIServerServletSpec extends ScalatraSpec
    with LoadConfig {
  def is = s2"""
  GET $appURI on StubJSONAPIServerServlet
    returns status 200           $root200
  """

  addServlet(classOf[StubJSONAPIServerServlet], "/*")
  def root200 = get(appURI) {
    status must_== 200
    body must_== contents
  }
}

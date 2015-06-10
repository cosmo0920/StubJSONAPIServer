package net.cosmo0920.app

import org.scalatra.test.specs2._
import com.typesafe.config._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class StubJSONAPIServerServletSpec extends ScalatraSpec { def is =
  "GET / on StubJSONAPIServerServlet"                     ^
    "should return status 200"                  ! root200^
                                                end

  addServlet(classOf[StubJSONAPIServerServlet], "/*")
  val applicationConfig = ConfigFactory.load()
  val appURI = applicationConfig.getString("default.route")

  def root200 = get(appURI) {
    status must_== 200
  }
}

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener

object JettyLauncher {
  def main(args: Array[String]) {
    val port = 9292
    val server = new Server(port)

    val context = new WebAppContext
    context.setContextPath("/")

    context.setResourceBase("src/main/webapp")
    context.setEventListeners(Array(new ScalatraListener))
    server.setHandler(context)

    server.start
    server.join
  }
}

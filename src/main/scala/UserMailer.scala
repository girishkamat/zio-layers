import zio.console.Console
import zio.{Has, Task, ZIO, ZLayer}

object UserMailer {

  trait Service {
    def sendMail(user: User, message: String): Task[Unit]
  }

  class Live(console: Console.Service) extends Service {
    override def sendMail(user: User, message: String): Task[Unit] = {
      console.putStrLn(s"Mailed $user message $message")
    }
  }

  val live: ZLayer[Has[Console.Service], Nothing, Has[Service]] = ZLayer.fromService[Console.Service, Service] { console =>
    println("Creating new instance of UserMailer.Service")
    new Live(console)
  }

  def sendEmail(user: User, message: String): ZIO[Has[Service], Throwable, Unit] =
    ZIO.accessM(_.get.sendMail(user, message))
}
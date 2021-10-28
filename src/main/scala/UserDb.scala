import zio.{Has, Task, ZIO, ZLayer}
import zio.console.Console

object UserDb {

  trait Service {
    def insert(user: User): Task[Unit]
  }

  class Live(console: Console.Service) extends Service {
    def insert(user: User): Task[Unit] = {
      console.putStrLn(s"Inserted user $user")
    }
  }

  val live: ZLayer[Has[Console.Service], Nothing, Has[Service]] = ZLayer.fromService[Console.Service, UserDb.Service] { consoleService =>
    new Live(consoleService)
  }

  def insert(user: User): ZIO[Has[Service], Throwable, Unit] =
    ZIO.accessM(_.get.insert(user))
}

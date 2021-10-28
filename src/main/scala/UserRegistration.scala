import zio.{Has, Task, ZIO, ZLayer}

object UserRegistration {

  trait Service {
    def register(user: User): Task[User]
  }

  class Live(userMailer: UserMailer.Service, userDb: UserDb.Service) extends Service {
    def register(user: User): Task[User] = for {
      _ <- userMailer.sendMail(user, "Some registration message")
      _ <- userDb.insert(user)
    } yield user
  }

  val live: ZLayer[Has[UserMailer.Service] with Has[UserDb.Service], Nothing, Has[Service]] =
    ZLayer.fromServices[UserMailer.Service, UserDb.Service, UserRegistration.Service] { (userMailer, userDb) =>
      new Live(userMailer, userDb)
    }

  def register(user: User): ZIO[Has[Service], Throwable, User] = ZIO.accessM(_.get.register(user))
}

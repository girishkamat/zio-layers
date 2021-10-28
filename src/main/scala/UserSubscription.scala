import zio.{Has, Task, ZIO, ZLayer}

object UserSubscription {

  trait Service {
    def subscribe(user: User): Task[User]
  }

  class Live(userMailer: UserMailer.Service, userDb: UserDb.Service) extends Service {
    def subscribe(user: User): Task[User] = for {
      _ <- userMailer.sendMail(user, "Some subscription message")
      _ <- userDb.insert(user)
    } yield user
  }

  val live: ZLayer[Has[UserMailer.Service] with Has[UserDb.Service], Nothing, Has[Service]] =
    ZLayer.fromServices[UserMailer.Service, UserDb.Service, UserSubscription.Service] { (userMailer, userDb) =>
      new Live(userMailer, userDb)
    }

  def subscribe(user: User): ZIO[Has[Service], Throwable, User] = ZIO.accessM(_.get.subscribe(user))
}

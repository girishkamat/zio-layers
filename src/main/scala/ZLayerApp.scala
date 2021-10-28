import zio.{ExitCode, Has, Ref, UIO, ZIO, ZLayer, ZRef}

object ZLayerApp extends zio.App {

  val userMailerLayer = zio.console.Console.live >>> UserMailer.live

  val userDbLayer = zio.console.Console.live >>> UserDb.live

  val userMailerAndDbLayer = userMailerLayer ++ userDbLayer

  val userSubscriptionLayer: ZLayer[Any, Nothing, Has[UserSubscription.Service]] =
    userMailerAndDbLayer >>> UserSubscription.live

  val userRegistrationLayer: ZLayer[Any, Nothing, Has[UserRegistration.Service]] =
    userMailerAndDbLayer >>> UserRegistration.live

  val cacheIO: UIO[Ref[Option[String]]] = ZRef.make(None)

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    (for {
      _ <- UserSubscription
        .subscribe(User("girish", "g@g.com"))
        .provideLayer(userSubscriptionLayer)
      _ <- UserRegistration.register(User("girish", "g@g.com"))
        .provideLayer(userRegistrationLayer)
    } yield ()).exitCode
  }

}

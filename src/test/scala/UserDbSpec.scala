import zio.test.DefaultRunnableSpec

object UserDbSpec extends DefaultRunnableSpec {
  def spec = suite("HelloWorldSpec")(
    testM("UserDb.sendEmail logs user and message") {
//      val prog = for {
//        test <- TestConsole.make(TestConsole.Data())
//        _ <- UserDb.insert(User("some-user", "some@email.com"))
//        output <- TestConsole.output
//      } yield assert(output)(equalTo(Vector()))
//      val consoleTest: URLayer[Has[Ref[TestConsole.Data]] with Has[Live.Service] with Has[FiberRef[Boolean]], Has[TestConsole.Test]] = TestConsole.Test.toLayer
//      val layer = consoleTest >>> UserDb.live
      ???
    }
  )
}

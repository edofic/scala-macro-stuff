import scala.concurrent.Future

@Futurify
class FuturifyTest {
  def i = 17
  def block = {
    Thread sleep 2000
    "foo"
  }
  def fut = Future successful 19
}

@FuturifyBlocking
class FuturifyBlockingTest {
  def i = 17
  def block = {
    Thread sleep 2000
    "foo"
  }
  def fut = Future successful 19
}

object FuturifyTest {
  //non blocking - uses ExecutionContext.global
  val t1 = new FuturifyTest
  val i1: Future[Int] = t1.i
  val b1: Future[String] = t1.block //returns a future immediately
  val fut1: Future[Int] = t1.fut

  //blocking - all futures returned are already completed
  val t2 = new FuturifyBlockingTest
  val i2: Future[Int] = t2.i
  val b2: Future[String] = t2.block //returns a future after 2 seconds
  val fut2: Future[Int] = t2.fut
}
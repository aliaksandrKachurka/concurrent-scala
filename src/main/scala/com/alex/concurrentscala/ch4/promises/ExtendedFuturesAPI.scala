package com.alex.concurrentscala.ch4.promises

import com.alex.concurrentscala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
object ExtendedFuturesAPI extends App {

  implicit class FutureOps[T](val self: Future[T]) {
    def or(that: Future[T]) = {
      val p = Promise[T]
      self.onComplete(x => p.tryComplete(x))
      that.onComplete(x => p.tryComplete(x))
      p.future
    }
  }

  type Cancellable[T] = (Future[T], Promise[Unit])

  class CancellationException extends RuntimeException

  def cancellable[T](b: Future[Unit] => T): Cancellable[T] = {
    val cancel = Promise[Unit]
    val f = Future {
      val r = b(cancel.future)
      if (!cancel.tryFailure(new Exception))
        throw new CancellationException
      r
    }
    (f, cancel)
  }

  val (value, cancel) = cancellable { cancel =>
    var i = 0
    while(i < 5) {
      if (cancel.isCompleted) throw new CancellationException
      sleep(500)
      log(s"$i working")
      i += 1
    }
    "resulting value"
  }

  sleep(1500)
  cancel.trySuccess()
  log("cancelled")
  sleep(1500)


  val x = Promise[Unit]
  x.tryFailure(new Exception)
  //x.trySuccess()
  x.tryFailure(new Exception)
  x.future.onComplete(y => log(y))
}

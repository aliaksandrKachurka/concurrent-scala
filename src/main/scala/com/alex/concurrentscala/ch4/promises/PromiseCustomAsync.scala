package com.alex.concurrentscala.ch4.promises
import com.alex.concurrentscala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise
import scala.util.control.NonFatal

object PromiseCustomAsync extends App {
  def customFuture[T](body: => T) = {
    val p = Promise[T]
    global.execute(new Runnable {
      override def run() = {
        try {
          p.success(body)
        } catch {
          case NonFatal(e) => p.failure(e)
        }
      }
    })
    p.future
  }
  val f = customFuture("hey guys!")
  f foreach {
    case message => log(message)
  }
  sleep(100)
}

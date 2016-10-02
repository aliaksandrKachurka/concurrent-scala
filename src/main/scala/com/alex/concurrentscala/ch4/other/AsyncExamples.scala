package com.alex.concurrentscala.ch4.other

import com.alex.concurrentscala.{log, sleep}

import scala.async.Async._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, _}


object AsyncExamples extends App {
  def delay(n : Int): Future[Unit] = async {
    blocking {
      sleep(1000)
    }
  }

  def countdown(n: Int)(f: Int => Unit): Future[Unit] = async {
    var i = n
    while (i > 0) {
      f(i)
      log("inside loop")
      await{
        log("inside await")
        delay(1)
      }
      log("after await")
      i -= 1
    }
  }

  countdown(10) {
    n => log(s"T-minus $n seconds")
  } foreach (_ => log(s"This program is over!"))
  sleep(15000)
}

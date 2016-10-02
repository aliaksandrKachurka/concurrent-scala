package com.alex.concurrentscala.ch4.promises

import com.alex.concurrentscala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise

object PromisesCreate extends App {
  val p = Promise[String]
  val q = Promise[String]
  p.future.foreach {
    case x => log(s"p succeeded with $x")
  }
  sleep(1000)
  p success "assigned"
  q failure new Exception("not kept")
  q.future.failed.foreach {
    case t => log(s"q failed with $t")
  }
  sleep(100)
}

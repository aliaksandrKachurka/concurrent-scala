package com.alex.concurrentscala.ch4.futures

import com.alex.concurrentscala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object FuturesNonFatal extends App {
  val f = Future {
    throw new InterruptedException
  }
  val g = Future {
    throw new IllegalArgumentException
  }
  f.failed.foreach {
    case t => log(s"error! $t")
  }
  g.failed.foreach {
    case t => log(s"error! $t")
  }
}

package com.alex.concurrentscala.ch4.other
import com.alex.concurrentscala.{log, sleep}

import scala.util.Random
import scalaz.concurrent._

object ScalazFutures extends App {
  val tombola = Future {
    Random.shuffle((0 until 10000).toVector)
  } unsafeStart

  tombola.unsafePerformAsync {
    numbers => log(s"And a winner is ${numbers.head}")
  }
  tombola.unsafePerformAsync {
    numbers => log(s"...ahem, winner is ${numbers.head}")
  }
  sleep(1000)
}

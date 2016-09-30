package com.alex.concurrentscala.basic

import com.alex.concurrentscala._

object Chapter3 extends App {
  for (i <- 1 to 32) execute {
    sleep(2000)
    log(s"task $i completed")
  }
  sleep(15000)
}

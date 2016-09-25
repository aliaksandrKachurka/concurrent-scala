package com.alex.concurrentscala

import java.util.concurrent.{Executor, ExecutorService, TimeUnit}

import scala.concurrent._

object Chapter3 extends App {
  for (i <- 1 to 32) execute {
    sleep(2000)
    log(s"task $i completed")
  }
  sleep(15000)
}

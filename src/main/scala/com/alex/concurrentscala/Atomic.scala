package com.alex.concurrentscala

import java.util.concurrent.atomic.AtomicLong

import scala.annotation.tailrec

object Atomic extends App {
  private val uid = new AtomicLong(0)

  @tailrec def getUID: Long = {
    val previous = uid.get
    val next = previous + 1
    if (uid.compareAndSet(previous, next)) {
      next
    } else {
      log("FAILED")
      getUID
    }
  }

  for (i <- 1 to 100) {
    execute{ log(s"new uid: $getUID") }
    log(s"new uid: $getUID")
  }
}

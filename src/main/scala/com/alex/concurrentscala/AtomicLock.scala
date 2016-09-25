package com.alex.concurrentscala

import java.util.concurrent.atomic.AtomicBoolean

object AtomicLock extends App {
  val lock = new AtomicBoolean(false)
  def mySynchronized(body: => Unit) = {
    while(lock.compareAndSet(false, true)) {}
    try body finally lock.set(false)
  }
  var count = 0
  for (i <- 1 to 10) execute { mySynchronized{ count +=1 }}
  sleep(2000)
  log(s"count is: $count")
}

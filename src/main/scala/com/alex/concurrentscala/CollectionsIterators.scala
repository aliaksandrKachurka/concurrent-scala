package com.alex.concurrentscala

import java.util.concurrent.LinkedBlockingDeque

object CollectionsIterators extends App {
  val queue = new LinkedBlockingDeque[String]
  for (i <- 1 to 5500) queue.offer(i.toString)
  execute {
    val it = queue.iterator
    while(it.hasNext)
      log(it.next)
  }
  for (i <- 1 to 5500)
    queue.poll()
  sleep(1000)
}

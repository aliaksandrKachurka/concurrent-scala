package com.alex.concurrentscala.basic

import com.alex.concurrentscala._

object SynchronizedGuardedBlocks extends App {
  val lock = new AnyRef
  var message: Option[String] = None

  val greeter = thread {
    lock.synchronized {
      while(message.isEmpty) lock.wait()
      log(message.get)
    }
  }

  lock.synchronized {
    message = Some("hey!")
    lock.notify()
  }
  greeter.join()
}

package com.alex.concurrentscala

object LazyValsCreate extends App {

  object A {
    lazy val x = 1
    this.synchronized {
      val t = thread {
        log(s"Initializing x: $x")
      }
      t.join()
    }
  }
  A
}

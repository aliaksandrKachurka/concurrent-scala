package com.alex.concurrentscala.basic

import com.alex.concurrentscala._

import scala.collection.mutable.ArrayBuffer


object Exercises2 extends App {
  var a = new ArrayBuffer[Int](2)

  def write(x: Int) = a.synchronized {
    a.clear()
    a += x
    log("write " + a(0))
    a.notify()
    a.wait()
  }

  def read() = a.synchronized {
    for (i <- 1 to 10) {
      while (a.isEmpty) {
        a.wait()
      }
      log("read " + a(0))
      a.notify()
      a.wait()
    }
  }

  val x = thread {
    read()
  }

  val y = thread {
    for (i <- 1 to 10) {
      write(i)
    }
  }
  x.join()
  y.join()
}

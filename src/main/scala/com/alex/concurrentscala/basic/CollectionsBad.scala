package com.alex.concurrentscala.basic

import java.util.concurrent.atomic.AtomicReference

import com.alex.concurrentscala._

import scala.annotation.tailrec

class AtomicBuffer[T] {
  val buffer = new AtomicReference[List[T]](Nil)

  @tailrec
  final def +=(x: T): Unit = {
    val xs = buffer.get
    val nxs = x :: xs
    if (!buffer.compareAndSet(xs, nxs)) this += x
  }
}
object CollectionsBad extends App {
  val buffer = new AtomicBuffer[Int]
  def asyncAdd(numbers: Seq[Int]) = execute {
    for (i <- numbers) {
      buffer += i
      log(s"buffer = ${buffer.buffer}")
    }
  }
  asyncAdd(0 until 10)
  asyncAdd(10 until 20)
  sleep(500)
}

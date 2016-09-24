package com.alex.concurrentscala

object Exercises2 extends App {

  def parallel[A, B](a: => A, b: => B): (A, B) = {
    var aResult: Any = 1
    var bResult: Any = 1
    val t1 = thread { aResult = a }
    val t2 = thread { bResult = b }
    t1.join()
    t2.join()
    (aResult.asInstanceOf[A], bResult.asInstanceOf[B])
  }

  val x = parallel(
    {
      3 + 2
    },
    {
      Thread.sleep(5000)
      "hey"
    }
  )

  println(x)
}

package com.alex

package object concurrentscala {

  def log(statement: String) = {
    val name = Thread.currentThread.getName
    println(s"$name: $statement")
  }

  def thread(body: => Unit) = {
    val t = new Thread() {
      override def run(): Unit = body
    }
    t.start()
    t
  }
}

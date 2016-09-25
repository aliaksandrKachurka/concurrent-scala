package com.alex

import java.util.concurrent.TimeUnit

import scala.concurrent.ExecutionContext

package object concurrentscala {

  def log(statement: Any) = {
    val name = Thread.currentThread.getName
    val message = statement.toString
    println(s"$name: $message")
  }

  def thread(body: => Unit) = {
    val t = new Thread() {
      override def run(): Unit = body
    }
    t.start()
    t
  }

  def execute(body: => Unit) = ExecutionContext.global.execute {
    new Runnable {
      override def run(): Unit = body
    }
  }

  def sleep(ms: Int) = Thread.sleep(ms)
}

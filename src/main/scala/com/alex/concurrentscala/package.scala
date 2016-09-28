package com.alex

import java.util.concurrent.LinkedBlockingQueue

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

  private val messages = new LinkedBlockingQueue[String]()
  private val logger = new Thread {
    setDaemon(true)
    override def run(): Unit = while(true) println(messages.take())
  }
  logger.start()
  def logMessage(message: String) = messages.offer(message)
}

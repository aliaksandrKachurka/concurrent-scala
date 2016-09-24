package com.alex.concurrentscala

import scala.collection.mutable


object Worker extends Thread {

}
object SynchronizedPool extends App {
  private val tasks = mutable.Queue[() => Unit]()

  class Worker extends Thread {
    var terminated = false
    def poll(): Option[() => Unit] = tasks.synchronized {
      while (tasks.isEmpty && !terminated) {
        tasks.wait()
      }
      if (!terminated)
        Some(tasks.dequeue())
      else
        None
    }

    override def run() = poll() match {
      case None => log("terminated")
      case Some(task) => task(); run()
    }

    def shutdown() = tasks.synchronized {
      terminated = true
      tasks.notify()
    }
  }

  val worker = new Worker()

  worker.setName("Worker")
  worker.start()

  def asynchronous(body: => Unit) = tasks.synchronized {
    tasks.enqueue(() => body)
    tasks.notify()
  }

  asynchronous { log("Hello") }

  worker.shutdown()
  asynchronous { log("World!") }

  Thread.sleep(5000)
}

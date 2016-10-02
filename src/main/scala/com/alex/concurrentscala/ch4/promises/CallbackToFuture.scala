package com.alex.concurrentscala.ch4.promises
import java.io.File
import java.util.{Timer, TimerTask}

import com.alex.concurrentscala._
import org.apache.commons.io.monitor._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

object CallbackToFuture extends App {
  def fileCreated(directory: String): Future[String] = {
    val fileMonitor = new FileAlterationMonitor(1000)
    val observer = new FileAlterationObserver(directory)
    val p = Promise[String]
    val listener = new FileAlterationListenerAdaptor {
      override def onFileCreate(file: File): Unit = {
        try p.trySuccess(file.getName) finally fileMonitor.stop()
      }
    }
    observer.addListener(listener)
    fileMonitor.addObserver(observer)
    fileMonitor.start()
    p.future
  }

  private	val	timer	=	new	Timer(true)

  def timeout(t: Long): Future[Unit] = {
    val p = Promise[Unit]
    timer.schedule(new TimerTask {
      override def run(): Unit = {
        p.success()
        timer.cancel()
      }
    }, t)
    p future
  }

  import com.alex.concurrentscala.ch4.promises.ExtendedFuturesAPI.FutureOps
  val x = Future {
    sleep(1000)
  } or timeout(1000)
  x onComplete( x => log("completed"))
  sleep(1500)
}

package com.alex.concurrentscala.futures

import com.alex.concurrentscala._

import scala.util.{Failure, Success, Try}

object FuturesTry extends App {
  def handleMessage(t: Try[String]) = t match {
    case Success(msg) => log(msg)
    case Failure(error) => log(s"unexpected failure - $error")
  }

  val threadName: Try[String] = Try(Thread.currentThread.getName)
  val someText = Try("Try objects are syncronous")
  val message: Try[String] = for {
    tn <- threadName
    st <- someText
  } yield s"Message $st was created on t = $tn"
  handleMessage(message)
}

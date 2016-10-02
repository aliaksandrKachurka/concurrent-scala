package com.alex.concurrentscala.ch4.futures

import com.alex.concurrentscala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

object FuturesCreate extends App {
  val buildFile = Future {
    val f = Source.fromFile("build.gradle")
    try f.getLines.mkString("\n") finally f.close()
  }
  log("started reading")
  log(s"status: ${buildFile.isCompleted}")
  sleep(250)
  log(s"status: ${buildFile.isCompleted}")
  log(s"value: ${buildFile.value}")
}

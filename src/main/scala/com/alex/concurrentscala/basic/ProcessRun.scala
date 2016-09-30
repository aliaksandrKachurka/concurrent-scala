package com.alex.concurrentscala.basic

import com.alex.concurrentscala._

import scala.sys.process._

object ProcessRun extends App {
  val command = "java -version".run()
  sleep(1000)
  command.destroy()
}

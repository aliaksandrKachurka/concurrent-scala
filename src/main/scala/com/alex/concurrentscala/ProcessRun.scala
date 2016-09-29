package com.alex.concurrentscala
import scala.sys.process._

object ProcessRun extends App {
  val command = "java -version".run()
  sleep(1000)
  command.destroy()
}

package com.alex.concurrentscala.filemanager

object Tester extends App {
  val fs = new FileSystem(".")
  fs.deleteFile("text.txt")
  Thread.sleep(500)
}

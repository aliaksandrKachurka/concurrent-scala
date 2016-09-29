package com.alex.concurrentscala.filemanager
import com.alex.concurrentscala._

object Tester extends App {
  val fs = new FileSystem(".")
  fs.deleteFile("text.txt")
  log(fs.allFiles)
  Thread.sleep(500)
}

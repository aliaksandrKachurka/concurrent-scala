package com.alex.concurrentscala.ch4.futures

import com.alex.concurrentscala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

object FuturesCallbacks extends App {
  def getUrlSpec(): Future[List[String]] = Future {
    val url = "http://www.w3.org/Addressing/URL/url-spec.txt"
    val f = Source.fromURL(url)
    try f.getLines.toList finally f.close()
  }

  val urlSpec = getUrlSpec()

  def find(lines: List[String], keyword: String) = lines.zipWithIndex
    .filter(x => x._1.contains(keyword))
    .map(x => x.swap)
    .mkString("\n")

  urlSpec foreach {
    case lines => log(find(lines, "telnet"))
  }
  urlSpec foreach {
    case lines => log(find(lines, "a"))
  }
  log("callback registered, continuing with other work")
  sleep(4000)
}

package com.alex.concurrentscala.futures
import com.alex.concurrentscala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

object FuturesFailure extends App {
  def getUrlSpec = Future {
    val invalidUrl = "http://www.w3.org/Addressing/URL/non-existent-url"
    val f = Source.fromURL(invalidUrl).mkString
  }
  getUrlSpec.failed.foreach {
    case t => log(s"exception occured: $t")
  }
  sleep(1000)
}

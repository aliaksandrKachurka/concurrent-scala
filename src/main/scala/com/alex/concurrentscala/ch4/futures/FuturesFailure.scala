package com.alex.concurrentscala.ch4.futures

import com.alex.concurrentscala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success}

object FuturesFailure extends App {
  def getUrlSpec = Future {
    val invalidUrl = "http://www.w3.org/Addressing/URL/non-existent-url"
    val f = Source.fromURL(invalidUrl).mkString
  }
  getUrlSpec.onComplete {
    case Failure(t) => log(s"exception occured: $t")
    case Success(s) => log(s)
  }
  sleep(10000)
}

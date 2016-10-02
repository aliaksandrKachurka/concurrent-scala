package com.alex.concurrentscala.ch4.futures

import java.io._

import com.alex.concurrentscala._
import org.apache.commons.io.FileUtils._

import scala.collection.convert.decorateAsScala._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

object FuturesClumsyCallback extends App {
  def blacklistFile(name: String): Future[List[String]] = Future {
    val lines = Source.fromFile(name).getLines
    lines.filter(line => line.startsWith("#") && !line.isEmpty).toList
  }

  def findFiles(patterns: List[String]) = {
    val root = new File(".")
    for {
      f <- iterateFiles(root, null, true).asScala.toList
      pat <- patterns
      abspat = root.getCanonicalPath + File.separator + pat
      if f.getCanonicalPath.contains(abspat)
    } yield f.getCanonicalPath
  }
  blacklistFile(".gitignore").foreach {
    case lines =>
      val files = findFiles(lines)
      log(s"matches: ${files.mkString("\n")}")
  }

  def blacklisted(name: String): Future[List[String]] = blacklistFile(name).map(patterns => findFiles(patterns))

  val buildFile = Future {
    Source.fromFile("build.gradle").getLines
  }
  val longest = for (ls <- buildFile) yield ls.maxBy(_.length)
  longest foreach {
    case line => log(s"longest line: $line")
  }

  val netiquetteUrl = "http://www.ietf.org/rfc/rfc1855.txt"
  val netiquette = Future { Source.fromURL(netiquetteUrl).mkString }
  val	urlSpecUrl	=	"http://www.w3.org/Addressing/URL/url-spec.txt"
  val	urlSpec	=	Future	{	Source.fromURL(urlSpecUrl).mkString	}
  val answer = netiquette.flatMap { nettext =>
    urlSpec.map {
      urltext => s"Check this out: $nettext. And check out: $urltext"
    }
  }.recover {
    case e: Throwable => log("bad")
  }

  val answer2 = for {
    nettext <- netiquette
    urltext <- urlSpec
  } yield s"Check this out: $nettext. And check out: $urltext"

  answer.foreach {
    case contents => log(contents)
  }
  sleep(5000)
}

package com.alex.concurrentscala.basic

import com.alex.concurrentscala._

import scala.collection.concurrent.TrieMap

object CollectionsConcurrentMapBulk extends App {
  val names = new TrieMap[String, Int]()
  names("Johny") = 0
  names("Jane") = 0
  names("Jack") = 0
  execute {
    for (n <- 0 until 100) names(s"John $n") = n
  }
  sleep(1)
  execute {
    for (n <- names.keys.toSeq.sorted) log(s"name: $n")
  }
  sleep(1000)
}

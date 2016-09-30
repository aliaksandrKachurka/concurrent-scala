package com.alex.concurrentscala.basic

import com.alex.concurrentscala._

class Page(val text: String, var position: Int)

object Volatiles extends App {
  val pages = for (i <- 1 to 5) yield new Page("Na" * (1000 - 20 * i) + "Batman!", -1)

  @volatile var found = false

   for (p <- pages) yield thread {
     var i = 0
     while (i < p.text.length && !found) {
       if (p.text(i) == '!') {
         p.position = i
         found = true
       } else i += 1
     }
   }

  while(!found) {}
  log(s"results: ${pages.map(_.position)}")
}

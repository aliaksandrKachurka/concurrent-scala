package com.alex.concurrentscala
package collections

import scala.util.Random

object ParBasic extends App {
  val numbers = Random.shuffle(Vector.tabulate(50000000)(i => i))
  val seqtime = timed {numbers.max}
  log(s"Sequential time: $seqtime")
  val partime = timed {numbers.par.max}
  log(s"Parallel time: $partime")
}

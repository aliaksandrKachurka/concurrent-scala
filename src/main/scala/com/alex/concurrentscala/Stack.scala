package com.alex.concurrentscala

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

class Stack[A] {
  val nodes = new AtomicReference[List[A]](Nil)

  @tailrec
  final def push(element: A): Unit = {
    val currentState = nodes.get
    val newState = element :: currentState
    if (!nodes.compareAndSet(currentState, newState))
      push(element)
  }

  @tailrec
  final def pop(): A = {
    val currentState = nodes.get
    currentState match {
      case head :: tail =>
        if (!nodes.compareAndSet(currentState, tail))
          pop()
        else
          head
      case Nil =>
        throw new RuntimeException("Empty stack!")
    }
  }
}


object Test extends App {
  val stack = new Stack[Int]

  execute {
    val start = System.currentTimeMillis
    log(s"Start: $start")
    for (i <- 1 until 10) stack.push(i)
    val end = System.currentTimeMillis()
    log(s"Exec: ${end - start}")
  }
  execute {
    val start = System.currentTimeMillis
    log(s"Start: $start")

    for (i <- 1 until 10) stack.push(i)
    val end = System.currentTimeMillis()
    log(s"Exec: ${end - start}")
  }
  execute {
    val start = System.currentTimeMillis
    log(s"Start popping: $start")
    for (i <- 1 until 2) println(stack.pop())
  }
  sleep(1000)
  log(stack.nodes)
}
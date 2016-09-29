package com.alex.concurrentscala.filemanager

import java.util.concurrent.atomic.AtomicReference

class Entry(val isDir: Boolean) {
  val state = new AtomicReference[State]()
  state.set(new Idle())
}

sealed trait State
class Idle extends State
class Creating extends State
class Copying(var n: Int) extends State
class Deleting extends State

object Runner extends App {

  def releaseCopy(e: Entry): Copying = e.state.get match {
    case c: Copying =>
      val newState = if(c.n == 1) new Idle else new Copying(c.n - 1)
      if (e.state.compareAndSet(c, newState)) c else releaseCopy(e)
  }
}
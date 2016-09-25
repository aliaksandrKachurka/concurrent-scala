package com.alex.concurrentscala.filemanager

import java.util.concurrent.atomic.AtomicReference

class Entry(val isDir: Boolean) {
  val state = new AtomicReference[State]()
}

sealed trait State
class Idle extends State
class Creating extends State
class Copying(var n: Int) extends State
class Deleting extends State

object Runner extends App {
  def prepareForDelete(entry: Entry): Boolean = {
    val entryState = entry.state.get()
    entryState match {
      case _: Idle =>
        if (entry.state.compareAndSet(entryState, new Deleting)) true
        else prepareForDelete(entry)
      case _: Creating => logMessage("Being created. Cannot delete"); false
      case _: Copying => logMessage("Being copied. Cannot delete"); false
      case _: Deleting => false
    }
  }

  def releaseCopy(e: Entry): Copying = e.state.get match {
    case c: Copying =>
      val newState = if(c.n == 1) new Idle else new Copying(c.n - 1)
      if (e.state.compareAndSet(c, newState)) c else releaseCopy(e)
  }

  def logMessage(message: String) = {
    println(message)
  }
}
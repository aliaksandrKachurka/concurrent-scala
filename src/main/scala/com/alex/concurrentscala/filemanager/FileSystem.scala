package com.alex.concurrentscala.filemanager

import java.io.File

import com.alex.concurrentscala._
import org.apache.commons.io.FileUtils

import scala.annotation.tailrec
import scala.collection.concurrent.TrieMap
import scala.collection.convert.decorateAsScala._

class FileSystem(val root: String) {
  val rootDir = new File(root)
  val files = new TrieMap[String, Entry]()
  for (f <- FileUtils.iterateFiles(rootDir, null, false).asScala) {
    files.put(f.getName, new Entry(false))
  }

  def allFiles = files.keys

  def deleteFile(fileName: String) = {
    files.get(fileName) match {
      case None =>
        logMessage(s"Path $fileName does not exist!")
      case Some(entry) if entry.isDir =>
        logMessage(s"Path $fileName is a directory!")
      case Some(entry) => execute {
        if (prepareForDelete(entry)) {
          if (FileUtils.deleteQuietly(new File(fileName))) {
            files.remove(fileName)
          }
        }
      }
    }
  }

  private def prepareForDelete(entry: Entry): Boolean = {
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

  @tailrec
  private def acquire(entry: Entry): Boolean = {
    val initialState = entry.state.get
    initialState match {
      case _: Creating | _: Deleting =>
        logMessage("File inaccessible, cannot copy."); false
      case i: Idle =>
        if (entry.state.compareAndSet(initialState, new Copying(1))) true
        else acquire(entry)
      case c: Copying =>
        if (entry.state.compareAndSet(initialState, new Copying(c.n + 1))) true
        else acquire(entry)
    }
  }

  @tailrec
  private def release(entry: Entry): Unit = {
    val initialState = entry.state.get
    initialState match {
      case c: Creating =>
        if (!entry.state.compareAndSet(initialState, new Idle)) release(entry)
      case c: Copying =>
        val newState = if (c.n == 1) new Idle else new Copying(c.n - 1)
        if (!entry.state.compareAndSet(initialState, newState)) release(entry)
    }
  }

  def copyFile(src: String, dest: String) = {
    files.get(src) match {
      case Some(srcEntry) if !srcEntry.isDir => execute {
        if (acquire(srcEntry)) try {
          val destEntry = new Entry(isDir = false)
          destEntry.state.set(new Creating)
          if (files.putIfAbsent(dest, destEntry).isEmpty) {
            try {
              FileUtils.copyFile(new File(src), new File(dest))
            } finally release(destEntry)
          }
        } finally release(srcEntry)
      }
    }
  }
}

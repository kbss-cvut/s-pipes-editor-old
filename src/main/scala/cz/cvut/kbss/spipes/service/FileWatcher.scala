package cz.cvut.kbss.spipes.service

import java.io.FileOutputStream
import java.nio.file.{Paths, StandardWatchEventKinds}

import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 12.07.2017.
  **/
@Service
class FileWatcher {

  def watch(): Unit = {
    val path = Paths.get("/home/yan/Audiobooks")
    val ws = path.getFileSystem().newWatchService()
    val logger = new FileOutputStream("/home/yan/log")

    path.register(ws, StandardWatchEventKinds.ENTRY_CREATE)
    while (true) {
      val wk = ws.poll()
      if (wk != null) {
        logger.write(wk.pollEvents().asScala.mkString("", "\n", "\n").getBytes())
        wk.reset()
      }
    }
  }
}
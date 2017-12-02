package cz.cvut.kbss.spipes.test.service

import java.io.FileNotFoundException
import java.util

import cz.cvut.kbss.spipes.model.spipes.Module
import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.spipes.persistence.dao.{ScriptDao, ViewDao}
import cz.cvut.kbss.spipes.service.ViewService
import org.junit.Assert._
import org.junit.Test
import org.mockito.Mockito.when
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters.{asScalaBufferConverter, setAsJavaSetConverter}
import scala.util.{Failure, Random, Success}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 27.08.2017.
  */
class ViewServiceTest extends BaseServiceTestRunner {

  @Autowired
  private var viewDao: ViewDao = _

  @Autowired
  private var spipesDao: ScriptDao = _

  @Autowired
  private var service: ViewService = _

  private val fileName = ""

  // Fixme: The following tests depend on correct implementation of SpipesService
  // Fixme: due to mysterious Spring processes
  // Fixme: that don't allow for proper SpipesService mock autowiring

  @Test
  def spipesServiceGotFailureFileNotFound: Unit = {
    val e = new FileNotFoundException()
    when(spipesDao.getModules(fileName)).thenReturn(Failure(e))
    assertEquals(Right(None), service.newViewFromSpipes(fileName))
  }

  @Test
  def spipesServiceGotFailureOther: Unit = {
    val e = new IllegalArgumentException()
    when(spipesDao.getModules(fileName)).thenReturn(Failure(e))
    assertEquals(Left(e), service.newViewFromSpipes(fileName))
  }

  @Test
  def spipesServiceGotNullSuccess: Unit = {
    when(spipesDao.getModules(fileName)).thenReturn(Success(null))
    assertEquals(Right(None), service.newViewFromSpipes(fileName))
  }

  @Test
  def spipesServiceGotEmptySuccess: Unit = {
    when(spipesDao.getModules(fileName)).thenReturn(Success(new util.LinkedList[Module]()))
    assertEquals(Right(None), service.newViewFromSpipes(fileName))
  }

  @Test
  def spipesServiceGotNonEmptySuccess: Unit = {
    val l = new util.LinkedList[Module]()
    val size = Random.nextInt(100) + 1
    Seq.fill(size)(0).foreach((_) =>
      l.add(new Module()))
    val v = new View(fileName, l.asScala.map((m) => {
      val n = new Node()
      n.setInParameters(new util.HashSet[String]())
      n.setOutParameters(new util.HashSet[String]())
      n
    }).toSet.asJava, new util.HashSet[Edge]())
    when(spipesDao.getModules(fileName)).thenReturn(Success(l))
    assertEquals(Right(Some(v)), service.newViewFromSpipes(fileName))
  }
}
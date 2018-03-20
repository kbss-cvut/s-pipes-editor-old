package cz.cvut.kbss.spipes.test.service

import java.io.FileNotFoundException
import java.util

import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.service.{OntologyHelper, ScriptService}
import cz.cvut.kbss.spipes.test.config.ScriptTestServiceConfig
import cz.cvut.kbss.spipes.util.ConfigParam.SCRIPTS_LOCATION
import cz.cvut.kbss.spipes.util.Implicits._
import org.apache.jena.rdf.model.ModelFactory
import org.junit.Assert._
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.when
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.{Failure, Random, Success}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 27.08.2017.
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[ScriptTestServiceConfig]))
class ScriptServiceTest {

  @Autowired
  private var dao: ScriptDao = _

  @Autowired
  private var helper: OntologyHelper = _

  @Autowired
  private var service: ScriptService = _

  private val fileName = ""

  @Test
  def moduleTypesGotFailureFileNotFound: Unit = {
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Failure(new FileNotFoundException()))
    assertEquals(Right(None), service.getModuleTypes(fileName))
  }

  @Test
  def moduleTypesGotFailureOther: Unit = {
    val e = new IllegalArgumentException()
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Failure(e))
    assertEquals(Left(e), service.getModuleTypes(fileName))
  }

  @Test
  def moduleTypesGotNullSuccess: Unit = {
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(null))
    when(helper.getImports(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(None), service.getModuleTypes(fileName))
  }

  @Test
  def moduleTypesGotEmptySuccess: Unit = {
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(new util.LinkedList[ModuleType]()))
    when(helper.getImports(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(None), service.getModuleTypes(fileName))
  }

  @Test
  def moduleTypesGotNonEmptySuccess: Unit = {
    val l = new util.LinkedList[ModuleType]()
    val size = Random.nextInt(100) + 1
    Seq.fill(size)(0).foreach((_) =>
      l.add(new ModuleType()))
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(l))
    when(helper.getImports(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(Some(l.asScala)), service.getModuleTypes(fileName))
  }

  @Test
  def modulesGotFailureFileNotFound: Unit = {
    when(dao.getModules(false)).thenReturn((_: String) => Failure(new FileNotFoundException()))
    when(helper.getImports(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(None), service.getModules(fileName))
  }

  @Test
  def modulesGotFailureOther: Unit = {
    val e = new IllegalArgumentException()
    when(dao.getModules(false)).thenReturn((_: String) => Failure(e))
    when(helper.getImports(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Left(e), service.getModules(fileName))
  }

  @Test
  def modulesGotEmptySuccess: Unit = {
    when(dao.getModules(false)).thenReturn((_: String) => Success(new util.LinkedList[Module]()))
    when(helper.getImports(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(None), service.getModules(fileName))
  }

  @Test
  def modulesGotNonEmptySuccess: Unit = {
    val l = new util.LinkedList[Module]()
    val size = Random.nextInt(100) + 1
    Seq.fill(size)(0).foreach((_) =>
      l.add(new Module()))
    when(dao.getModules(false)).thenReturn((_: String) => Success(l))
    when(helper.getImports(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(Some(l.asScala)), service.getModules(fileName))
  }

  @Test
  def modelLoadsOnlyOwnStatements: Unit = {
    val script = getClass().getClassLoader().getResource("scripts/sample-script.ttl").getFile()
    val model = ModelFactory.createDefaultModel().read(script)
    val statements = model.listStatements().toList()
    assertTrue(statements.size() == 9)
  }
}
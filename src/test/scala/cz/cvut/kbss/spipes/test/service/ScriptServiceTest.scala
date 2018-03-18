package cz.cvut.kbss.spipes.test.service

import java.io.{File, FileNotFoundException}
import java.util

import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.service.ScriptService
import cz.cvut.kbss.spipes.test.config.TestServiceConfig
import org.apache.jena.rdf.model.ModelFactory
import org.junit.Assert._
import org.junit.runner.RunWith
import org.junit.{Ignore, Test}
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
@ContextConfiguration(classes = Array(classOf[TestServiceConfig]))
class ScriptServiceTest {

  @Autowired
  private var dao: ScriptDao = _

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

  // FIXME Way to mock getImports?
  @Ignore
  @Test
  def moduleTypesGotNullSuccess: Unit = {
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(null))
    when(dao.getModuleTypes(true)).thenReturn((_: String) => Failure(new Exception()))
    assertEquals(Right(None), service.getModuleTypes(fileName))
  }

  @Ignore
  @Test
  def moduleTypesGotEmptySuccess: Unit = {
    when(dao.getModuleTypes(false)(fileName)).thenReturn(Success(new util.LinkedList[ModuleType]()))
    assertEquals(Right(None), service.getModuleTypes(fileName))
  }

  @Ignore
  @Test
  def moduleTypesGotNonEmptySuccess: Unit = {
    val l = new util.LinkedList[ModuleType]()
    val size = Random.nextInt(100) + 1
    Seq.fill(size)(0).foreach((_) =>
      l.add(new ModuleType()))
    when(dao.getModuleTypes(false)(fileName)).thenReturn(Success(l))
    assertEquals(Right(Some(l.asScala)), service.getModuleTypes(fileName))
  }

  @Ignore
  @Test
  def modulesGotFailureFileNotFound: Unit = {
    when(dao.getModules(false)(fileName)).thenReturn(Failure(new FileNotFoundException()))
    assertEquals(Right(None), service.getModules(fileName))
  }

  @Ignore
  @Test
  def modulesGotFailureOther: Unit = {
    val e = new IllegalArgumentException()
    when(dao.getModules(false)(fileName)).thenReturn(Failure(e))
    assertEquals(Left(e), service.getModules(fileName))
  }

  @Ignore
  @Test
  def modulesGotEmptySuccess: Unit = {
    when(dao.getModules(false)(fileName)).thenReturn(Success(new util.LinkedList[Module]()))
    assertEquals(Right(None), service.getModules(fileName))
  }

  @Ignore
  @Test
  def modulesGotNonEmptySuccess: Unit = {
    val l = new util.LinkedList[Module]()
    val size = Random.nextInt(100) + 1
    Seq.fill(size)(0).foreach((_) =>
      l.add(new Module()))
    when(dao.getModules(false)(fileName)).thenReturn(Success(l))
    assertEquals(Right(Some(l.asScala)), service.getModules(fileName))
  }

  @Test
  def modelLoadsOnlyOwnStatements: Unit = {
    val script = getClass().getClassLoader().getResource("scripts/sample-script.ttl").getFile()
    val model = ModelFactory.createDefaultModel().read(script)
    val statements = model.listStatements().toList()
    assertTrue(statements.size() == 9)
  }

  @Test
  def getOntologyUriReturnsCorrectURI: Unit = {
    val script = getClass().getClassLoader().getResource("scripts/sample-script.ttl").getFile()
    assertEquals(Some("http://www.semanticweb.org/sample-script"), service.getOntologyUri(new File(script)))
  }

  @Test
  def collectOntologyUrisWorksAsIntended: Unit = {
    val script = getClass().getClassLoader().getResource("scripts/sample-script.ttl").getFile()
    val script1 = getClass().getClassLoader().getResource("scripts/sample-script1.ttl").getFile()
    assertEquals(
      Map(
        "http://www.semanticweb.org/sample-script" -> "sample-script.ttl",
        "http://www.semanticweb.org/sample-script1" -> "sample-script1.ttl"
      ),
      service.collectOntologyUris(Set(new File(script), new File(script1))).map(kv => kv._1 -> kv._2.getName())
    )
  }

  @Test
  def getImportsCollectsAllTheImports: Unit = {
    val rootPath = getClass().getClassLoader().getResource("scripts").getFile() + "/"
    val script = "sample-script1.ttl"
    val imports = service.getImports(rootPath)(script)
    assertEquals(Success(Seq("http://spinrdf.org/spl")), imports)
  }
}
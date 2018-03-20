package cz.cvut.kbss.spipes.test.service

import java.io.File

import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.util.ConfigParam.SCRIPTS_LOCATION
import cz.cvut.kbss.spipes.util.Implicits._
import org.junit.Assert._
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.when
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import scala.collection.JavaConverters._
import scala.util.Success

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.03.2018.
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
class ScriptServiceImportsTest extends ScriptServiceTest {

  @Test
  def noModuleTypesFound: Unit = {
    val scripts = getClass().getClassLoader().getResource("scripts").getFile() + "/sample-script1.ttl"
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(Seq[ModuleType]().asJava))
    when(dao.getModuleTypes(true)).thenReturn((_: String) => Success(Seq[ModuleType]().asJava))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Success(Seq("http://imported.ttl")))
    when(helper.getFile("http://imported.ttl")).thenReturn(Some(new File(scripts)))
    val res = service.getModuleTypes(fileName)
    assertTrue(res.isRight)
    assertTrue(res.getOrElse(None).isEmpty)
  }

  @Test
  def moduleTypesFoundLocally: Unit = {
    val scripts = getClass().getClassLoader().getResource("scripts").getFile() + "/sample-script1.ttl"
    val label = "Label"
    val comment = "Comment"
    val m = new ModuleType()
    m.setLabel(label)
    m.setComment(comment)
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(Seq(m).asJava))
    when(dao.getModuleTypes(true)).thenReturn((_: String) => Success(Seq[ModuleType]().asJava))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Success(Seq("http://imported.ttl")))
    when(helper.getFile("http://imported.ttl")).thenReturn(Some(new File(scripts)))
    val res = service.getModuleTypes(fileName)
    assertTrue(res.isRight)
    assertTrue(res.getOrElse(None).nonEmpty)
    assertEquals(1, res.getOrElse(None).getOrElse(Seq()).size)
    val resM = res.getOrElse(None).get.toSeq.head
    assertEquals(label, resM.getLabel())
    assertEquals(comment, resM.getComment())
  }

  @Test
  def moduleTypesFoundInTheImports: Unit = {
    val scripts = getClass().getClassLoader().getResource("scripts").getFile() + "/sample-script1.ttl"
    val label = "Label"
    val comment = "Comment"
    val m = new ModuleType()
    m.setLabel(label)
    m.setComment(comment)
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(Seq[ModuleType]().asJava))
    when(dao.getModuleTypes(true)).thenReturn((_: String) => Success(Seq(m).asJava))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Success(Seq("http://imported.ttl")))
    when(helper.getFile("http://imported.ttl")).thenReturn(Some(new File(scripts)))
    val res = service.getModuleTypes(fileName)
    assertTrue(res.isRight)
    assertTrue(res.getOrElse(None).nonEmpty)
    assertTrue(res.getOrElse(None).getOrElse(Seq()).nonEmpty)
    val resM = res.getOrElse(None).get.toSeq.head
    assertEquals(label, resM.getLabel())
    assertEquals(comment, resM.getComment())
  }

  @Test
  def moduleTypesFoundBothLocallyAndInTheImports: Unit = {
    val scripts = getClass().getClassLoader().getResource("scripts").getFile() + "/sample-script1.ttl"
    val label1 = "Label1"
    val comment1 = "Comment1"
    val m1 = new ModuleType()
    m1.setLabel(label1)
    m1.setComment(comment1)
    val label2 = "Label2"
    val comment2 = "Comment2"
    val m2 = new ModuleType()
    m2.setLabel(label2)
    m2.setComment(comment2)
    when(dao.getModuleTypes(false)).thenReturn((_: String) => Success(Seq(m1).asJava))
    when(dao.getModuleTypes(true)).thenReturn((_: String) => Success(Seq(m2).asJava))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Success(Seq("http://imported.ttl")))
    when(helper.getFile("http://imported.ttl")).thenReturn(Some(new File(scripts)))
    val res = service.getModuleTypes(fileName)
    assertTrue(res.isRight)
    assertTrue(res.getOrElse(None).nonEmpty)
    assertEquals(2, res.getOrElse(None).getOrElse(Seq()).size)
    val resMs = res.getOrElse(None).get.toSeq
    assertEquals(label1, resMs.head.getLabel())
    assertEquals(comment1, resMs.head.getComment())
    assertEquals(label2, resMs.last.getLabel())
    assertEquals(comment2, resMs.last.getComment())
  }

  @Test
  def noModulesFound: Unit = {
    val scripts = getClass().getClassLoader().getResource("scripts").getFile() + "/sample-script1.ttl"
    when(dao.getModules(false)).thenReturn((_: String) => Success(Seq[Module]().asJava))
    when(dao.getModules(true)).thenReturn((_: String) => Success(Seq[Module]().asJava))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Success(Seq("http://imported.ttl")))
    when(helper.getFile("http://imported.ttl")).thenReturn(Some(new File(scripts)))
    val res = service.getModules(fileName)
    assertTrue(res.isRight)
    assertTrue(res.getOrElse(None).isEmpty)
  }

  @Test
  def modulesFoundLocally: Unit = {
    val scripts = getClass().getClassLoader().getResource("scripts").getFile() + "/sample-script1.ttl"
    val label = "Label"
    val types = Set("type1").asJava
    val m = new Module()
    m.setLabel(label)
      m.setTypes(types)
    when(dao.getModules(false)).thenReturn((_: String) => Success(Seq(m).asJava))
    when(dao.getModules(true)).thenReturn((_: String) => Success(Seq[Module]().asJava))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Success(Seq("http://imported.ttl")))
    when(helper.getFile("http://imported.ttl")).thenReturn(Some(new File(scripts)))
    val res = service.getModules(fileName)
    assertTrue(res.isRight)
    assertTrue(res.getOrElse(None).nonEmpty)
    assertEquals(1, res.getOrElse(None).getOrElse(Seq()).size)
    val resM = res.getOrElse(None).get.toSeq.head
    assertEquals(label, resM.getLabel())
    assertEquals(types, resM.getTypes())
  }

  @Test
  def modulesFoundInTheImports: Unit = {
    val scripts = getClass().getClassLoader().getResource("scripts").getFile() + "/sample-script1.ttl"
    val label = "Label"
    val types = Set("type1").asJava
    val m = new Module()
    m.setLabel(label)
    m.setTypes(types)
    when(dao.getModules(false)).thenReturn((_: String) => Success(Seq[Module]().asJava))
    when(dao.getModules(true)).thenReturn((_: String) => Success(Seq(m).asJava))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Success(Seq("http://imported.ttl")))
    when(helper.getFile("http://imported.ttl")).thenReturn(Some(new File(scripts)))
    val res = service.getModules(fileName)
    assertTrue(res.isRight)
    assertTrue(res.getOrElse(None).nonEmpty)
    assertEquals(1, res.getOrElse(None).getOrElse(Seq()).size)
    val resM = res.getOrElse(None).get.toSeq.head
    assertEquals(label, resM.getLabel())
    assertEquals(types, resM.getTypes())
  }

  @Test
  def modulesFoundBothLocallyAndInTheImports: Unit = {
    val scripts = getClass().getClassLoader().getResource("scripts").getFile() + "/sample-script1.ttl"
    val label1 = "Label1"
    val types1 = Set("type1").asJava
    val m1 = new Module()
    m1.setLabel(label1)
    m1.setTypes(types1)
    val label2 = "Label2"
    val types2 = Set("type2").asJava
    val m2 = new Module()
    m2.setLabel(label2)
    m2.setTypes(types2)
    when(dao.getModules(false)).thenReturn((_: String) => Success(Seq(m1).asJava))
    when(dao.getModules(true)).thenReturn((_: String) => Success(Seq(m2).asJava))
    when(helper.getURIOfImportedOntologies(service.getProperty(SCRIPTS_LOCATION))).thenReturn((_: String) => Success(Seq("http://imported.ttl")))
    when(helper.getFile("http://imported.ttl")).thenReturn(Some(new File(scripts)))
    val res = service.getModules(fileName)
    assertTrue(res.isRight)
    assertTrue(res.getOrElse(None).nonEmpty)
    assertEquals(2, res.getOrElse(None).getOrElse(Seq()).size)
    val resMs = res.getOrElse(None).get.toSeq
    assertEquals(label1, resMs.head.getLabel())
    assertEquals(types1, resMs.head.getTypes())
    assertEquals(label2, resMs.last.getLabel())
    assertEquals(types2, resMs.last.getTypes())
  }
}

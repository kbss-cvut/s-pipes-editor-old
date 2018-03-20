package cz.cvut.kbss.spipes.test.service

import java.io.File

import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.service.OntologyHelper
import cz.cvut.kbss.spipes.test.config.TestServiceConfig
import org.junit.Assert.{assertEquals, _}
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import scala.util.Success

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.03.2018.
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[TestServiceConfig]))
class OntologyHelperTest {

  @Autowired
  private var scriptDao: ScriptDao = _

  @Autowired
  private var helper: OntologyHelper = _

  @Test
  def getOntologyUriReturnsCorrectURI: Unit = {
    val script = getClass().getClassLoader().getResource("scripts/sample-script.ttl").getFile()
    assertEquals(Some("http://www.semanticweb.org/sample-script"), helper.getOntologyUri(new File(script)))
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
      helper.collectOntologyUris(Set(new File(script), new File(script1))).map(kv => kv._1 -> kv._2.getName())
    )
  }

  @Test
  def getImportsCollectsAllTheImports: Unit = {
    val rootPath = getClass().getClassLoader().getResource("scripts").getFile() + "/"
    val script = "sample-script1.ttl"
    val imports = helper.getURIOfImportedOntologies(rootPath)(script)
    assertEquals(Success(Seq("http://spinrdf.org/spl")), imports)
  }

  @Test
  def getFileWorksAsIntendedWithNone: Unit = {
    when(scriptDao.getScripts).thenReturn(None)
    assertTrue(helper.getFile("ontologyUri").isEmpty)
  }

  @Test
  def getFileWorksAsIntendedWithNoScripts: Unit = {
    when(scriptDao.getScripts).thenReturn(Some(Set[File]()))
    assertTrue(helper.getFile("ontologyUri").isEmpty)
  }

  @Test
  def getFileFindsCorrect: Unit = {
    val script = getClass().getClassLoader().getResource("scripts/sample-script.ttl").getFile()
    when(scriptDao.getScripts).thenReturn(Some(Set(new File(script))))
    val res = helper.getFile("http://www.semanticweb.org/sample-script")
    assertTrue(res.nonEmpty)
    assertEquals(Some(new File(script)), res)
  }

  @Test
  def getFileDoesNotFindIncorrect: Unit = {
    val script = getClass().getClassLoader().getResource("scripts/sample-script.ttl").getFile()
    when(scriptDao.getScripts).thenReturn(Some(Set(new File(script))))
    val res = helper.getFile("http://not.there")
    assertTrue(res.isEmpty)
  }
}

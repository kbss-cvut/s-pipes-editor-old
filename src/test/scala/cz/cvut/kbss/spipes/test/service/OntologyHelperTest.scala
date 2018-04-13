package cz.cvut.kbss.spipes.test.service

import java.io.{File, FileNotFoundException}
import java.util.UUID

import cz.cvut.kbss.spipes.persistence.dao.ScriptDao
import cz.cvut.kbss.spipes.service.OntologyHelper
import cz.cvut.kbss.spipes.test.config.TestServiceConfig
import org.apache.jena.ontology.{OntDocumentManager, OntModelSpec}
import org.apache.jena.rdf.model.{ModelFactory, ResourceFactory}
import org.junit.Assert.{assertEquals, _}
import org.junit.runner.RunWith
import org.junit.{Ignore, Test}
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

  @Ignore
  @Test
  def createModelFileNotFound: Unit = {
    val res = helper.createUnionModel(new File(UUID.randomUUID().toString()))
    assertTrue(res.isFailure)
    assertEquals(classOf[FileNotFoundException].getCanonicalName(),
      res.recoverWith { case e => Success(e) }.get.getClass().getCanonicalName())
  }

  @Ignore
  @Test
  def createModelCreatesModel: Unit = {
    val script = getClass().getClassLoader().getResource("scripts/sample-script.ttl").getFile()
    val res = helper.createUnionModel(new File(script))
    assertTrue(res.isSuccess)
    assertEquals(9, res.get.size())
  }

  @Test
  def createFile: Unit = {
    val script = getClass().getClassLoader().getResource("scripts/").getFile()
    val f = new File(script + "test")
    f.createNewFile()
    println(f.getAbsolutePath())
  }

  @Test
  def jenaTest: Unit = {
    val m1 = ModelFactory.createDefaultModel()
    m1.add(ResourceFactory.createResource("http://uri.org/s1"), ResourceFactory.createProperty("http://uri.org/p"), ResourceFactory.createResource("http://uri.org/o1"))
    val m2 = ModelFactory.createDefaultModel()
    m2.add(ResourceFactory.createResource("http://uri.org/s2"), ResourceFactory.createProperty("http://uri.org/p"), ResourceFactory.createResource("http://uri.org/o2"))
    val union = m1.union(m2)
    union.listStatements()
  }

  @Ignore
  @Test
  def importsTest: Unit = {
    val docManager = OntDocumentManager.getInstance()
    val o = "http://ontologies.org/importing"
    docManager.addAltEntry("http://ontologies.org/importing", "/home/yan/git/kbss/s-pipes-editor/src/test/resources/scripts/sample/import-test/importing.ttl")
    docManager.addAltEntry("http://ontologies.org/imported", "/home/yan/git/kbss/s-pipes-editor/src/test/resources/scripts/sample/import-test/imported.ttl")
    val model = docManager.getOntology(o, OntModelSpec.OWL_MEM)
    model.loadImports()
    assertEquals(4, model.listStatements().toList.size())
  }
}

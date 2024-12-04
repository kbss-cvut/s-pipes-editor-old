package cz.cvut.kbss.spipes.test.persistence.dao

import cz.cvut.kbss.spipes.config.PersistenceConfig
import cz.cvut.kbss.spipes.persistence.dao.{ScriptDao, ViewDao}
import cz.cvut.kbss.spipes.test.persistence.dao.ScriptDaoTest.{defaultModel, getClass}
import org.apache.jena.rdf.model.ModelFactory
import org.junit.Assert.{assertEquals, assertTrue, fail}
import org.junit.{Ignore, Test}
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import scala.collection.JavaConverters._

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.03.2018.
  */
//TODO create testing ContextConfiguration
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[PersistenceConfig]))
class ScriptDaoTest {

  @Autowired
  private var scriptDao: ScriptDao = _

  @Test
  def initCorrectlyInitTest: Unit = {
    import scala.collection.JavaConverters._
    assertEquals(scriptDao.emf.getProperties.asScala, Map(
      "cz.cvut.jopa.scanPackage" -> "cz.cvut.kbss.spipes.model",
      "javax.persistence.provider" -> "cz.cvut.kbss.jopa.model.JOPAPersistenceProvider",
      "cz.cvut.jopa.dataSource.class" -> "cz.cvut.kbss.ontodriver.jena.JenaDataSource",
      "cz.cvut.jopa.lang" -> "en",
      "in-memory" -> "true",
      "cz.cvut.jopa.ontology.logicalUri" -> "http://temporary",
      "cz.cvut.jopa.ontology.physicalURI" -> "local://temporary"
      )
    )
  }

  //TODO do not know how exactly is working especily the em query
  @Test
  def getModulesTest: Unit = {
    val modules = scriptDao.getModules(defaultModel)

    val res = modules.get.asScala.toList
    res.foreach(x => {
        val moduleToString = s"id: ${x.getId}, uri: ${x.getUri}, label: ${x.getLabel}"
        println(moduleToString)
    })

    assertEquals(res.size, 18)
  }

  @Test
  def getModuleTypesTest: Unit = {
    val getModuleTypes = scriptDao.getModuleTypes(defaultModel)

    val res = getModuleTypes.get.asScala.toList
    println(s"sizu: ${res.size}")
    getModuleTypes.get.asScala.foreach(x => {
      val comments = if(x.getComment != null) x.getComment.asScala.toList else List.empty
      val moduleToString = s"id: ${x.getId}, uri: ${x.getUri}, icon: ${x.getIcon}, label: ${x.getLabel}, comments: ${comments}"
      println(moduleToString)
    })

    assertEquals(getModuleTypes.get.size(), 77)
  }

  @Test
  def getFunctionStatementsTest: Unit = {
    val getModuleTypes = scriptDao.getFunctionStatements(defaultModel)

    val res = getModuleTypes.get.mapWith(_.getSubject.getURI).asScala.toList

    assertEquals(
      res,
      List(
        "http://vfn.cz/ontologies/fss-form-generation-0.123/clone-fss-form",
        "http://topbraid.org/sparqlmotion#Functions",
        "http://vfn.cz/ontologies/fss-form-generation-0.123/generate-fss-form"
      )
    )
  }

  @Test
  def getScriptsTest: Unit = {
    //TODO problem with the testing because hardcoded path
    assertEquals(scriptDao.getScripts.head.map(_.getName), Set("fss-form-static.ttl", "fss-form-generation.sms.ttl", "vfn-form-modules.ttl"))
  }

  @Test
  def getScriptsWithImportsTest: Unit = {
    val scriptWithImports = scriptDao.getScriptsWithImports.head

    assertEquals(scriptWithImports.head._1.getName, "vfn-example")
    assertEquals(scriptWithImports.head._2.map(_.getName), Set("fss-form-static.ttl", "fss-form-generation.sms.ttl", "vfn-form-modules.ttl"))
  }

  @Test
  def getScriptsTreeTest: Unit = {
    val tree = scriptDao.getScriptsTree.map(_.getName).mkString(",")

    assertEquals(tree, "vfn-example")
  }

}
object ScriptDaoTest {

  private val source = getClass.getResource("/scripts/sample/simple-import/script.ttl").getPath
  val defaultModel = ModelFactory.createDefaultModel().read(source)

}


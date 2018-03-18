package cz.cvut.kbss.spipes.test.service

import cz.cvut.kbss.spipes.model.spipes.Module
import cz.cvut.kbss.spipes.model.view.{Edge, Node}
import cz.cvut.kbss.spipes.persistence.dao.{ScriptDao, ViewDao}
import cz.cvut.kbss.spipes.service.{ScriptService, ViewService}
import org.junit.Assert.{assertEquals, _}
import org.junit.Test
import org.mockito.Mockito.when
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters.setAsJavaSetConverter

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

  @Autowired
  private var scriptService: ScriptService = _

  private val fileName = ""

  @Test
  def scriptServiceReturnsRightNone: Unit = {
    when(scriptService.getModules(fileName)).thenReturn(Right(None))
    assertEquals(Right(None), service.newViewFromSpipes(fileName))
  }

  @Test
  def scriptServiceReturnsLeft: Unit = {
    val e = new IllegalArgumentException()
    when(scriptService.getModules(fileName)).thenReturn(Left(e))
    assertEquals(Left(e), service.newViewFromSpipes(fileName))
  }

  @Test
  def spipesServiceGotEmptySuccess: Unit = {
    when(scriptService.getModules(fileName))
      .thenReturn(Right(Some(List[Module]())))
    val res = service.newViewFromSpipes(fileName)
    assertTrue(res.isRight)
    assertTrue(res.getOrElse(None).nonEmpty)
    val v = res.getOrElse(None).get
    assertEquals(Set[Edge]().asJava, v.getEdges())
    assertEquals(Set[Node]().asJava, v.getNodes())
    assertEquals(fileName, v.getLabel())
  }

  @Test
  def spipesServiceGotNonEmptySuccess: Unit = {
    val m = new Module()
    m.setLabel("Label")
    m.setTypes(Set("Type").asJava)
    when(scriptService.getModules(fileName))
      .thenReturn(Right(Some(List[Module](m))))
    val res = service.newViewFromSpipes(fileName)
    assertTrue(res.isRight)
    assertTrue(res.getOrElse(None).nonEmpty)
    val v = res.getOrElse(None).get
    assertEquals(1, v.getNodes().size())
    v.getNodes().forEach((n) => {
      assertEquals(m.getLabel(), n.getLabel())
      assertEquals(m.getTypes(), n.getModuleTypes())
    })
    assertEquals(0, v.getEdges().size())
  }
}
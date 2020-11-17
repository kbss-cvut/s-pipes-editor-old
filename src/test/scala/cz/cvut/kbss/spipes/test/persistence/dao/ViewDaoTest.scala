package cz.cvut.kbss.spipes.test.persistence.dao

import java.net.URI

import cz.cvut.kbss.spipes.config.PersistenceConfig
import cz.cvut.kbss.spipes.model.view.{Edge, Node, View}
import cz.cvut.kbss.spipes.persistence.dao.ViewDao
import cz.cvut.kbss.spipes.test.config.ViewTestServiceConfig
import org.junit.Assert.assertEquals
import org.junit.{Ignore, Test}
import org.junit.runner.RunWith
import org.mockito.Mockito.when
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.03.2018.
 *
 * I cant setup this module - ask someone for its purpose. Sadly I cant find any documentation.
  */
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(classes = Array(classOf[PersistenceConfig]))
class ViewDaoTest {

  @Autowired
  private var viewDao: ViewDao = _

  @Test
  @Ignore
  def getViewEdgesTest: Unit = {
    val edgeNode = viewDao.getViewEdges(new URI("http://purl.org/dc/elements/1.1/date"))
    //edge should be something
  }

  @Test
  @Ignore
  def getViewNodesTest: Unit = {
    val edgeNode = viewDao.getViewNodes(new URI("http://purl.org/dc/elements/1.1/date"))
    //view should be something
  }

  @Test
  @Ignore
  def updateViewTest: Unit = {
    val edgeNode = viewDao.updateView(new URI("http://purl.org/dc/elements/1.1/date"), new View("label", new java.util.HashSet[Node](), new java.util.HashSet[Edge]()))
    //update should update something but it is not working correctly
  }

  @Test
  @Ignore
  def fetchAllTest: Unit = {
    //Not working, failing on abstract DAO
    viewDao.findAll.foreach(x => {
      println(x)
      x.forEach(x => println(s"view: $x"))
    })
  }

}

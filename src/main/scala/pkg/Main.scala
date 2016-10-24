package pkg

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.stereotype.Component

import scala.beans.BeanProperty

/**
  * Created by yan on 21.10.16.
  */

@Component
@Scope("prototype")
class Main {

  @Autowired
  @BeanProperty
  var bean: Hello = _

  def p = println(bean message)
}

object Main {

  def main(args: Array[String]) {
    val context = new ClassPathXmlApplicationContext("beans.xml")

    (context getBean classOf[Main]).p
    (context getBean classOf[Main]).p
    (context getBean classOf[Main]).p
    (context getBean classOf[Main]).p
  }
}
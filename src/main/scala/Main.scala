import org.springframework.context.support.ClassPathXmlApplicationContext

/**
  * Created by yan on 21.10.16.
  */
object Main extends App {

  val context = new ClassPathXmlApplicationContext("beans.xml")

  val bean = (context getBean "hello").asInstanceOf[Hello]

  println(bean message)
}
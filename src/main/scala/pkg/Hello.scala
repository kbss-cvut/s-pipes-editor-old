package pkg

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import scala.beans.BeanProperty
import scala.util.Random

/**
  * Created by yan on 21.10.16.
  */

@Component
@Scope("prototype")
class Hello {

  @BeanProperty
  var message: String = "Hello " + new Random().nextInt(10)

}

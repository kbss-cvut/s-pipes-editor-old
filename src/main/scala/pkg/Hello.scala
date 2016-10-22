package pkg

import org.springframework.stereotype.Component

import scala.beans.BeanProperty

/**
  * Created by yan on 21.10.16.
  */

@Component
class Hello {

  @BeanProperty
  var message: String = _

}

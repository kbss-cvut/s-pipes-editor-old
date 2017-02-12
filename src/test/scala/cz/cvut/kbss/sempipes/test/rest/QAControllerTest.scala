package cz.cvut.kbss.sempipes.test.rest

import cz.cvut.kbss.sempipes.rest.dto.RawJson
import cz.cvut.kbss.sempipes.service.QAService
import org.junit.Assert._
import org.junit.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http._
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.web.client.RestTemplate

/**
  * Created by yan on 2/10/17.
  */

class QAControllerTest extends BaseControllerTestRunner {

  @Autowired
  private var service: QAService = _

  private var restTemplate = new RestTemplate()

  @Test
  def generateFormGeneratesCorrectForm = {
    Mockito.when(service.generateForm("1")).thenReturn(Some(RawJson(restTemplate.exchange("https://kbss.felk.cvut.cz/sempipes-sped/service?_pId=generate-fss-form",
      HttpMethod.GET,
      new HttpEntity[AnyRef](null, new HttpHeaders()),
      classOf[String]).getBody())))
    assertTrue(mockMvc.perform(post("/nodes/1/form")).andReturn().getResponse().getContentType().contains(MediaType.APPLICATION_JSON.toString()))
  }
}
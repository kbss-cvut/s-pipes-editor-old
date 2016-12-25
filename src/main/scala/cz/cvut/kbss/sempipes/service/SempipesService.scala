package cz.cvut.kbss.sempipes.service

import java.net.URI

import cz.cvut.kbss.sempipes.model.sempipes.Module
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpEntity, HttpHeaders, HttpMethod}
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 22.12.16.
  */
@Service
class SempipesService {

  @Autowired
  private var restTemplate: RestTemplate = _

  def getModules() = {
    val uri = new URI("https://kbss.felk.cvut.cz/sempipes-sped/contexts/12/data")
    val headers = new HttpHeaders()
    headers.set(HttpHeaders.ACCEPT, "application/ld+json")
    val entity = new HttpEntity[Module](null, headers)
    restTemplate.exchange(uri,
      HttpMethod.GET,
      entity,
      classOf[Module])
  }
}
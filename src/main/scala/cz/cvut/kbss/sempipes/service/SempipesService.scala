package cz.cvut.kbss.sempipes.service

import java.io.FileOutputStream
import java.net.URI
import java.nio.file.{Files, Paths}

import cz.cvut.kbss.sempipes.model.sempipes.Module
import cz.cvut.kbss.sempipes.persistence.PersistenceFactory
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
    headers.set(HttpHeaders.ACCEPT, "text/turtle")
    val entity = new HttpEntity[String](null, headers)
    new FileOutputStream("sempipes.ttl").write(restTemplate.exchange(uri,
      HttpMethod.GET,
      entity,
      classOf[String]).getBody().getBytes())
    PersistenceFactory.init("sempipes.ttl")
    val em = PersistenceFactory.createEntityManager()
    System.err.println(em.createNativeQuery("select ?s where { ?s a ?type }", classOf[Module])
      .setParameter("type", URI.create("sm:Module")).getResultList())
    Files.delete(Paths.get("sempipes.ttl"))
  }
}
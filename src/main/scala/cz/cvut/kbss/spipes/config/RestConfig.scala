package cz.cvut.kbss.spipes.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import cz.cvut.kbss.jsonld.jackson.JsonLdModule
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration, Import}
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 16.11.16.
  */
@EnableWebMvc
@Configuration
@Import(Array(classOf[ServiceConfig]))
@ComponentScan(basePackages = Array("cz.cvut.kbss.spipes.rest"))
class RestConfig {

  @Bean
  def objectMapper: ObjectMapper = {
    val objectMapper = new ObjectMapper()
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    // Here we register the JSON-LD serialization/deserialization module
    objectMapper.registerModule(new JsonLdModule())
    objectMapper
  }

  @Bean
  def restTemplate = new RestTemplate()
}
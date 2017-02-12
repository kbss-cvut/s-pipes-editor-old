package cz.cvut.kbss.sempipes.config

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 14.12.16.
  */

import java.nio.charset.Charset
import java.util.{List => JavaList}

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Configuration, Import}
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.converter.{HttpMessageConverter, StringHttpMessageConverter}
import org.springframework.web.servlet.config.annotation.{DefaultServletHandlerConfigurer, WebMvcConfigurerAdapter}

@Configuration
@Import(Array(classOf[RestConfig], classOf[PersistenceConfig], classOf[SecurityConfig]))
class AppConfig extends WebMvcConfigurerAdapter {

  @Autowired
  private var objectMapper: ObjectMapper = _

  override def configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer) {
    configurer.enable()
  }

  override def configureMessageConverters(converters: JavaList[HttpMessageConverter[_]]) {
    val converter = new MappingJackson2HttpMessageConverter
    converter.setObjectMapper(objectMapper)
    converters.add(converter)
    val stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"))
    converters.add(stringConverter)
    super.configureMessageConverters(converters)
  }
}
package cz.cvut.kbss.sempipes.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 2/12/17.
  */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  def configureGlobal(auth: AuthenticationManagerBuilder) = {

    auth.inMemoryAuthentication.withUser("user").password("1").roles("USER")
    auth.inMemoryAuthentication.withUser("admin").password("1").roles("USER").roles("ADMIN")
    auth.inMemoryAuthentication.withUser("scripts_user").password("1").roles("SCRIPTS")
  }

  override protected def configure(http: HttpSecurity): Unit = {
    http.authorizeRequests()
      .antMatchers("/")
      .permitAll()
      .and().asInstanceOf[HttpSecurity].formLogin()
  }

}

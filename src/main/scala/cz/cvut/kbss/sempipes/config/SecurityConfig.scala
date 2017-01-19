package cz.cvut.kbss.sempipes.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
  * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 10.12.16.
  */
@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  def configureGlobal(auth: AuthenticationManagerBuilder) = {
    auth.inMemoryAuthentication.withUser("user").password("123456").roles("USER")
    auth.inMemoryAuthentication.withUser("admin").password("123456").roles("ADMIN")
    auth.inMemoryAuthentication.withUser("dba").password("123456").roles("DBA")
  }

  override protected def configure(http: HttpSecurity): Unit = {
    http.authorizeRequests()
      .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
      .antMatchers("/dba/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
      .and().asInstanceOf[HttpSecurity].formLogin()
  }
}
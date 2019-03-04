/**
 * 
 */
package com.gc.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author maurice tedder
 * 
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource jdbcDataSource;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/* (non-Javadoc)
	 * Ref: https://spring.io/guides/gs/securing-web/
	 * or http://www.baeldung.com/spring-security-basic-authentication
	 * https://docs.spring.io/spring-security/site/docs/current/reference/html/index.html
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
            .antMatchers("/", "/home", "/getstudentlist", "/js/**").permitAll()
            .antMatchers("/addstudents/**").hasAnyRole("ADMIN")
            .antMatchers("/admin/**").hasAnyRole("ADMIN")
            .antMatchers("/getrandomstudents/**").hasAnyRole("ADMIN")
            .antMatchers("/resetstudentlist/**").hasAnyRole("ADMIN")          
            .anyRequest().authenticated()           
            .and()
         .csrf() //https://github.com/spring-guides/tut-spring-security-and-angular-js/issues/71
            .ignoringAntMatchers("/logout")
            //.csrfTokenRepository(csrfTokenRepository())
            .and()
        //.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
        .formLogin()
            .loginPage("/login")//custom login page
            .permitAll()
            .and()
        .logout()
            .permitAll();                 
	}
	
	/**
	 * Configure database based authentication and authorization
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.
		jdbcAuthentication()
		.usersByUsernameQuery(
                "select username,password, enabled, userid as id from users where username=?")
        .authoritiesByUsernameQuery("select username, role from user_roles where username=?")
			//.usersByUsernameQuery(usersQuery)
			//.authoritiesByUsernameQuery(rolesQuery)
			.dataSource(jdbcDataSource)
			//.withDefaultSchema();
			.passwordEncoder(bCryptPasswordEncoder);
	}	

/*	@Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
             User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER","ADMIN","JAVAJAN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }*/
	
	/**
	 * ByCryptPasswordEncoder bean
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
}	
		
}

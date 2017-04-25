package es.joaquincaro.jwtsample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("Admin");
    }
	
	 protected void configure(HttpSecurity http) throws Exception {
	        http.csrf().disable().authorizeRequests()
	                .antMatchers("/").permitAll()
	                .antMatchers(HttpMethod.POST, "/auth").permitAll()
	                .anyRequest().authenticated();
	                //.and()
	                // And filter other requests to check the presence of JWT in header
	                //.addFilterBefore(new JWTAuthenticationFilter(),
	                //        UsernamePasswordAuthenticationFilter.class);
	    }


    
}

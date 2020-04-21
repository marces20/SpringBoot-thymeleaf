package com.bienes.secutiry;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.bienes.auth.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		 auth
		.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("select username, password, estatus from usuarios where username=?")
		.authoritiesByUsernameQuery("select u.username, p.nombre from usuario_perfil up " +
			"inner join usuarios u on u.id = up.id_usuario " +
			"inner join perfiles p on p.id = up.id_perfil " +
			"where u.username = ?");
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		// Los recursos estáticos no requieren autenticación
		.antMatchers(
			"/js/**",
			"/img/**",
			"/css/**",
			"/plugins/**").permitAll()
		// Las vistas públicas no requieren autenticación
		//.antMatchers("/").permitAll()
		// Todas las demás URLs de la Aplicación requieren autenticación
		.antMatchers("/expedientes/index").hasAnyRole("ADMIN","VISTA_EXPEDIENTE")
		.antMatchers("/expedientes/edit/**").hasAnyRole("ADMIN")	
		
		.anyRequest().authenticated()
		.and()
		    .formLogin()
		        .successHandler(successHandler)
		        .loginPage("/login")
		    .permitAll()
		.and()
			.logout().permitAll()
		.and()
			.exceptionHandling().accessDeniedPage("/error_403");
	}
	
	
}

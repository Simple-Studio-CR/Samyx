package com.samyx;

import com.samyx.auth.handler.LoginSuccessHandler;
import com.samyx.service.impl.JpaUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private LoginSuccessHandler successHandler;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JpaUserDetailsService userDetailsService;

	protected void configure(HttpSecurity http) throws Exception {
		((HttpSecurity) ((HttpSecurity) ((HttpSecurity) ((FormLoginConfigurer) ((FormLoginConfigurer) ((HttpSecurity) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) ((HttpSecurity) http
				.headers().frameOptions().disable().and())
						.authorizeRequests()
						.antMatchers(new String[] { "/css/**", "/scss/**", "/js/**", "/images/**", "/locale/**",
								"/ubicacion/**", "/nuevo-registro/**", "/account/forgot/**" })).permitAll()

										.anyRequest()).authenticated().and()).formLogin()
												.successHandler((AuthenticationSuccessHandler) this.successHandler))
														.loginPage("/login")

														.permitAll()).and()).logout().permitAll().and())
																.exceptionHandling().accessDeniedPage("/error_403")
																.and()).sessionManagement()
																		.invalidSessionUrl("/login/?expired");
	}

	@Autowired
	public void configurerGlobl(AuthenticationManagerBuilder build) throws Exception {
		build.userDetailsService((UserDetailsService) this.userDetailsService)
				.passwordEncoder((PasswordEncoder) this.passwordEncoder);
	}
}
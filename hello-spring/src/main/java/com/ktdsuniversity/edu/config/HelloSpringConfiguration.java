package com.ktdsuniversity.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ktdsuniversity.edu.exceptions.handlers.AuthorizationDeniedeExceptionHandler;
import com.ktdsuniversity.edu.members.dao.MembersDao;
import com.ktdsuniversity.edu.security.authenticate.filters.JsonWebTokenAuthenticationFilter;
import com.ktdsuniversity.edu.security.authenticate.handlers.LoginFailureHandler;
import com.ktdsuniversity.edu.security.authenticate.handlers.LoginSuccessHandler;
import com.ktdsuniversity.edu.security.authenticate.service.SecurityPasswordEncoder;
import com.ktdsuniversity.edu.security.authenticate.service.SecurityUserDetailsService;
import com.ktdsuniversity.edu.security.providers.JsonWebTokenAuthenticationProvider;
import com.ktdsuniversity.edu.security.providers.UsernameAndPasswordAuthenticationProvider;


// application.yml에서 작성할 수 없는 설정들을 적용하기 위한 Annotation
// @Component 의 자식 Annotation
@Configuration
// spring-boot-starter-validation 동작 활성화 시키기
// @EnableWebMvc가 추가되면 application.yml의 mvc 관련 설정들이 모두 무시된다.
//   1. spring.mvc.view.prefix, spring.mvc.view.suffix
//   2. src/main/resources/static 경로 사용 불가능.
@EnableWebMvc
// 생략 가능
// Spring Security 라이브러리를 활성화 시킨다.
// Spring Security의 필터목록을 확인하기 위해서 작성한다.
@EnableWebSecurity(debug = true)
// 컨트롤러 혹은 서비스코드에서 권한 검사를 수행하기 위한 애노테이션 추가.
@EnableMethodSecurity
public class HelloSpringConfiguration implements
		// WebMvc 설정을 위한 Configuration
		// @EnableWebMvc Annotation 에서 적용하는 기본 설정들을 변경하기 위함.
		WebMvcConfigurer {

	@Autowired
	private MembersDao membersDao;

	// application.yml에서 관련된 정보를 가져온다.
	// @Value = 환경설정 정보를 Bean 으로 가져오는 방법. 괄호에 환경설정 경로를 작성.
	// @Value("${시작부터 가져오고자하는 것 까지의 경로}")
	// @Value가 동작하는 조건 : @Component 가 적용된 클래스에서만 가능.
	@Value("${app.jwt.secret-key}") 
	private String jwtSecretKey;
	@Value("${app.jwt.issuer}")
	private String jwtIssuer;
	
	@Bean
	JsonWebTokenAuthenticationProvider createJwtAuthenticationProvider() {
		return new JsonWebTokenAuthenticationProvider(this.jwtSecretKey, this.jwtIssuer);
	}
	
	// SecurityPasswordEncoder의 Bean을 생성한다.
	@Bean // 메소드가 실행되어서 반환되는 객체를 Bean Container에 적재한다.
	PasswordEncoder createPasswordEncoder() {
		return new SecurityPasswordEncoder();
	}

	// SecurityUserDetailsService의 Bean을 생성한다.
	// @Bean으로 생성하는 객체(Bean)들은 필요한 의존 객체를 생성자로 주입해 주어야 한다.
	@Bean
	UserDetailsService createUserDetailsService() {
		return new SecurityUserDetailsService(this.membersDao);
	}

	// UsernameAndPasswordAuthenticationProvider의 Bean을 생성한다.
	@Bean
	AuthenticationProvider createAuthenticationProvider() {
		UserDetailsService userDetailsService = this.createUserDetailsService();
		PasswordEncoder passwordEncoder = this.createPasswordEncoder();

		return new UsernameAndPasswordAuthenticationProvider(userDetailsService, passwordEncoder);
	}
	
	@Bean
	AuthenticationSuccessHandler createLoginSuccessHandler() {
		return new LoginSuccessHandler(this.membersDao);
	}
	
	@Bean
	AuthenticationFailureHandler createLoginFailureHandler() {
		return new LoginFailureHandler(this.membersDao);
	}
	
	@Bean
	OncePerRequestFilter createJwtAuthFilter() {
		return new JsonWebTokenAuthenticationFilter(
				  this.createJwtAuthenticationProvider()
				, this.createUserDetailsService());
	}
	
	/**
	 * 특정 URL에 대해서 Spring Security 가 개입하지 않도록 설정.
	 * /WEB-INF/views 아래의 모든 jsp 파일들은 Spring Security의 간섭을 받지 않는다.
	 * Controller에서 해당 페이지를 노출하려 할 때 "WEB-INF/views/.../*.jsp" 경로 사용시
	 * 인증이 된 사용자에게만 노출시키려 하는 경우가 존재 ==> Spring Security가 개입하지 않도록 설정.
	 *  
	 * @return
	 */
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
				.requestMatchers("/WEB-INF/views/**");
	}
	
	// Spring Login Filter(BasicAuthenticationFilter) 등록.
	// Spring Security의 기본 로그인 절차를 수정하는 작업.
	@Bean
	SecurityFilterChain configureFilterChain(HttpSecurity httpSecurity) {
		
		// 상대방이 내 서버로 접속할 수 있도록 허용하기
		// ==> 내 서버로 접속 가능한 안전한 URL 등록하기
		httpSecurity.cors(corsConfigurer -> {
			
			CorsConfigurationSource source = (HttpServletRequest) -> {
				
				// 허용할 타 사이트의 도메인을 작성.
				CorsConfiguration config = new CorsConfiguration();
				
				// 허용할 타 사이트의 URL 
				// ==> 해당 URL에서 요청하는 모든 접근(API)들을 허용하겠다.
				config.addAllowedOrigin("http://192.168.211.28:8080");
				
				// 허용할 타 사이트의 요청 Method
				// ==> 해당 URL에서 POST, GET으로 요청되는 접근들만 허용하겠다.
				config.addAllowedMethod("POST");
				config.addAllowedMethod("GET");
				
				// 허용할 타 사이트의 요청 HttpHeader
				// ==> 모든 요청 HttpHeader를 허용하겠다.
				config.addAllowedHeader("*");
				
				// if(Naver에서 요청 HttpHeader에 "X-HTTP-Request-Naver-Client-ID"를 추가할 것을 요구한다.
				// config.addAllowedHeader("X-HTTP-Request-Naver-Client-ID"); - 네이버측에서 추가
				// ==> 저 헤더가 없으면 허용하지 않는다.
				
				return config;
				
				
			};
			
			corsConfigurer.configurationSource(source);
		});
		
		// CSRF 수정, 댓글 등록 불가. (Invalid CSRF token found for  ...)
		// CSRF를 체크하는 SecurityFilter(CsrfFilter)를 무효화.
//		 httpSecurity.csrf(csrf -> csrf.disable());
		
		// API 통신에서는 CSRF를 체크하지 않도록 설정.
		httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
		
		// Custom Filter(JsonWebTokenAuthenticationFilter) 추가.
		httpSecurity.addFilterAfter(this.createJwtAuthFilter()
								  , UsernamePasswordAuthenticationFilter.class);
		
		// AuthorizationDeniedExceptionHandler 를 추가한다.
		// Controller 코드 이하에서 @PreAuthorized() 검증에 실패하면 아래 설정에 등록한 Handler가 동작하게 된다.
		httpSecurity.exceptionHandling(exceptionHandling -> 
							exceptionHandling.accessDeniedHandler(new AuthorizationDeniedeExceptionHandler()));
		
		// UsernamePasswordAuthenticationFilter 수정.
		httpSecurity.formLogin(formLogin -> 
					// Login URL 지정.
					formLogin.loginPage("/login")
					// Login 인증 처리 URL 지정 
					// (UsernameAndPasswordAuthenticationProvider가 실행될 Endpoint)
							 .loginProcessingUrl("/login-provider")
					// 로그인에 필요한 아이디 파라미터 이름을 "username"에서 "email"로 변경한다.
							 .usernameParameter("email")
					// 로그인에 성공하면 뭐할까?
					// this.membersDao.updateSuccessLogin(loginVO); 실행해야 한다.
							 .successHandler(this.createLoginSuccessHandler())
					// 로그인에 실패하면 뭐할까?
					// this.membersDao.updateIncreaseLoginFailCount(loginVO.getEmail());
					// this.membersDao.updateBlock(loginVO.getEmail());
							 .failureHandler(this.createLoginFailureHandler())
		);
		
		// 10시 15분 시작.
		return httpSecurity.build();
	}

	// configureViewResolvers 설정
	// spring.mvc.view.prefix, spring.mvc.view.suffix 재설정
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	// addResourceHandlers
	// src/main/resources/static 경로의 endpoint 재설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// /static/css/ 폴더에 있는 파일들에 대한 Endpoint 설정.
		registry.addResourceHandler("/css/**") // /static/css/ 의 엔드포인트
				.addResourceLocations("classpath:/static/css/"); // /static/css/ 의 물리적인 위치

		// /static/image/ 폴더에 있는 파일들에 대한 Endpoint 설정.
		registry.addResourceHandler("/image/**") // /static/image/ 의 엔드포인트
				.addResourceLocations("classpath:/static/image/"); // /static/image/ 의 물리적인 위치

		// /static/js/ 폴더에 있는 파일들에 대한 Endpoint 설정.
		registry.addResourceHandler("/js/**") // /static/js/ 의 엔드포인트
				.addResourceLocations("classpath:/static/js/"); // /static/js/ 의 물리적인 위치
	}
}

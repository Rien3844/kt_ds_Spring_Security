package com.ktdsuniversity.edu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ktdsuniversity.edu.config.interceptors.SessionInterceptor;

// application.yml에서 작성할 수 없는 설정들을 적용하기 위한 Annotation
// @Component 의 자식 Annotation
@Configuration
// spring-boot-starter-validation 동작 활성화 시키기
// @EnableWebMvc가 추가되면 application.yml의 mvc 관련 설정들이 모두 무시된다.
//   1. spring.mvc.view.prefix, spring.mvc.view.suffix
//   2. src/main/resources/static 경로 사용 불가능.
@EnableWebMvc
public class HelloSpringConfiguration implements 
		// WebMvc 설정을 위한 Configuration
		// @EnableWebMvc Annotation 에서 적용하는 기본 설정들을 변경하기 위함.
		WebMvcConfigurer {
	
	// Interceptor 등록 및 대상 URL 지정.
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		SessionInterceptor sessionInterceptor = new SessionInterceptor();
		
		registry.addInterceptor(sessionInterceptor)
				.addPathPatterns("/**") // 모든 URL을 대상으로 sessionInterceptor를 수행해라!
				.excludePathPatterns(
						"/regist/check/duplicate/**", // 회원가입 이메일 중복 검사.
						"/regist", // 회원가입 페이지 & 처리
						"/login", // 로그인 페이지 & 처리
						"/js/**", // static resources
						"/css/**", // static resources
						"/image/**", // static resources
						"/", // 게시글 목록 조회
						"/view/**", // 게시글 내용 조회
						"/file/**" // 첨부파일 다운로드
						) // sessionInterceptor가 적용되지 않을 URL 명시.
				;
		
	}
	
	// configureViewResolvers 설정
	//  spring.mvc.view.prefix, spring.mvc.view.suffix 재설정
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	
	// addResourceHandlers
	//  src/main/resources/static 경로의 endpoint 재설정
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

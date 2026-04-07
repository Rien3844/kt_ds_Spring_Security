package com.ktdsuniversity.edu.config.interceptors;

import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 로그인이 필요한(세션 데이터가 필요한) URL
 *  1. 글 작성 페이지
 *  2. 글 작성 처리 URL
 *  3. 글 수정 페이지
 *  4. 글 수정 처리 URL
 *  5. 글 삭제 처리 URL
 *  6. 회원 정보 수정 페이지
 *  7. 회원 정보 수정 처리 URL
 *  8. 회원 삭제 처리 URL
 *  9. 회원 탈퇴 처리 URL
 *  10. 회원 로그아웃 URL
 *  ==> 위 URL 10개에 세션없이 접근할 경우(컨트롤러가 실행되기 전) 로그인 페이지로 이동시키기.
 * 로그인이 필요 없는(세션 데이터가 필요없는) URL
 *  1. 회원 가입 페이지
 *  2. 회원 가입 처리 URL
 *  3. 로그인 페이지
 *  4. 로그인 처리 URL
 *  5. static resource (JS, CSS, Image).
 *  6. 게시글 목록 페이지
 *  7. 게시글 내용 조회 페이지
 *  8. 이메일 중복 검사 URL
 *  9. 첨부파일 다운로드 URL
 *  ==> 위 URL 9개 중 1 ~ 4 까지는 세션이 있는 상태에서 접근을 못해야 한다.
 */
public class SessionInterceptor
		// 인터셉터의 기능을 정의해둔 인터페이스.
		implements HandlerInterceptor {

	/**
	 * 컨트롤러가 실행되기 이전에 수행할 공통 코드를 작성하는 영역.
	 * 
	 * @param request 브라우저(클라이언트)가 요청한 내용 - header, parameter, file, session ...
	 * @param response 컨트롤러가 실행되기 이전에 브라우저(클라이언트)로 응답을 보내는 역할. (필요시 사용)
	 * @param handler 실행할 컨트롤러
	 * @return 컨트롤러의 실행 여부를 반환. (true: 컨트롤러를 실행한다!, false: 컨트롤러를 실행하지 않는다!)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 1. 세션을 가져온다.
		HttpSession session = request.getSession();
		
		// 2. 세션이 있는지 검사한다. (__LOGIN_DATA__)
		if (session.getAttribute("__LOGIN_DATA__") == null) {
			// 3. 세션이 없으면, 컨트롤러는 실행시키지 않는다.
			//    대신, 사용자에게 로그인 페이지를 보여주도록 한다. 
			//    URL은 변동되지 않아야 한다. ==> 예> URL => 게시글 수정 처리, 보여지는 페이지는 로그인 페이지.
			String loginPagePath = "/WEB-INF/views/members/login.jsp";
			// 브라우저에게 보여질 화면 설정.
			RequestDispatcher dispatcher = request.getRequestDispatcher(loginPagePath);
			// 브라우저에게 화면을 보내준다. URL은 바뀌지 않는다.
			dispatcher.forward(request, response);
			
			return false;
		}
		
		// 4. 세션이 있으면, 컨트롤러를 실행시킨다.
		return true;
	}

	/**
	 * 컨트롤러가 실행된 이후에 수행할 공통 코드를 작성하는 영역.
	 * 예> 모델데이터 추가. 모델데이터 삭제. 수정....
	 * 
	 * @param request 브라우저(클라이언트)가 요청한 내용 - header, parameter, file, session ...
	 * @param response 컨트롤러가 실행된 후에 브라우저(클라이언트)로 응답을 보내는 역할. (필요시 사용)
	 * @param handler 실행된 컨트롤러
	 * @param modelAndView 컨트롤러가 반환시킨 View(Template)와 Model
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * JSP를 HTML로 변환시켜 브라우저(클라이언트)로 반환하기 직전에 수행되는 영역.
	 * 예> response에 내용을 첨부/제거. (파일, 텍스트, 이미지, 암호화 등등), 처리되지 않고 던져진 예외 핸들링.
	 * 
	 * @param request 브라우저(클라이언트)가 요청한 내용 - header, parameter, file, session ...
	 * @param response 컨트롤러가 실행된 후에 브라우저(클라이언트)로 응답을 보내는 역할. (필요시 사용)
	 * @param handler 실행된 컨트롤러
	 * @param ex catch 되지 않는 예외 객체 ==> 예외의 종류를 통해 알맞은 예외 처리등을 수행.
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
}

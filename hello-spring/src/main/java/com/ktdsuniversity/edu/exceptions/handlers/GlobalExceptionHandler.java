package com.ktdsuniversity.edu.exceptions.handlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktdsuniversity.edu.common.utils.AuthUtils;
import com.ktdsuniversity.edu.common.utils.ServletUtils;
import com.ktdsuniversity.edu.exceptions.HelloSpringApiException;
import com.ktdsuniversity.edu.exceptions.HelloSpringException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Spring Application에서 던져진 catch되지 않은
 * 예외들을 처리하는 클래스.
 * 
 * @Controller와 유사한 형태.
 *  ==> URL이 endpoint
 * 
 * @ControllerAdvice
 *  ==> Exception이 endpoint
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	public void viewErrorPage(AuthorizationDeniedException ade, Model model) {
		
		logger.error(ade.getMessage(), ade);
		
		HttpServletResponse response = ServletUtils.getResponse();
		if (ServletUtils.isApiRequest()) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			
			PrintWriter writer;
			try {
				writer = response.getWriter();
				writer.append("{ \"error\": \"인증이 필요하거나 잘못된 권한입니다.\" }");
				writer.flush();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		else {
			HttpServletRequest request = ServletUtils.getRequest();
			String viewPath = "/WEB-INF/views/members/login.jsp";
			if (AuthUtils.isAuthenticated()) {
				viewPath = "/WEB-INF/views/errors/403.jsp";
				request.setAttribute("errorMessage", "잘못된 접근입니다. 권한이 충분하지 않습니다.");
				return;
			}
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
			try {
				requestDispatcher.forward(request, response);
			} catch (ServletException | IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * HelloSpringException이 던져지면,
	 * viewErrorPage가 실행된다.
	 * 실행된 결과는 ModelAndView가 된다.
	 * 
	 * @return 사용자에게 보여줄 템플릿의 이름
	 */
	@ExceptionHandler(HelloSpringException.class)
	public String viewErrorPage(HelloSpringException hse, Model model) {
		
		logger.error(hse.getMessage(), hse);
		
		String message = hse.getMessage();
		model.addAttribute("errorMessage", message);
		
		String errorPage = hse.getErrorPage();
		
		Object modelData = hse.getObject();
		if (modelData != null) {
			model.addAttribute("errorData", modelData);
		}
		
		return errorPage;
	}
	
	@ResponseBody
	@ExceptionHandler(HelloSpringApiException.class)
	public Map<String, Object> returnErrorJson(HelloSpringApiException hsae) {
		logger.error(hsae.getMessage(), hsae);
		
		int status = hsae.getErrorStatus();
		Object errorObjet = hsae.getError();
		
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("status", status);
		responseData.put("error", errorObjet);
		
		return responseData;
	}
	
	@ExceptionHandler(RuntimeException.class)
	public String viewSystemErrorPage(RuntimeException re) {
		logger.error(re.getMessage(), re);
		
		return "errors/500";
	}
	
}

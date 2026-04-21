package com.ktdsuniversity.edu.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class ServletUtils {

	private ServletUtils() {}
	
	public static boolean isApiRequest() {
		return ServletUtils.getRequest().getServletPath().startsWith("/api");
	}
	
	public static HttpServletRequest getRequest() {
		return ServletUtils.getRequestAttributes().getRequest();
	}
	
	public static HttpServletResponse getResponse() {
		return ServletUtils.getRequestAttributes().getResponse();
	}
	
	public static String getIp() {
		return ServletUtils.getRequest().getRemoteAddr();
	}
	
	private static ServletRequestAttributes getRequestAttributes() {
		return (ServletRequestAttributes)RequestContextHolder.getRequestAttributes(); 
	}
	
}

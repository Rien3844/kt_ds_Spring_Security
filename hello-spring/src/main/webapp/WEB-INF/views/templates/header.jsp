<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <!-- API / AJAX를 위한 CSRF 토큰 가져오기  -->
    <sec:csrfMetaTags/>
    <title>${param.title}</title>
    <link rel="stylesheet" type="text/css" href="/css/hello-spring.css" />
    <script type="text/javascript" src="/js/jquery-4.0.0.slim.min.js"></script>
    ${param.scripts}
  </head>
  <body>
    <div class="wrapper">
      <div class="header">
         <sec:authorize access="!isAuthenticated()">
            <%-- 로그인 안했을 때의 링크 시작 --%>
            <a href="/regist">회원가입</a>
            <a href="/login">로그인</a>
            <%-- 로그인 안했을 때의 링크 끝 --%>
         </sec:authorize>
         
         <sec:authorize access="isAuthenticated()">
            <%-- 로그인 했을 때의 링크 시작 --%>
            <div
              class="member-info"
              data-email="<sec:authentication property='principal.email' />"
            >
              <sec:authentication property='principal.name' />
              (<sec:authentication property='principal.email' />)
            </div>
            <a href="/member/view/<sec:authentication property='principal.email' />"
              >마이페이지</a
            >
            <a href="/logout">로그아웃</a>
            <%-- 로그인 했을 때의 링크 끝 --%>
         </sec:authorize>
      </div>


<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>회원 목록</title>
    <link rel="stylesheet" type="text/css" href="/css/hello-spring.css">
  </head>
  <body>
    <div class="grid list">
      <h1>회원 목록</h1>
      <div>총 ${searchCount}명의 회원이 검색되었습니다.</div>
      <ul class="grid member-list">
        <li class="header">
          <ul class="header-item">
            <li>이메일</li>
            <li>이름</li>
            <li>비밀번호</li>
          </ul>
        </li>
        <c:choose>
          <c:when test="${not empty searchList}">
            <!-- searchList가 존재하면, 반복하여 데이터를 보여주고 -->
            <li class="body">
              <c:forEach items="${searchList}" var="member">
                <ul class="body-item">
                  <li>${member.email}</li>
                  <li>
                    <a href="/member/view/${member.email}">${member.name}</a>
                  </li>
                  <li>${member.password}</li>
                </ul>
              </c:forEach>
            </li>
          </c:when>
          <c:otherwise>
            <!-- searchResult가 존재하지 않으면, "검색된 데이터가 없습니다"를 보여주고 -->
            <li class="footer">
              <ul class="footer-item">
                <li class="center">검색된 회원이 없습니다.</li>
              </ul>
            </li>
          </c:otherwise>
          </c:choose>
      </ul>

      <div class="btn-group">
        <div class="right-align">
          <a href="/regist">새로운 회원 등록</a>
        </div>
      </div>
    </div>
  </body>
</html>

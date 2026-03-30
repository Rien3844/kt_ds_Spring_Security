<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>회원 목록</title>
    <link rel="stylesheet" type="text/css" href="/css/hello-spring.css" />
  </head>
  <body>
    <div class="grid member-list">
      <h1>회원 목록</h1>
      <div>총 ${searchCount}명의 회원이 검색되었습니다.</div>
      <table class="grid">
        <colgroup>
          <col width="33.3%" />
          <col width="33.3%" />
          <col width="33.3%" />
        </colgroup>
        <thead>
          <tr>
            <th>이메일</th>
            <th>이름</th>
            <th>비밀번호</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${not empty searchList}">
              <%-- searchList가 존재하면, 반복하여 데이터를 보여주고 --%>
              <c:forEach items="${searchList}" var="member">
                <tr>
                  <td>${member.email}</td>
                  <td>
                    <a href="/member/view/${member.email}">${member.name}</a>
                  </td>
                  <td>${member.password}</td>
                </tr>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <%-- searchList가 존재하지 않으면, "검색된 데이터가 없습니다"를 보여주고 --%>
              <tr>
                <td colspan="3">검색된 데이터가 없습니다.</td>
              </tr>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
      <div class="btn-group">
        <div class="right-align">
          <a href="/regist">새로운 회원 등록</a>
        </div>
      </div>
    </div>
  </body>
</html>

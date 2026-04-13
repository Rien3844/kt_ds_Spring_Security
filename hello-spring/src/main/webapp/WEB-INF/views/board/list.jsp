<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 목록</title>
    <link rel="stylesheet" type="text/css" href="/css/hello-spring.css" />
  </head>
  <body>
    <div class="wrapper">
      <div class="grid list">
        <h1>게시글 목록</h1>
        <div>총 ${searchCount}개의 게시글이 검색되었습니다.</div>
        <%-- HTML 주석: 브라우저 개발자 도구에서 노출되는 주석. --%>
        <%-- JSP 주석: 브라우저 개발자 도구에서 노출되지 않는 주석. --%>
        <%-- <% for (BoardVO board: searchResult) { %>
          <div ><%=board.getId()%></div>
      <% } %> --%>
        <table class="grid">
          <colgroup>
            <col width="200" />
            <col width="*" />
            <col width="150" />
            <col width="80" />
            <col width="180" />
            <col width="180" />
          </colgroup>
          <thead>
            <tr>
              <th>번호</th>
              <th>제목</th>
              <th>이메일</th>
              <th>조회수</th>
              <th>등록일</th>
              <th>수정일</th>
            </tr>
          </thead>
          <tbody>
            <c:choose>
              <c:when test="${not empty searchResult}">
                <%-- searchResult가 존재하면, 반복하여 데이터를 보여주고 --%>
                <c:forEach items="${searchResult}" var="board">
                  <tr>
                    <td>${board.id}</td>
                    <td>
                      <a href="/view/${board.id}">${board.subject}</a>
                    </td>
                    <td>${board.email}</td>
                    <td>${board.viewCnt}</td>
                    <td>${board.crtDt}</td>
                    <td>${board.mdfyDt}</td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <%-- searchResult가 존재하지 않으면, "검색된 데이터가 없습니다"를 보여주고 --%>
                <tr>
                  <td colspan="6">검색된 데이터가 없습니다.</td>
                </tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
        <div class="btn-group">
          <div class="right-align">
            <a href="/write">새로운 게시글 작성</a>
          </div>
        </div>
        
        <ul class="page-navigator">
          <c:forEach begin="0" 
                     end="${pagination.pageCount}" 
                     step="1"
                     var="page">
            ${page}
          </c:forEach>
        </ul>
      </div>
    </div>
  </body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <jsp:include page="/WEB-INF/views/templates/header.jsp">
    <jsp:param value="회원 목록" name="title" />
    <jsp:param value="<script type='text/javascript' src='/js/members.js'></script>" 
                   name="scripts"/>
  </jsp:include>
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
      
      <select id="list-size">
        <option value="10" ${pagination.listSize eq "10" ? "selected": ""}>10개씩</option>
        <option value="20" ${pagination.listSize eq "20" ? "selected": ""}>20개씩</option>
        <option value="50" ${pagination.listSize eq "50" ? "selected": ""}>50개씩</option>
        <option value="100" ${pagination.listSize eq "100" ? "selected": ""}>100개씩</option>
      </select>
      
      <c:if test="${pagination.pageCount > 0}">
        <ul class="page-navigator">
          <c:if test="${pagination.hasPrevPageGroup}">
            <li>
              <a data-page-no="0" href="javascript:void(-1);">처음</a>
            </li>
            <li>
              <a data-page-no="${pagination.prevPageGroupStartPageNo}" href="javascript:void(-1);">이전</a>
            </li>
          </c:if>
          <c:forEach begin="${pagination.groupStartPageNo}" 
                     end="${pagination.groupEndPageNo}" 
                     step="1"
                     var="page">
            <li class="${page eq pagination.pageNo ? 'active': ''}">
              <a data-page-no="${page}" href="javascript:void(-1);">${page + 1}</a>
            </li>
          </c:forEach>
          <c:if test="${pagination.hasNextPageGroup}">
            <li>
              <a data-page-no="${pagination.nextPageGroupStartPageNo}" href="javascript:void(-1);">다음</a>
            </li>
            <li>
              <a data-page-no="${pagination.pageCount - 1}" href="javascript:void(-1);">마지막</a>
            </li>
          </c:if>
        </ul>
      </c:if>
    </div>
  <jsp:include page="/WEB-INF/views/templates/footer.jsp"></jsp:include>
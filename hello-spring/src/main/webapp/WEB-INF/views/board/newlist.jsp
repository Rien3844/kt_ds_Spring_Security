<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

    <!-- /templates/header.jsp import -->
    <jsp:include page="/WEB-INF/views/templates/header.jsp">
        <jsp:param value="게시글 목록" name="title" />
        <jsp:param value="<script type='text/javascript' src='/js/board.js'></script>" 
                   name="scripts"/>
    </jsp:include>
    
      <div class="grid list">
        <h1>게시글 목록</h1>
        <div>
            총 ${searchCount}개의 게시글이 검색되었습니다.
            <!-- 로그인 사용자가 "슈퍼 관리자" 
                 권한일 때 노출되도록 한다. -->
            <sec:authorize access="hasRole('RL-20260414-000001')">
	            <a href="/delete/all">
	                전체 게시글 삭제
	            </a>
            </sec:authorize>
        </div>
        <ul class="grid articles">
          <li class="header">
            <ul class="header-item">
              <li>번호</li>
              <li>제목</li>
              <li>이름</li>
              <li>조회수</li>
              <li>등록일</li>
              <li>수정일</li>
            </ul>
          </li>
          <c:choose>
            <c:when test="${not empty searchResult}">
              <%-- searchResult가 존재하면, 반복하여 데이터를 보여주고 --%>
              <li class="body">
                <c:forEach items="${searchResult}" var="board">
                  <ul class="body-item">
                    <li class="center">${board.id}</li>
                    <li>
                      <a href="/view/${board.id}">${board.subject}</a>
                    </li>
                    <li>${board.membersVO.name}</li>
                    <li class="center">${board.viewCnt}</li>
                    <li class="center">${board.crtDt}</li>
                    <li class="center">${board.mdfyDt}</li>
                  </ul>
                </c:forEach>
              </li>
            </c:when>
            <c:otherwise>
              <%-- searchResult가 존재하지 않으면, "검색된 데이터가 없습니다"를 보여주고 --%>
              <li class="footer">
                <ul class="footer-item">
                  <li class="center">검색된 데이터가 없습니다.</li>
                </ul>
              </li>
            </c:otherwise>
          </c:choose>
        </ul>

        <div class="btn-group">
          <div class="right-align">
            <sec:authorize access="isAuthenticated()">
              <a href="/write">새로운 게시글 작성</a>
            </sec:authorize>
          </div>
        </div>
        
        <div class="search-box">
          <select id="list-size">
            <option value="10" ${pagination.listSize eq "10" ? "selected": ""}>10개씩</option>
            <option value="20" ${pagination.listSize eq "20" ? "selected": ""}>20개씩</option>
            <option value="50" ${pagination.listSize eq "50" ? "selected": ""}>50개씩</option>
            <option value="100" ${pagination.listSize eq "100" ? "selected": ""}>100개씩</option>
          </select>
          <div>
            <select id="search-type">
              <option value="email" ${pagination.searchType eq "email" ? "selected" : ""}>
                Email로 검색</option>
              <option value="name" ${pagination.searchType eq "name" ? "selected" : ""}>
                작성자 이름으로 검색</option>
              <option value="subject" ${pagination.searchType eq "subject" ? "selected" : ""}>
                제목으로 검색</option>
              <option value="content" ${pagination.searchType eq "content" ? "selected" : ""}>
                내용으로 검색</option>
              <option value="subject+content" 
                ${pagination.searchType eq "subject content" ? "selected" : ""}>
                    제목 + 내용으로 검색</option>
            </select>
            <input type="text" id="search-keyword" placeholder="검색어를 입력하세요." 
                   value="${pagination.searchKeyword}" />
            <button type="button" class="search-button">검색!</button>
          </div>
        </div>
        
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
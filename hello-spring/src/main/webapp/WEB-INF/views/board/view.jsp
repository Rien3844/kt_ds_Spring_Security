<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/views/templates/header.jsp">
  <jsp:param value="게시글 내용 조회 : ${article.id}" name="title" />
  <jsp:param
    value="<script type='text/javascript' src='/js/reply.js'></script>"
    name="scripts"
  />
</jsp:include>

<h1>게시글 내용 조회</h1>
<div class="grid view" data-article-id="${article.id}">
  <span>아이디</span>
  <div>${article.id}</div>

  <span>제목</span>
  <div>${article.subject}</div>

  <span>이름</span>
  <div>${article.membersVO.name} (가입일: ${article.membersVO.registDate})</div>

  <span>조회수</span>
  <div>${article.viewCnt}</div>

  <span>작성일</span>
  <div>${article.crtDt}</div>

  <span>마지막 수정일</span>
  <div>${article.mdfyDt}</div>

  <span>첨부파일</span>
  <div>
    <ul class="vertical-list">
      <c:forEach items="${article.files}" var="file">
        <li>
          <a href="/file/${file.fileGroupId}/${file.fileNum}"
            >${file.displayName}</a
          >
        </li>
      </c:forEach>
    </ul>
  </div>

  <span>내용</span>
  <%-- <pre > ==> Presentation --%>
  <pre>${article.content}</pre>

  <div class="replies-count">
    총 <span class="count">0</span>개의 댓글이 검색되었습니다.
    <!-- 로그인 사용자가 "슈퍼 관리자" 
         권한일 때 노출되도록 한다. -->
    <sec:authorize access="hasRole('RL-20260414-000001')">
	    <a href="/reply/delete/all/${article.id}">
	        전체 댓글 삭제
	    </a>
    </sec:authorize>
  </div>
  <ul class="replies"></ul>
  <div class="reply-form">
    <input type="text" class="parent-reply-id" readonly />
    <textarea class="reply-content"></textarea>
    <input type="file" class="reply-attach-file" multiple />
    <button class="reply-save" data-article-id="${article.id}">등록</button>
  </div>

  <template class="reply-item-update-files">
    <div>
      <input
        type="checkbox"
        id="#fileGroupId#-#fileNum#"
        name="deleteFileNum"
        value="#fileNum#"
      />
      <label for="#fileGroupId#-#fileNum#">#fileDisplayName#</label>
    </div>
  </template>

  <template class="reply-item-update-template">
    <div class="update-form">
      <textarea></textarea>
      <div class="update-file-list"></div>
      <input type="file" class="reply-update-attach-file" multiple />
      <div class="update-button-area">
        <button class="update-save">저장</button>
        <button class="update-cancel">취소</button>
      </div>
    </div>
  </template>

  <template class="reply-item-template">
    <li class="reply-item" data-reply-id="#replyId#">
      <div class="writer">
        <span class="writer-name">#name#</span>
        <span class="writer-email">(#email#)</span>
        <span class="recommend-count">#recommendCount#</span> 추천
      </div>
      <div class="dates">
        <div class="create-date">#createDate# 작성</div>
        <div class="modify-date">#modifyDate# 수정</div>
      </div>
      <pre class="content">#content#</pre>
      <div class="reply-attach-files" data-files=""></div>
      <div class="links">
        <span class="links-write">답글 쓰기</span>
        <span class="links-recommend">추천하기</span>
        <span class="links-update">수정</span>
        <span class="links-delete">삭제</span>
      </div>
    </li>
  </template>

  <div class="btn-group">
    <div class="right-align">
      <!-- 사용자의 권한(ROLE)이 
           슈퍼관리자 혹은 관리자 라면 내가 쓰지 않은 게시글이라도 
           수정 및 삭제할 수 있다. -->
      <sec:authorize 
        access="hasAnyRole('RL-20260414-000001', 'RL-20260414-000002')" 
        var="isAdmin" />
      
      <sec:authorize access="isAuthenticated()">
	      <sec:authentication property="principal.email" var="loginUserEmail" />
	      <c:if test="${ isAdmin or loginUserEmail eq article.email}">
	        <a href="/update/${article.id}">수정</a>
	        <a href="/delete?id=${article.id}">삭제</a>
	      </c:if>
      </sec:authorize>
    </div>
  </div>
</div>
<jsp:include page="/WEB-INF/views/templates/footer.jsp"></jsp:include>

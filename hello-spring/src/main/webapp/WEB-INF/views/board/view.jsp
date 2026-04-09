<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
  </div>
  <ul class="replies"></ul>
  <div class="reply-form">
    <input type="text" class="parent-reply-id" readonly />
    <textarea class="reply-content"></textarea>
    <input type="file" class="reply-attach-file" />
    <button class="reply-save" data-article-id="${article.id}">등록</button>
  </div>

  <template class="reply-item-template">
    <li class="reply-item" data-reply-id="#replyId#">
      <div class="writer">
        <span class="writer-name">#name#</span>
        <span class="writer-email">(#email#)</span>
      </div>
      <div class="dates">
        <div class="create-date">#createDate# 작성</div>
        <div class="modify-date">#modifyDate# 수정</div>
      </div>
      <pre class="content">#content#</pre>
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
      <c:if test="${sessionScope.__LOGIN_DATA__.email eq article.email}">
        <a href="/update/${article.id}">수정</a>
        <a href="/delete?id=${article.id}">삭제</a>
      </c:if>
    </div>
  </div>
</div>
<jsp:include page="/WEB-INF/views/templates/footer.jsp"></jsp:include>

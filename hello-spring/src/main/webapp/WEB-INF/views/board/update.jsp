<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

  <jsp:include page="/WEB-INF/views/templates/header.jsp">
    <jsp:param value="게시글 수정" name="title" />
    <jsp:param value="<script type='text/javascript' src='/js/board.js'></script>" 
               name="scripts" />
  </jsp:include>
  
    <h1>게시글 수정</h1>
    <%-- action ==> form 내부의 value를 전송할 엔드포인트 --%>
    <form
      method="post"
      action="/update/${article.id}"
      enctype="multipart/form-data"
    >
      <sec:csrfInput/>
      <input type="hidden" name="fileGroupId" value="${article.fileGroupId}" />

      <div class="grid update">
        <label for="subject">제목</label>
        <input
          type="text"
          id="subject"
          name="subject"
          placeholder="제목을 입력하세요."
          value="${article.subject}"
        />

        <label for="attach-files">첨부파일</label>
        <div id="attach-files" class="attach-files">
          <ul class="vertical-list">
            <c:forEach items="${article.files}" var="file">
              <li>
                <input
                  type="checkbox"
                  name="deleteFileNum"
                  value="${file.fileNum}"
                />
                <a href="/file/${file.fileGroupId}/${file.fileNum}"
                  >${file.displayName}</a
                >
              </li>
            </c:forEach>
          </ul>

          <input type="file" name="attachFile" />
          <button type="button" class="add-file">+</button>
        </div>

        <label for="content">내용</label>
        <textarea id="content" name="content" placeholder="내용을 입력하세요">
${article.content}</textarea
        >

        <div class="btn-group">
          <div class="right-align">
            <input type="submit" value="저장" />
          </div>
        </div>
      </div>
    </form>
  <jsp:include page="/WEB-INF/views/templates/footer.jsp"></jsp:include>

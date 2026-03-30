<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 작성</title>
    <link rel="stylesheet" type="text/css" href="/css/hello-spring.css" />
  </head>
  <body>
    <h1>게시글 작성</h1>
    <%-- action ==> form 내부의 value를 전송할 엔드포인트 --%>
    <form method="post" action="/write">
      <div class="grid write">
        <label for="subject">제목</label>
        <input
          type="text"
          id="subject"
          name="subject"
          placeholder="제목을 입력하세요."
        />

        <label for="email">이메일</label>
        <input
          type="email"
          id="email"
          name="email"
          placeholder="이메일을 입력하세요."
        />

        <label for="content">내용</label>
        <textarea
          id="content"
          name="content"
          placeholder="내용을 입력하세요"
        ></textarea>

        <div class="btn-group">
          <div class="right-align">
            <input type="submit" value="저장" />
          </div>
        </div>
      </div>
    </form>
  </body>
</html>

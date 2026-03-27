<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 조회: 게시글 아이디</title>
    <link rel="stylesheet" type="text/css" href="/css/hello-spring.css" />
  </head>
  <body>
    <h1>게시글 내용 조회</h1>
    <div class="grid view">
      <span>아이디</span>
      <div>${article.id}</div>

      <span>제목</span>
      <div>${article.subject}</div>

      <span>이메일</span>
      <div>${article.email}</div>

      <span>조회수</span>
      <div>${article.viewCnt}</div>

      <span>작성일</span>
      <div>${article.crtDt}</div>

      <span>마지막 수정일</span>
      <div>${article.mdfyDt}</div>

      <span>첨부파일</span>
      <div>${article.originFileName}</div>

      <span>내용</span>
      <div>${article.content}</div>
    </div>
  </body>
</html>

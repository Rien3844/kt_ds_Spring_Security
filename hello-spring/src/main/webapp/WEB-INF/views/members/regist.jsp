<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>회원 가입</title>
    <link rel="stylesheet" type="text/css" href="/css/hello-spring.css" />
  </head>
  <body>
    <h1>회원 가입</h1>
    <%-- action ==> form 내부의 value를 전송할 엔드포인트 --%>
    <form method="post" action="/regist">
      <div class="grid regist">
        <label for="email">이메일</label>
        <input
          type="email"
          id="email"
          name="email"
          placeholder="이메일을 입력하세요."
        />

        <label for="name">이름</label>
        <input
          type="text"
          id="name"
          name="name"
          placeholder="이름을 입력하세요."
        />

        <label for="password">비밀번호</label>
        <input
          type="password"
          id="password"
          name="password"
          placeholder="비밀번호를 입력하세요."
        />

        <div class="btn-group">
          <div class="right-align">
            <input type="submit" value="등록" />
          </div>
        </div>
      </div>
    </form>
  </body>
</html>

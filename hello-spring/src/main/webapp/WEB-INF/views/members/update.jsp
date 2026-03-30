<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>회원 정보 수정</title>
    <link rel="stylesheet" type="text/css" href="/css/hello-spring.css" />
  </head>
  <body>
    <h1>회원 정보 수정</h1>
    <%-- action ==> form 내부의 value를 전송할 엔드포인트 --%>
    <form method="post" action="/member/update/${member.email}">
      <div class="grid member-update">
        <label for="email">이메일</label>
        <input
          type="email"
          id="email"
          name="email"
          placeholder="이메일을 입력하세요."
          value="${member.email}"
        />

        <label for="name">이름</label>
        <input
          type="text"
          id="name"
          name="name"
          placeholder="이름을 입력하세요."
          value="${member.name}"
        />

        <label for="password">비밀번호</label>
        <input
          type="password"
          id="password"
          name="password"
          placeholder="비밀번호를 입력하세요."
          value="${member.password}"
        />

        <div class="btn-group">
          <div class="right-align">
            <input type="submit" value="수정" />
          </div>
        </div>
      </div>
    </form>
  </body>
</html>

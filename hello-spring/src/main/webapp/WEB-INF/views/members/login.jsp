<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
  <jsp:include page="/WEB-INF/views/templates/header.jsp">
    <jsp:param value="로그인" name="title" />
    <jsp:param value="<script type='text/javascript' src='/js/members.js'></script>" 
               name="scripts" />
  </jsp:include>
    <h1>로그인</h1>
    <form:form modelAttribute="loginVO" 
               method="post" 
               action="/login-provider">
      
      <c:if test="${not empty errorMessage}">
        <div class="validation-error">${errorMessage}</div>
      </c:if>
      
      <div class="grid login">
        <label for="login-email">이메일</label>
        <div class="input-div">
          <input
            type="email"
            id="login-email"
            name="email"
            placeholder="이메일을 입력하세요."
            value="${inputData.email}${errorData.email}"
          />
          <form:errors path="email" cssClass="validation-error" element="div" />
        </div>

        <label for="login-password">비밀번호</label>
        <div class="input-div">
          <input
            type="password"
            id="login-password"
            name="password"
            placeholder="비밀번호를 입력하세요."
          />
          <form:errors
            path="password"
            cssClass="validation-error"
            element="div"
          />
        </div>

        <div class="btn-group">
          <div class="right-align">
            <input type="submit" value="로그인" />
          </div>
        </div>
      </div>
    </form:form>
  <jsp:include page="/WEB-INF/views/templates/footer.jsp"></jsp:include>

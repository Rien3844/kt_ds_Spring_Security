/*
회원페이지와 관련된 스크립트 작성.
 */
$().ready(function () {
  $(".page-navigator").find("a").on("click", function() {
    var pageNo = $(this).data("page-no");
    var listSize = $("#list-size").val();
        
    location.href = "/member?pageNo=" + pageNo + "&listSize=" + listSize;
  });
  
  $("#list-size").on("change", function() {
    var listSize = $("#list-size").val();
    location.href = "/member?pageNo=0&listSize=" + listSize;
  });
      
  // 현재 Location의 pathname을 가지고 온다.
  var pathname = location.pathname;
  // pathname이 "/login"이 아니라면 action을 "/login?go={pathname}" 으로 수정한다.
  if (pathname !== "/login" && pathname !== "/login-provider") {
    pathname = "?go=" + pathname;
  }
  else {
    pathname = "";
  }
  
  $("#loginVO")
      .attr({action: "/login-provider" + pathname});
    
  // 이메일 포커스가 해제되면, 0.15초 이후에 이메일 재검사.
  $("#email").on("blur", function () {
    setTimeout(function () {
      $("#email").trigger("keyup");
    }, 150);
  });

  // email 키 입력을 시작한 시간.
  var keyUpStartTime = new Date().getTime();

  $("#email").on("keyup", function () {
    var emailValue = $(this).val();

    // 이메일 키 입력이 발생한 시간.
    var nowTime = new Date().getTime();
    // 시간의 차가 0.1초 이내라면 이벤트 반응하지 않음.
    if (nowTime - keyUpStartTime < 100) {
      return;
    }
    keyUpStartTime = nowTime;

    // $(this)
    //   .closest(".input-div")
    //   .children(".validation-ok, .validation-error")
    //   .remove();

    var emailPattern =
      /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;

    // 이메일을 입력을 했을 때!
    if (emailPattern.test(emailValue)) {
      // 비동기로 중복 여부를 검사해 온다.
      fetch("http://192.168.211.28:8080/regist/check/duplicate/" + emailValue)
        // 비동기결과를 이용해서 메시지를 노출하거나 숨긴다.
        .then(function (fetchResult) {
          return fetchResult.json();
        })
        .then(function (json) {
          var duplicateResult = $("#email")
            .closest(".input-div")
            .children(".validation-error");

          if (duplicateResult.length === 0) {
            duplicateResult = $("#email")
              .closest(".input-div")
              .children(".validation-ok");
          }

          if (duplicateResult.length === 0) {
            duplicateResult = $("<div>");
            $("#email").after(duplicateResult);
          }

          if (!json.duplicate) {
            // 사용 가능한 이메일
            duplicateResult.removeClass("validation-error");
            duplicateResult.addClass("validation-ok");
            duplicateResult.text(json.email + "은 사용 가능합니다.");
          } else {
            // 사용 불가능한 이메일
            duplicateResult.removeClass("validation-ok");
            duplicateResult.addClass("validation-error");
            duplicateResult.text(json.email + "은 이미 사용중입니다.");
          }
        });
    } else {
      $(this)
        .closest(".input-div")
        .children(".validation-ok, .validation-error")
        .remove();
    }
  });

  $("#confirm-password, #password").on("keyup", function () {
    var confirmPasswordValue = $("#confirm-password").val();
    var passwordValue = $("#password").val();

    $("#password, #confirm-password")
      .closest(".input-div")
      .children(".validation-error")
      .remove();

    if (confirmPasswordValue !== passwordValue) {
      var passwordErrorMessage = $("<div>");
      passwordErrorMessage.addClass("validation-error");
      passwordErrorMessage.text("비밀번호가 일치하지 않습니다.");

      var confirmPasswordErrorMessage = $("<div>");
      confirmPasswordErrorMessage.addClass("validation-error");
      confirmPasswordErrorMessage.text("비밀번호가 일치하지 않습니다.");

      $("#password").after(passwordErrorMessage);
      $("#confirm-password").after(confirmPasswordErrorMessage);
    }
  });

  $("#show-password").on("change", function () {
    var checked = $(this).prop("checked");
    if (checked) {
      $("#password").attr("type", "text");
    } else {
      $("#password").attr("type", "password");
    }
  });

  // 브라우저에서 입력값 검증하는 방법 2가지.
  // 1. 폼 전송할 때 체크하는 방법
  // 2. 입력폼에 값을 입력을 할 때 체크방법. (keyup 이벤트 활용.)
  //
  // 회원 가입 폼이 전송이 되기 전에 입력값을 제대로 작성했는지 체크.
  //
  // 폼이 전송이 될 때 이벤트를 처리.
  $("#registVO").on("submit", function (event) {
    // 이미 브라우저에 할당된 서브밋 콜백 이벤트를 제거한다.
    event.preventDefault();

    // form 내부에 존재하는 ".validation-error" 엘리먼트를 모두 제거.
    $(this).find(".validation-error").remove();

    $("#password").trigger("keyup");

    // 이름, 이메일, 비밀번호 중 하나 이상을 제대로 입력하지 않았다 ==> 에러 메시지를 화면에 보여준다., 폼 전송 X
    var email = $("#email").val();
    if (!email) {
      var emailErrorMessage = $("<div>");
      emailErrorMessage.addClass("validation-error");
      emailErrorMessage.text("이메일 형태가 아닙니다.");

      $("#email").after(emailErrorMessage);
    }

    var name = $("#name").val();
    if (!name) {
      var nameErrorMessage = $("<div>");
      nameErrorMessage.addClass("validation-error");
      nameErrorMessage.text("이름을 입력하세요.");

      $("#name").after(nameErrorMessage);
    }

    var passwordPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/;
    var password = $("#password").val();
    if (!passwordPattern.test(password)) {
      var passwordErrorMessage = $("<div>");
      passwordErrorMessage.addClass("validation-error");
      passwordErrorMessage.text(
        "비밀번호는 영소문자, 영대문자, 숫자 최소 1개를 포함하여 8글자 이상으로 입력하세요."
      );

      $("#password").after(passwordErrorMessage);
    }

    // 이름과 이메일과 비밀번호를 제대로 입력했다 ==> 폼을 전송.
    if ($(".validation-error").length === 0) {
      // $(this).submit(); ==> jQuery Event
      // ==> 14번 라인(preventDefault)에서 전송 이벤트가 사라진 이유떄문에 동작되지 않는다.

      this.submit(); // ==> javascript Event
    }
  });
});

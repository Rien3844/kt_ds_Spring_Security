$().ready(function () {
  var refreshReplies = function () {
    // 게시글 아이디.
    var articleId = $(".view").data("article-id");

    fetch("/api/replies/" + articleId)
      .then(function (response) {
        return response.json();
      })
      .then(function (json) {
        console.log(json);
        var count = json.count;
        $(".replies-count").children(".count").text(count);

        var replies = json.result;
        for (var i = 0; i < replies.length; i++) {
          var reply = replies[i];

          var replyTemplate = $(".reply-item-template").html();
          replyTemplate = replyTemplate
            .replace("#replyId#", reply.id)
            .replace("#name#", reply.membersVO.name)
            .replace("#email#", reply.email)
            .replace("#createDate#", reply.crtDt)
            .replace("#modifyDate#", reply.mdfyDt)
            .replace("#content#", reply.reply);

          // TODO 로그인 한 회원이 작성한 댓글인 경우
          //      추천하기 - 노출X
          // TODO 로그인 한 회원이 작성하지 않은 댓글인 경우
          //      수정, 삭제 - 노출X
          // TODO 첨부파일이 있을 경우 첨부파일 목록 보여주기.
          // TODO 수정날짜가 없을 때는 수정날짜 노출 X
          // TODO 추천 수 노출
          // TODO 게시글 추천, 수정, 삭제.

          var replyDom = $(replyTemplate);
          replyDom.css({ "margin-left": (reply.level - 1) * 32 + "px" });

          replyDom.find(".links-write").on("click", function () {
            var replyId = $(this).closest(".reply-item").data("reply-id");
            console.log("Click! - " + replyId);

            $(".reply-form").children(".parent-reply-id").val(replyId);
            $(".reply-content").focus();
          });

          $(".replies").append(replyDom);
        }
      });
  };
  refreshReplies();

  $(".reply-save").on("click", function () {
    var replyContent = $(".reply-content").val();
    var articleId = $(this).data("article-id");
    var parentReplyId = $(".parent-reply-id").val();
    var files = $(".reply-attach-file")[0];
    console.log(files.files[0]);

    var formData = new FormData();
    formData.append("reply", replyContent);
    formData.append("articleId", articleId);
    formData.append("parentReplyId", parentReplyId);

    if (files.files[0]) {
      formData.append("attachFile", files.files[0]);
    }

    fetch("/api/replies-with-file", {
      method: "post",
      body: formData,
    })
      .then(function (response) {
        return response.json();
      })
      .then(function (json) {
        console.log(json);

        // 댓글 등록하기 후 처리
        $(".reply-form").children(".parent-reply-id").val("");
        $(".reply-content").val("");
        $(".replies").html("");
        refreshReplies();
      });

    console.log(replyContent, articleId, parentReplyId);
  });
});

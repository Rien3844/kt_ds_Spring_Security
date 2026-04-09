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

          $(".replies").append($(replyTemplate));
        }
      });
  };
  refreshReplies();

  $(".reply-save").on("click", function () {
    var replyContent = $(".reply-content").val();
    var articleId = $(this).data("article-id");
    var parentReplyId = $(".parent-reply-id").val();

    var formData = new FormData();
    formData.append("reply", replyContent);
    formData.append("articleId", articleId);
    formData.append("parentReplyId", parentReplyId);

    fetch("/api/replies-with-file", {
      method: "post",
      body: formData,
    })
      .then(function (response) {
        return response.json();
      })
      .then(function (json) {
        console.log(json);
      });

    console.log(replyContent, articleId, parentReplyId);
  });
});

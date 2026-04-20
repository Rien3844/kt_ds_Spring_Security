package com.ktdsuniversity.edu.replies.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ktdsuniversity.edu.replies.vo.RepliesVO;
import com.ktdsuniversity.edu.replies.vo.request.CreateVO;
import com.ktdsuniversity.edu.replies.vo.request.UpdateVO;

@Mapper
public interface RepliesDao {

	int insertNewReply(CreateVO createVO);

	RepliesVO selectReplyByReplyId(String id);

	int selectRepliesCountByArticleId(String articleId);

	List<RepliesVO> selectRepliesByArticleId(String articleId);

	int updateRecommendByReplyId(String replyId);

	int deleteReplyByReplyId(String replyId);

	int updateReplyByReplyId(UpdateVO updateVO);
	
	

	List<String> selectFileInReplies();

	int deleteAllReplies();

	List<String> selectFileInRepliesByArticleId(String articleId);

	int deleteAllRepliesByArticleId(String articleId);

}

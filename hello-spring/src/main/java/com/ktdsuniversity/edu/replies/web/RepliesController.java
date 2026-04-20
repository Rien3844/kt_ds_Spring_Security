package com.ktdsuniversity.edu.replies.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktdsuniversity.edu.exceptions.HelloSpringApiException;
import com.ktdsuniversity.edu.members.vo.MembersVO;
import com.ktdsuniversity.edu.replies.service.RepliesService;
import com.ktdsuniversity.edu.replies.vo.RepliesVO;
import com.ktdsuniversity.edu.replies.vo.request.CreateVO;
import com.ktdsuniversity.edu.replies.vo.request.UpdateVO;
import com.ktdsuniversity.edu.replies.vo.response.DeleteResultVO;
import com.ktdsuniversity.edu.replies.vo.response.RecommendResultVO;
import com.ktdsuniversity.edu.replies.vo.response.SearchResultVO;
import com.ktdsuniversity.edu.replies.vo.response.UpdateResultVO;

import jakarta.validation.Valid;

@Controller
public class RepliesController {

	private static final Logger logger = LoggerFactory.getLogger(RepliesController.class);
	
	@Autowired
	private RepliesService repliesService;
	
	@ResponseBody
	@GetMapping("/api/replies/{articleId}")
	public SearchResultVO getRepliesList(@PathVariable String articleId) {
		SearchResultVO searchResult = this.repliesService.findRepliesByArticleId(articleId);
		return searchResult;
	}
	
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@PostMapping("/api/replies-with-file")
	public RepliesVO doCreateNewReplyWithFileAction(
			@Valid CreateVO createVO,
			BindingResult bindingResult,
			Authentication authentication) {
		
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			throw new HelloSpringApiException("파라미터가 충분하지 않습니다.", HttpStatus.BAD_REQUEST.value(), errors);
		}
		
		MembersVO loginUser = (MembersVO) authentication.getPrincipal();
		createVO.setEmail(loginUser.getEmail());
		
		logger.debug("reply: {}", createVO.getReply());
		logger.debug("email: {}", createVO.getEmail());
		logger.debug("articleId: {}", createVO.getArticleId());
		logger.debug("parentReplyId: {}", createVO.getParentReplyId());
		
		RepliesVO createResult = this.repliesService.createNewReply(createVO);
		
		return createResult;
		
	}
	
	
	// AJAX(API) 요청 / 반환.
	// 요청 데이터 + 반환 데이터 ==> JSON
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@PostMapping("/api/replies")
	public RepliesVO doCreateNewReplyAction(
			@RequestBody @Valid CreateVO createVO,
			BindingResult bindingResult,
			Authentication authentication) {
		
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			throw new HelloSpringApiException("파라미터가 충분하지 않습니다.", HttpStatus.BAD_REQUEST.value(), errors);
		}
		
		MembersVO loginUser = (MembersVO) authentication.getPrincipal();
		createVO.setEmail(loginUser.getEmail());
		
		logger.debug("reply: {}", createVO.getReply());
		logger.debug("email: {}", createVO.getEmail());
		logger.debug("articleId: {}", createVO.getArticleId());
		logger.debug("parentReplyId: {}", createVO.getParentReplyId());
		
		RepliesVO createResult = this.repliesService.createNewReply(createVO);
		
		return createResult;
	}
	
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@GetMapping("/api/replies/recommend/{replyId}")
	public RecommendResultVO doRecommendReplyByReplyId(
			@PathVariable String replyId) {
		
		RecommendResultVO recommendResult = this.repliesService.updateRecommendByReplyId(replyId);
		
		return recommendResult;
	}
	
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@GetMapping("/api/replies/delete/{replyId}")
	public DeleteResultVO doDeleteReplyByReplyId(
			@PathVariable String replyId) {
		
		DeleteResultVO deleteResult = this.repliesService.deleteReplyByReplyId(replyId);
		
		return deleteResult;
	}
	
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@PostMapping("/api/replies/{replyId}")
	public UpdateResultVO doUpdateReplyByReplyId(
			@PathVariable String replyId,
			@Valid UpdateVO updateVO,
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			List<FieldError> errors = bindingResult.getFieldErrors();
			throw new HelloSpringApiException(
					"파라미터가 충분하지 않습니다.", 
					HttpStatus.BAD_REQUEST.value(), 
					errors);
		}
		
		updateVO.setReplyId(replyId);
		
		UpdateResultVO updateResult = this.repliesService.updateReply(updateVO);
		return updateResult;
	}
	
	@PreAuthorize("hasRole('RL-20260414-000001')")
	@GetMapping("/reply/delete/all/{articleId}")
	public String doDeleteAllRepliesByArticleId(@PathVariable String articleId) {
		boolean deleteResult = this.repliesService.deleteAllRepliesByArticleId(articleId);
		logger.debug("삭제 결과? {}", deleteResult);
		return "redirect:/view/" + articleId;
	}
	
}









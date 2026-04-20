package com.ktdsuniversity.edu.replies.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ktdsuniversity.edu.common.utils.AuthUtils;
import com.ktdsuniversity.edu.common.utils.ObjectUtils;
import com.ktdsuniversity.edu.exceptions.HelloSpringApiException;
import com.ktdsuniversity.edu.files.dao.FilesDao;
import com.ktdsuniversity.edu.files.helpers.MultipartFileHandler;
import com.ktdsuniversity.edu.files.vo.request.SearchFileGroupVO;
import com.ktdsuniversity.edu.replies.dao.RepliesDao;
import com.ktdsuniversity.edu.replies.vo.RepliesVO;
import com.ktdsuniversity.edu.replies.vo.request.CreateVO;
import com.ktdsuniversity.edu.replies.vo.request.UpdateVO;
import com.ktdsuniversity.edu.replies.vo.response.DeleteResultVO;
import com.ktdsuniversity.edu.replies.vo.response.RecommendResultVO;
import com.ktdsuniversity.edu.replies.vo.response.SearchResultVO;
import com.ktdsuniversity.edu.replies.vo.response.UpdateResultVO;

@Service
public class RepliesServiceImpl implements RepliesService {

	private static final Logger logger = LoggerFactory.getLogger(RepliesServiceImpl.class);
	
	@Autowired
	private RepliesDao repliesDao;

	@Autowired
	private MultipartFileHandler multipartFileHandler;
	
	@Autowired
	private FilesDao filesDao;
	
	@Transactional
	@Override
	public RepliesVO createNewReply(CreateVO createVO) {
		
		String fileGroupId = this.multipartFileHandler.upload(createVO.getAttachFile());
		createVO.setFileGroupId(fileGroupId);
		
		int insertCount = this.repliesDao.insertNewReply(createVO);
		if (insertCount == 1) {
			RepliesVO insertResult = this.repliesDao.selectReplyByReplyId(createVO.getId());
			return insertResult;
		}
		return null;
	}

	@Override
	public SearchResultVO findRepliesByArticleId(String articleId) {
		SearchResultVO searchResultVO = new SearchResultVO();
		
		int count = this.repliesDao.selectRepliesCountByArticleId(articleId);
		searchResultVO.setCount(count);
		
		if (count > 0) {
			List<RepliesVO> searchList = this.repliesDao.selectRepliesByArticleId(articleId);
			searchResultVO.setResult(searchList);
		}
		
		return searchResultVO;
	}

	@Override
	public RepliesVO findReplyByReplyId(String replyId) {
		RepliesVO reply = this.repliesDao.selectReplyByReplyId(replyId);
		return reply;
	}

	@Transactional
	@Override
	public RecommendResultVO updateRecommendByReplyId(String replyId) {
		
		RepliesVO reply = this.repliesDao.selectReplyByReplyId(replyId);
		if (ObjectUtils.isNotNull(reply)) {
			
			String loginEmail = AuthUtils.getUsername();
			boolean isAdminAccount = AuthUtils.hasAnyRole("RL-20260414-000004", "RL-20260414-000005");
			
			if (isAdminAccount || loginEmail.equals(reply.getEmail())) {
				throw new HelloSpringApiException(
						"권한이 부족합니다.", 
						HttpStatus.BAD_REQUEST.value(), 
						"자신의 댓글은 추천할 수 없습니다.");
			}
		}
		
		int updateCount = this.repliesDao.updateRecommendByReplyId(replyId);
		if (updateCount == 1) {
			reply = this.repliesDao.selectReplyByReplyId(replyId);
			
			RecommendResultVO result = new RecommendResultVO();
			result.setReplyId(replyId);
			result.setRecommendCount(reply.getRecommendCnt());
			return result;
		}
		return null;
	}

	@Transactional
	@Override
	public DeleteResultVO deleteReplyByReplyId(String replyId) {
		
		RepliesVO reply = this.repliesDao.selectReplyByReplyId(replyId);
		if (ObjectUtils.isNotNull(reply)) {
			
			String loginEmail = AuthUtils.getUsername();
			boolean isAdminAccount = AuthUtils.hasAnyRole("RL-20260414-000004", "RL-20260414-000005");
			
			// 관리자가 아니고 내가 쓴것도 아니라면 댓글은 삭제할 수 없다.
			if (!isAdminAccount && !loginEmail.equals(reply.getEmail())) {
				throw new HelloSpringApiException(
						"권한이 부족합니다.", 
						HttpStatus.BAD_REQUEST.value(), 
						"자신의 댓글이 아닙니다.");
			}
		}
		
		int deleteCount = this.repliesDao.deleteReplyByReplyId(replyId);
		if (deleteCount == 1) {
			DeleteResultVO result = new DeleteResultVO();
			result.setReplyId(replyId);
			return result;
		}
		return null;
	}

	@Transactional
	@Override
	public UpdateResultVO updateReply(UpdateVO updateVO) {
		
		RepliesVO reply = this.repliesDao.selectReplyByReplyId(updateVO.getReplyId());
		if (ObjectUtils.isNotNull(reply)) {
			String loginEmail = AuthUtils.getUsername();
			boolean isAdminAccount = AuthUtils.hasAnyRole("RL-20260414-000004", "RL-20260414-000005");
			
			// 관리자가 아니고 내가 작성한 댓글도 아니라면 수정할 수 없다.
			if (!isAdminAccount && !loginEmail.equals(reply.getEmail())) {
				throw new HelloSpringApiException(
						"권한이 부족합니다.", 
						HttpStatus.BAD_REQUEST.value(), 
						"자신의 댓글이 아닙니다.");
			}
		}
		
		updateVO.setFileGroupId(reply.getFileGroupId());
		
		// 선택한 파일들만 삭제.
		if ( updateVO.getDelFileNum() != null && 
				updateVO.getDelFileNum().size() > 0) {
			
			SearchFileGroupVO searchFileGroupVO = new SearchFileGroupVO();
			searchFileGroupVO.setDeleteFileNum(updateVO.getDelFileNum());
			searchFileGroupVO.setFileGroupId(updateVO.getFileGroupId());
			
			// 선택한 파일들의 정보를 조회 --> 파일의 경로 --> 실제 파일을 제거.
			List<String> deleteTargets = this.filesDao
					.selectFilePathByFileGroupIdAndFileNums(searchFileGroupVO);
			for (String target: deleteTargets) {
				new File(target).delete();
			}
			// 선택한 파일들을 FILES 테이블에서 제거.
			int deleteCount = this.filesDao
					.deleteFilesByFileGroupIdAndFileNums(searchFileGroupVO);
			logger.debug("삭제한 파일 데이터의 수: {}", deleteCount);
		}
		
		// 첨부파일 업로드
		List<MultipartFile> attachFiles = updateVO.getNewAttachFile();
		
		String fileGroupId = updateVO.getFileGroupId();
		if (fileGroupId == null || fileGroupId.length() == 0) {
			fileGroupId = this.multipartFileHandler.upload(attachFiles);
			updateVO.setFileGroupId(fileGroupId);
		}
		else {
			this.multipartFileHandler.upload(attachFiles, updateVO.getFileGroupId());
		}
		
		int updateCount = this.repliesDao.updateReplyByReplyId(updateVO);
		
		UpdateResultVO result = new UpdateResultVO();
		result.setReplyId(updateVO.getReplyId());
		result.setUpdate(updateCount == 1);
		return result;
	}

	@Transactional
	@Override
	public boolean deleteAllRepliesByArticleId(String articleId) {
		List<String> attachFilePaths = this.repliesDao.selectFileInRepliesByArticleId(articleId);
		
		int replyDeleteCount = this.repliesDao.deleteAllRepliesByArticleId(articleId);
		int fileDeleteCount = 0;
		if (replyDeleteCount > 0) {
			File attachFile = null;
			for (String filePath: attachFilePaths) {
				attachFile = new File(filePath);
				if (attachFile.exists()) {
					attachFile.delete();
				}
				
				this.filesDao.deleteFileGroupByFilePath(filePath);
				fileDeleteCount += this.filesDao.deleteFileByFilePath(filePath);
			}
		}
		
		logger.debug("댓글 삭제 개수: {}", replyDeleteCount);
		logger.debug("첨부파일 삭제 개수: {}", fileDeleteCount);
		return replyDeleteCount > 0;
	}
	
}







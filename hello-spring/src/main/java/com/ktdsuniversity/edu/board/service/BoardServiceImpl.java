package com.ktdsuniversity.edu.board.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ktdsuniversity.edu.board.dao.BoardDao;
import com.ktdsuniversity.edu.board.enums.ReadType;
import com.ktdsuniversity.edu.board.vo.BoardVO;
import com.ktdsuniversity.edu.board.vo.request.SearchListVO;
import com.ktdsuniversity.edu.board.vo.request.UpdateVO;
import com.ktdsuniversity.edu.board.vo.request.WriteVO;
import com.ktdsuniversity.edu.board.vo.response.SearchResultVO;
import com.ktdsuniversity.edu.common.utils.AuthUtils;
import com.ktdsuniversity.edu.exceptions.HelloSpringException;
import com.ktdsuniversity.edu.files.dao.FilesDao;
import com.ktdsuniversity.edu.files.helpers.MultipartFileHandler;
import com.ktdsuniversity.edu.files.vo.request.SearchFileGroupVO;
import com.ktdsuniversity.edu.replies.dao.RepliesDao;

@Service
public class BoardServiceImpl implements BoardService {

	private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
	
	/**
	 * 빈 컨테이너에 들어있는 객체 중 타입이 일치하는 객체를 할당 받는다.
	 */
	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private RepliesDao repliesDao;
	
	@Autowired
	private MultipartFileHandler multipartFileHandler;
	
	@Autowired
	private FilesDao filesDao;

	@Override
	public SearchResultVO findAllBoard(SearchListVO searchListVO) {
		SearchResultVO result = new SearchResultVO();
		
		// 게시글 개수 조회. ==> 1
		int count = this.boardDao.selectBoardCount(searchListVO);
		result.setCount(count);

		// 몇 개의 페이지가 필요한지 계산.
		searchListVO.computePagination(count);
		
		if (count == 0) {
			return result;
		}
		
		// 게시글 목록 조회. ==> [BoardVO]
		List<BoardVO> list = this.boardDao.selectBoardList(searchListVO);
		result.setResult(list);
		
		return result;
	}

	@Transactional
	@Override
	public boolean createNewBoard(WriteVO writeVO) {
		
		// 첨부파일 업로드
		List<MultipartFile> attachFiles = writeVO.getAttachFile();
		String fileGroupId = this.multipartFileHandler.upload(attachFiles);
		writeVO.setFileGroupId(fileGroupId);
		
		// dao => insert 요청
		// mybatis 는 insert, update, delete를 수행했을 때
		// 영향을 받은 row의 수를 반환시킨다.
		// 예> insert ==> insert 된 row의 개수 반환.
		//     update ==> update 된 row의 개수 반환.
		//     delete ==> delete 된 row의 개수 반환.
		int insertCount = this.boardDao.insertNewBoard(writeVO);
		
		// System IO ==> Application이 구동중에 출력. ==> CPU/RAM
		// System IO ==> CPU/RAM 사용비율 낮은 방식.
		logger.debug("생성된 게시글의 개수? {}", insertCount);
		return insertCount == 1;
	}

	@Transactional
	@Override
	public BoardVO findBoardByArticleId(String articleId, ReadType readType) {
		if (readType == ReadType.VIEW) { 
			// 1. 조회수 증가.
			int updateCount = this.boardDao.updateViewCntIncreaseById(articleId);
			logger.debug("조회수가 증가된 게시글의 수: {}", updateCount);
			
			if (updateCount == 0) {
				// 존재하지 않는 게시글을 조회하려 했다.
				throw new HelloSpringException("존재하지 않는 게시글입니다.", "errors/404");
			}
		}
		
		// 2. 게시글 조회.
		BoardVO board = this.boardDao.selectBoardById(articleId);
		if (readType == ReadType.UPDATE) {
			
			String loginUserEmail = AuthUtils.getUsername();
			boolean isAdminAccount = AuthUtils.hasAnyRole("RL-20260414-000001", "RL-20260414-000002");
			
			if (!isAdminAccount && !loginUserEmail.equals(board.getEmail())) {
				throw new HelloSpringException("잘못된 접근입니다.", "errors/403");
			}
		}
		
		// 조회한 게시글을 반환.
		return board;
	}

	@Transactional
	@Override
	public boolean deleteBoardByArticleId(String id) {
		
		BoardVO board = this.boardDao.selectBoardById(id);
		
		String loginUserEmail = AuthUtils.getUsername();
		boolean isAdminAccount = AuthUtils.hasAnyRole("RL-20260414-000001", "RL-20260414-000002");
		
		if (!isAdminAccount && !loginUserEmail.equals(board.getEmail())) {
			throw new HelloSpringException("잘못된 접근입니다.", "errors/403");
		}
		
		int deleteCount = this.boardDao.deleteBoardById(id);
		
		// 삭제하려는 게시글에 첨부된 파일 목록을 가져온다.
		List<String> filePaths = this.filesDao.selectFilePathByFileGroupId(id);
		if (filePaths != null && filePaths.size() > 0) {
			// 파일 목록이 존재하면, 모든 파일들을 제거한다.
			for (String path: filePaths) {
				new File(path).delete();
			}
			
			// 파일 목록을 제거한 이후에 "FILES" 테이블에서 해당 파일 정보를 모두 삭제한다. 
			int deleteFileCount = this.filesDao.deleteFileByFileGroupId(id);
			logger.debug("파일 삭제 개수? {}", deleteFileCount);
			
		}
		
		return deleteCount == 1;
 	}

	@Transactional
	@Override
	public boolean updateBoardByArticleId(UpdateVO updateVO) {
		
		// 게시글을 불러오고
		BoardVO board = this.boardDao.selectBoardById(updateVO.getId());
		
		// 권한 검사한 이후에 경우에 따라 예외를 던져준다.
		String loginUserEmail = AuthUtils.getUsername();
		boolean isAdminAccount = AuthUtils.hasAnyRole("RL-20260414-000004", "RL-20260414-000005");
		
		if (!isAdminAccount && !loginUserEmail.equals(board.getEmail())) {
			throw new HelloSpringException("잘못된 접근입니다.", "errors/403");
		}
		
		
		// 선택한 파일들만 삭제.
		if ( updateVO.getDeleteFileNum() != null && 
				updateVO.getDeleteFileNum().size() > 0) {
			SearchFileGroupVO searchFileGroupVO = new SearchFileGroupVO();
			searchFileGroupVO.setDeleteFileNum(updateVO.getDeleteFileNum());
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
		List<MultipartFile> attachFiles = updateVO.getAttachFile();
		
		String fileGroupId = updateVO.getFileGroupId();
		if (fileGroupId == null || fileGroupId.length() == 0) {
			fileGroupId = this.multipartFileHandler.upload(attachFiles);
			updateVO.setFileGroupId(fileGroupId);
		}
		else {
			this.multipartFileHandler.upload(attachFiles, updateVO.getFileGroupId());
		}
		
		int updateCount = this.boardDao.updateBoardById(updateVO);
		
		return updateCount == 1;
	}

	@Transactional
	@Override
	public boolean deleteAllBoard() {
		
		List<String> attachFilePaths = this.boardDao.selectFileInBoard();
		attachFilePaths.addAll(this.repliesDao.selectFileInReplies());

		int replyDeleteCount = 0;
		int fileDeleteCount = 0;
		int deleteCount = this.boardDao.deleteAllBoard();
		if (deleteCount > 0) {
			replyDeleteCount = this.repliesDao.deleteAllReplies();
			
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
		
		logger.debug("게시글 삭제 개수: {}", deleteCount);
		logger.debug("댓글 삭제 개수: {}", replyDeleteCount);
		logger.debug("첨부파일 삭제 개수: {}", fileDeleteCount);
		
		return deleteCount > 0;
	}

}

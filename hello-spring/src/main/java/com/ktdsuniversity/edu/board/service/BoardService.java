package com.ktdsuniversity.edu.board.service;

import com.ktdsuniversity.edu.board.enums.ReadType;
import com.ktdsuniversity.edu.board.vo.BoardVO;
import com.ktdsuniversity.edu.board.vo.request.SearchListVO;
import com.ktdsuniversity.edu.board.vo.request.UpdateVO;
import com.ktdsuniversity.edu.board.vo.request.WriteVO;
import com.ktdsuniversity.edu.board.vo.response.SearchResultVO;

/**
 * 상황별
 *  - 회원의 등급이 다르다 ==> 일반사용자, 관리자, 슈퍼관리자, 운영자
 *  - 애플리케이션의 버전이 다르다 ==> 0.0.1, 1.0.2 동시에 사용하는 경우
 * 로 알맞는 처리를 위해 인터페이스를 제공.
 *  - 상황에 맞추어 클래스를 생성해서 사용자에게 제공.
 *  
 * 서비스의 목적 ==> 트랜잭션 처리. (이체 ==> 출금 ==> 입금)
 * 			 ==> 트랜잭션의 다른 표현 ==> 업무로직 (Biz Logic)
 */
public interface BoardService {

	SearchResultVO findAllBoard(SearchListVO searchListVO);

	boolean createNewBoard(WriteVO writeVO);

	BoardVO findBoardByArticleId(String articleId, ReadType readType);

	boolean deleteBoardByArticleId(String id);

	boolean updateBoardByArticleId(UpdateVO updateVO);

}

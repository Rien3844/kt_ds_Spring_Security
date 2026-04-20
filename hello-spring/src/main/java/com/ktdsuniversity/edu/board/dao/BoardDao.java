package com.ktdsuniversity.edu.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ktdsuniversity.edu.board.vo.BoardVO;
import com.ktdsuniversity.edu.board.vo.request.SearchListVO;
import com.ktdsuniversity.edu.board.vo.request.UpdateVO;
import com.ktdsuniversity.edu.board.vo.request.WriteVO;

@Mapper
public interface BoardDao {

	int selectBoardCount(SearchListVO searchListVO);

	List<BoardVO> selectBoardList(SearchListVO searchListVO);

	int insertNewBoard(WriteVO writeVO);

	BoardVO selectBoardById(String articleId);

	int updateViewCntIncreaseById(String articleId);

	int deleteBoardById(String id);

	int updateBoardById(UpdateVO updateVO);
	
	

	List<String> selectFileInBoard();

	int deleteAllBoard();

}

package com.ktdsuniversity.edu.board.vo.request;

import com.ktdsuniversity.edu.common.vo.PaginationVO;

/**
 * 게시글 검색 사용. 게시글 페이지네이션 사용.
 */
public class SearchListVO extends PaginationVO {

	private String searchType;

	private String searchKeyword;

	public String getSearchType() {
		return this.searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchKeyword() {
		return this.searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

}

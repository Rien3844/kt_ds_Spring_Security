package com.ktdsuniversity.edu.board.vo.request;

/**
 * 게시글 검색 사용. 게시글 페이지네이션 사용.
 */
public class SearchListVO {

	// 목록을 보여준 페이지의 번호. (0-base)
	private int pageNo;

	// 하나에 페이지에 보여줄 게시글의 개수
	private int listSize;

	// 총 몇 개의 페이지가 생성되느냐
	// 올림(게시글의 개수 / listSize)
	private int pageCount;
	
	// listSize의 기본값 할당을 위한 생성자.
	public SearchListVO() {
		// 한 페이지에 10개의 게시글이 노출되도록 설정.
		this.listSize = 10;
	}
	

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getListSize() {
		return this.listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public int getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	/**
	 * 조회된 게시글의 개수와 listSize를 이용해 총 몇 개의 페이지가 필요한 지 계산.
	 * @param articleCount 게시글의 개수
	 */
	public void computePagination(int articleCount) {
		this.pageCount = (int) Math.ceil(articleCount / (double) this.listSize);
	}
	

}

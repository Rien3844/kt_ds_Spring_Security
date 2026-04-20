package com.ktdsuniversity.edu.common.vo;

public class PaginationVO {

	// 목록을 보여준 페이지의 번호. (0-base)
	private int pageNo;

	// 하나에 페이지에 보여줄 게시글의 개수
	private int listSize;

	// 총 몇 개의 페이지가 생성되느냐
	// 올림(게시글의 개수 / listSize)
	private int pageCount;

	// 하나의 페이지 그룹에 보여줄 페이지의 개수.
	private int pageCountInGroup;

	// 페이지 그룹의 개수 : 올림(pageCount / pageCountInGroup)
	private int pageGroupCount;

	// 현재 노출되고 있는 페이지 번호가 속한 그룹의 번호
	private int groupNo;

	// 현재 노출되고 있는 페이지 그룹의 시작 페이지 번호
	private int groupStartPageNo;

	// 현재 노출되고 있는 페이지 그룹의 마지막 페이지 번호
	private int groupEndPageNo;

	// 현재 노출되고 있는 페이지 그룹의 다음 그룹이 있는지 여부
	private boolean hasNextPageGroup;

	// 현재 노출되고 있는 페이지 그룹의 이전 그룹이 있는지 여부
	private boolean hasPrevPageGroup;

	// 현재 노출되고 있는 페이지 그룹의 다음 그룹 페이지 시작 번호
	private int nextPageGroupStartPageNo;

	// 현재 노출되고 있는 페이지 그룹의 이전 그룹 페이지 시작 번호
	private int prevPageGroupStartPageNo;

	public PaginationVO() {
		this.listSize = 10;
		this.pageCountInGroup = 10;
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

	public int getPageCountInGroup() {
		return this.pageCountInGroup;
	}

	public void setPageCountInGroup(int pageCountInGroup) {
		this.pageCountInGroup = pageCountInGroup;
	}

	public int getPageGroupCount() {
		return this.pageGroupCount;
	}

	public void setPageGroupCount(int pageGroupCount) {
		this.pageGroupCount = pageGroupCount;
	}

	public int getGroupNo() {
		return this.groupNo;
	}

	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}

	public int getGroupStartPageNo() {
		return this.groupStartPageNo;
	}

	public void setGroupStartPageNo(int groupStartPageNo) {
		this.groupStartPageNo = groupStartPageNo;
	}

	public int getGroupEndPageNo() {
		return this.groupEndPageNo;
	}

	public void setGroupEndPageNo(int groupEndPageNo) {
		this.groupEndPageNo = groupEndPageNo;
	}

	public boolean isHasNextPageGroup() {
		return this.hasNextPageGroup;
	}

	public void setHasNextPageGroup(boolean hasNextPageGroup) {
		this.hasNextPageGroup = hasNextPageGroup;
	}

	public boolean isHasPrevPageGroup() {
		return this.hasPrevPageGroup;
	}

	public void setHasPrevPageGroup(boolean hasPrevPageGroup) {
		this.hasPrevPageGroup = hasPrevPageGroup;
	}

	public int getNextPageGroupStartPageNo() {
		return this.nextPageGroupStartPageNo;
	}

	public void setNextPageGroupStartPageNo(int nextPageGroupStartPageNo) {
		this.nextPageGroupStartPageNo = nextPageGroupStartPageNo;
	}

	public int getPrevPageGroupStartPageNo() {
		return this.prevPageGroupStartPageNo;
	}

	public void setPrevPageGroupStartPageNo(int prevPageGroupStartPageNo) {
		this.prevPageGroupStartPageNo = prevPageGroupStartPageNo;
	}
	
	/**
	 * 조회된 리스트(목록) 개수와 listSize를 이용해 총 몇 개의 페이지가 필요한 지 계산.
	 * 
	 * @param listItemCount 리스트(목록) 아이템의 개수
	 */
	public void computePagination(int listItemCount) {
		// 페이지의 개수 계산.
		this.pageCount = (int) Math.ceil(listItemCount / (double) this.listSize);

		// 페이지를 페이지네이션하기 위한 계산.
		// 페이지 그룹의 개수 계산.
		// (올림)(페이지 개수 / 페이지 그룹당 페이지 개수)
		this.pageGroupCount = (int) Math.ceil(this.pageCount / (double) this.pageCountInGroup);

		// 현재 페이지 그룹 번호 계산.
		// 현재 페이지 번호 / 페이지 그룹당 페이지 개수
		this.groupNo = this.pageNo / this.pageCountInGroup;

		// 현재 페이지 그룹의 시작 페이지 번호 계산.
		// 현재 페이지 그룹 번호 * 페이지 그룹당 페이지 개수
		this.groupStartPageNo = this.groupNo * this.pageCountInGroup;

		// 현재 페이지 그룹의 마지막 페이지 번호 계산.
		// (현재 페이지 그룹 번호 + 1) * 페이지 그룹당 페이지 개수 - 1
		this.groupEndPageNo = (this.groupNo + 1) * this.pageCountInGroup - 1;

		// 마지막 페이지 번호 보정.
		// 현재 페이지 그룹의 마지막 페이지 번호가 총 페이지 개수보다 클 경우 보정 필요.
		if (this.groupEndPageNo > this.pageCount) {
			this.groupEndPageNo = this.pageCount - 1;
		}

		// 다음 그룹이 존재하는지 계산.
		// 현재 페이지 그룹 < 총 페이지 그룹 개수 - 1
		this.hasNextPageGroup = this.groupNo < this.pageGroupCount - 1;

		// 이전 그룹이 존재하는 지 계산.
		// 현재 페이지 그룹 > 0
		this.hasPrevPageGroup = this.groupNo > 0;

		// 다음 그룹의 시작 페이지 번호 계산.
		// 현재 페이지 그룹의 마지막 페이지 번호 + 1
		this.nextPageGroupStartPageNo = this.groupEndPageNo + 1;

		// 이전 그룹의 시작 페이지 번호 계산.
		// 현재 페이지 그룹의 시작 페이지 번호 - 페이지 그룹당 페이지의 개수
		this.prevPageGroupStartPageNo = this.groupStartPageNo - this.pageCountInGroup;

	}

}

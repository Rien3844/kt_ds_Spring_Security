package com.ktdsuniversity.edu.files.vo.request;

public class UploadVO {

	private int fileNum;
	private String fileGroupId;
	private String obfuscateName;
	private String displayName;
	private String extendName;
	private long fileLength;
	private String filePath;

	public int getFileNum() {
		return this.fileNum;
	}

	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	public String getFileGroupId() {
		return this.fileGroupId;
	}

	public void setFileGroupId(String fileGroupId) {
		this.fileGroupId = fileGroupId;
	}

	public String getObfuscateName() {
		return this.obfuscateName;
	}

	public void setObfuscateName(String obfuscateName) {
		this.obfuscateName = obfuscateName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getExtendName() {
		return this.extendName;
	}

	public void setExtendName(String extendName) {
		this.extendName = extendName;
	}

	public long getFileLength() {
		return this.fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}

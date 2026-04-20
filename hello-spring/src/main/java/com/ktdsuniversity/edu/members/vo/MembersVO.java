package com.ktdsuniversity.edu.members.vo;

import java.util.List;

public class MembersVO {

	private String email;
	private String name;
	private String password;

	private String salt;
	private String registDate;
	private String modifyDate;
	private String latestPasswordChangeDate;
	private String loginDate;
	private String latestLoginIp;
	private int loginFailCount;
	private String latestLoginFailDate;
	private String blockYn;
	private String fileGroupId;

	private List<String> roles;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRegistDate() {
		return this.registDate;
	}

	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}

	public String getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getLatestPasswordChangeDate() {
		return this.latestPasswordChangeDate;
	}

	public void setLatestPasswordChangeDate(String latestPasswordChangeDate) {
		this.latestPasswordChangeDate = latestPasswordChangeDate;
	}

	public String getLoginDate() {
		return this.loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	public String getLatestLoginIp() {
		return this.latestLoginIp;
	}

	public void setLatestLoginIp(String latestLoginIp) {
		this.latestLoginIp = latestLoginIp;
	}

	public int getLoginFailCount() {
		return this.loginFailCount;
	}

	public void setLoginFailCount(int loginFailCount) {
		this.loginFailCount = loginFailCount;
	}

	public String getLatestLoginFailDate() {
		return this.latestLoginFailDate;
	}

	public void setLatestLoginFailDate(String latestLoginFailDate) {
		this.latestLoginFailDate = latestLoginFailDate;
	}

	public String getBlockYn() {
		return this.blockYn;
	}

	public void setBlockYn(String blockYn) {
		this.blockYn = blockYn;
	}

	public String getFileGroupId() {
		return this.fileGroupId;
	}

	public void setFileGroupId(String fileGroupId) {
		this.fileGroupId = fileGroupId;
	}

	public List<String> getRoles() {
		return this.roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}

package com.ktdsuniversity.edu.members.vo.request;

import jakarta.validation.constraints.NotBlank;

public class LoginVO {

	@NotBlank(message = "email을 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	// 사용자 IP
	private String ip;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}

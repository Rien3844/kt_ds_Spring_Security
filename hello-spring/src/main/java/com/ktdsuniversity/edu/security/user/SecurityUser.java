package com.ktdsuniversity.edu.security.user;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ktdsuniversity.edu.members.vo.MembersVO;

/**
 * Spring Security가 사용자를 식별할 때 사용.
 */
public class SecurityUser implements UserDetails {

	private static final long serialVersionUID = 7907191462472441568L;

	/**
	 * UserDetails 인터페이스로 사용자의 세부 내용을 알 수 없기 때문에
	 * 사용자의 정보를 가지고 있는 membersVO를 멤버변수로 추가해준다.
	 */
	private MembersVO membersVO;
	
	public SecurityUser(MembersVO membersVO) {
		this.membersVO = membersVO;
	}
	
	public MembersVO getMembersVO() {
		return this.membersVO;
	}
	
	/**
	 * 사용자의 권한 목록을 관리.
	 * 추후 권한별 서비스 제공시 사용.
	 * ROLES 테이블에서 조회.
	 * 
	 * GrantedAuthority <-- 사용자에게 허용된 권한
	 * Collection <-- List / Set
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Spring Security가 체크하는 권한 2가지.
		// 1. ROLE ==> 권한.
		// 2. ACTION ==> 생성, 조회, 수정, 삭제, 다운로드, 업로드, ...
		// Spring Security가 ROLE과 ACTION을 구분하는 방법.
		// ROLE ==> Prefix == 'ROLE_RL-20260414-000003'
		// ACTION ==> ACTION이름으로 작성. (CREATE, READ, MODIFY, DELETE, DOWNLOAD, UPLOAD)
		return this.membersVO.getRoles()
							 .stream()
							 .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
							 .toList();
	}

	/**
	 * 로그인 한 회원의 비밀번호
	 */
	@Override
	public @Nullable String getPassword() {
		return this.membersVO.getPassword();
	}

	/**
	 * 사용자의 아이디(식별가능한)
	 * --> 이메일
	 */
	@Override
	public String getUsername() {
		return this.membersVO.getEmail();
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return this.membersVO.getBlockYn().equals("N");
	}
	
}

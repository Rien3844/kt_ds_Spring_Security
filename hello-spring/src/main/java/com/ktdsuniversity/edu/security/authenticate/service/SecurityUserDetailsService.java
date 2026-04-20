package com.ktdsuniversity.edu.security.authenticate.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ktdsuniversity.edu.members.dao.MembersDao;
import com.ktdsuniversity.edu.members.vo.MembersVO;
import com.ktdsuniversity.edu.security.user.SecurityUser;

/**
 * 로그인 인증 수행시 로그인 요청 정보 중 아이디로 회원의 정보를 조회한다.
 */
public class SecurityUserDetailsService 
			implements UserDetailsService {

	private MembersDao membersDao;
	
	public SecurityUserDetailsService(MembersDao membersDao) {
		this.membersDao = membersDao; 
	}

	/**
	 * 아이디로 데이터베이스에서 회원의 정보를 조회한다.
	 * @param username : 아이디 (이메일)
	 * @return DB에서 조회한 회원의 정보 (SecurityUser)
	 * @throws UsernameNotFoundException : DB에 회원의 정보가 없을 때 던져지는 예외.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) 
				throws UsernameNotFoundException {
		MembersVO loadedUser = this.membersDao.selectMemberByEmail(username);
		if (loadedUser == null) {
			throw new UsernameNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		List<String> userRole = this.membersDao.selectMemberRolesByEmail(username);
		loadedUser.setRoles(userRole);
		
		return new SecurityUser(loadedUser);
	}

}









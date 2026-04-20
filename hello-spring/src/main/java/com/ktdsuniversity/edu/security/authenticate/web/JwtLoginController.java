package com.ktdsuniversity.edu.security.authenticate.web;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktdsuniversity.edu.common.utils.ServletUtils;
import com.ktdsuniversity.edu.exceptions.HelloSpringApiException;
import com.ktdsuniversity.edu.members.dao.MembersDao;
import com.ktdsuniversity.edu.members.vo.MembersVO;
import com.ktdsuniversity.edu.members.vo.request.LoginVO;
import com.ktdsuniversity.edu.security.authenticate.service.SecurityPasswordEncoder;
import com.ktdsuniversity.edu.security.providers.JsonWebTokenAuthenticationProvider;
import com.ktdsuniversity.edu.security.user.SecurityUser;

import jakarta.validation.Valid;

@Controller
public class JwtLoginController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MembersDao membersDao;
	
	@Autowired
	private JsonWebTokenAuthenticationProvider jwtAuthenticationProvider;
	
	@PostMapping("/api/authorization")
	@ResponseBody
	public Map<String, String> doJwtLogin(@Valid @RequestBody LoginVO loginVO, BindingResult bindingResult) {
		// 에러 발생시
		if (bindingResult.hasErrors()) {
			throw new HelloSpringApiException("로그인 실패", HttpStatus.BAD_REQUEST.value(), bindingResult.getFieldErrors());
		}

		// 이메일을 통해서 회원의 정보를 조회.
		UserDetails userDetails = null;
		try {
			userDetails = this.userDetailsService.loadUserByUsername(loginVO.getEmail());
		} catch (UsernameNotFoundException unfe) { // DB에 email에 해당하는 사용자 정보 있는지 확인
			throw new HelloSpringApiException("로그인 실패"
											 , HttpStatus.BAD_REQUEST.value()
											 , "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		// 잠긴 계정인지 확인
		if (!userDetails.isAccountNonLocked()) {
			throw new HelloSpringApiException("로그인 실패"
											 , HttpStatus.BAD_REQUEST.value()
											 , "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		// 비밀번호 일치검사 수행.
		String password = loginVO.getPassword();
		SecurityPasswordEncoder securityPasswordEncoder =
								(SecurityPasswordEncoder) this.passwordEncoder;
		
		SecurityUser securityUser = (SecurityUser) userDetails;
		MembersVO membersVO = securityUser.getMembersVO();
		
		if(!securityPasswordEncoder.matches(password
										  , membersVO.getSalt()
										  , membersVO.getPassword())) {
			this.membersDao.updateIncreaseLoginFailCount(loginVO.getEmail());
			this.membersDao.updateBlock(loginVO.getEmail());
			
			throw new HelloSpringApiException("로그인 실패"
											, HttpStatus.BAD_REQUEST.value()
											, "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		// 패스워드 일치
		loginVO.setIp(ServletUtils.getIp());
		
		this.membersDao.updateSuccessLogin(loginVO);
		
		// JWT 생성 후 API 결과 반환.
		// 9시간 짜리 토큰 제작(보통 9to6로 일하므로.)
		String jwt = this.jwtAuthenticationProvider
							.makeJsonWebToken(loginVO.getEmail(), Duration.ofHours(9));
		
		return Map.of("token", jwt);
	}
}

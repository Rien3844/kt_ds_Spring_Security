package com.ktdsuniversity.edu.security.authenticate.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.ktdsuniversity.edu.members.vo.MembersVO;

public class HelloSpringOAuthService 
		//OAuth를 통해 회원을 조회하는 인터페이스.
		implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	
	private static final Logger logger = LoggerFactory.getLogger(HelloSpringOAuthService.class);
	
	/**
	 * /oauth/authorization/naver or google을 통해 로그인 한 이후 수행되는 메소드.
	 * naver 또는 google에서 redurect-uri로 응답을 돌려줄 때 실행된다.
	 * 
	 * @param userRequest oauth 서비스 제공자(naver, google) 에게 개인정보를 요청하는 객체
	 * 		  1. authorization-uri 호출해서 oauth 인증 수행.
	 * 		  2. 인증 성공 후 token-uri 호출해서 oauth token을 발급받는다.
	 * 		  3. 발급받은 oauth token을 이용해서 user-info-uri를 호출해서 사용자 정보를 취득한다.
	 * @return OAuth2User  서비스 제공자(naver, google)로 부터
	 * 		   취득한 사용자의 정보를 이용해 Security 인증 정보 생성.
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest)
				throws OAuth2AuthenticationException{
		
		// userRequest를 통해서 개인 정보 취득하기
		// Oauth2UserService의 기본 객체를 생성 후 userRequest 전달.
		OAuth2UserService<OAuth2UserRequest, OAuth2User> userService = new DefaultOAuth2UserService();
		
		OAuth2User oauthResult = userService.loadUser(userRequest);
		
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		if (registrationId.equals("naver")) {
			MembersVO naverMember = new MembersVO();
			OAuth2User naverUser = 
					new NaverOAuthUserDetails(
							naverMember, oauthResult.getAttributes());
			
			return naverUser;
		}
		
		logger.debug(oauthResult.toString());
		
		return null;
	}
}

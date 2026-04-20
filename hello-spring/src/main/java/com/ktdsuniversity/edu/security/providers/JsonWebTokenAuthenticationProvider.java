package com.ktdsuniversity.edu.security.providers;

import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * 사용자의 정보를 이용해 인증 객체를 생성하고 검증하는 클래스.
 * Spring Security AuthenticationProvider과는 무관.
 * 사용 목적 : API를 호출할 때 인증수단으로 사용하기 위해.
 */
public class JsonWebTokenAuthenticationProvider {
	
	/**
	 * 사용자의 이메일을 이용해 인증용 JWT 생성.
	 * 
	 * @param email - 사용자의 이메일
	 * @param expiredAt - expireAt JWT의 유효기간 ( 지금으로부터 ~분(시간, 일, 월, 연) 까지 유효
	 * @return email과 expireAt으로 생성한 JsonWebToken
	 */
	public String makeJsonWebToken(String email, Duration expiredAt) {
		
		// JsonWebToken이 발행되는 날짜와 시간을 생성.
		Date issueDate = new Date();
		
		// JsonWebToken이 만료되는 날짜와 시간을 생성.
		// 발행 날짜 시간 + expiredAt
		Date expirationDate = new Date(issueDate.getTime() + expiredAt.toMillis());
		
		// SecretKey signKey = Keys.hmacShaKeyFor("application.yml에 작성한 비밀 키");
		SecretKey signKey = Keys.hmacShaKeyFor("sadn9xuin29dx29sd0x2fje9p2j90v40tafjdasjo22".getBytes());
		
		String jsonWebToken = Jwts.builder()
								  // JsonWebToken을 발행한 시스템의 이름
								  // TODO aplication.yml에서 가져올것
								  .issuer("hello-spring")
								  // JsonWebToken의 이름 작성
								  .subject(email + "_token")
								  // JsonWebToken에 포함되어야 할 회원의 정보'들' ==> .claim(key, value)
								  // 추가하고싶으면 뒤에 .claim으로 계속 추가 가능.
								  // But, claim에 개인정보가 들어갈 수록 위험 => 최소한의 내용만 전달.
								  .claim("identify", email)
								  // JsonWebToken을 발행한 시간(발행한 날짜와 시간)
								  .issuedAt(issueDate)
								  // JsonWebToken의 유효 기간(만료되는 날짜와 시간)
								  // issuedAt => expiration에 지정된 기간만큼 유효한 토큰 생성.
								  .expiration(expirationDate)
								  // 평문으로 구성된 JsonWebToken을 암호화 또는 복호화 시킬 때 사용할 키(salt와 유사.)
								  // => JsonWebToken을 Debugger에 돌려도 암호화된 값이 나옴.(개인정보 털릴확률 낮아짐)
								  .signWith(signKey)
								  // Jwts에 제공된 데이터를 이용해 String Type의 Token을 생성.
								  .compact();
		
		return jsonWebToken;
	}
	
	public static void main(String[] args) {
		JsonWebTokenAuthenticationProvider jwtProvider = new JsonWebTokenAuthenticationProvider();
		String jwt = jwtProvider.makeJsonWebToken("test@gmail.com", Duration.ofHours(3));
		System.out.println(jwt);
	}
}

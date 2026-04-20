package com.ktdsuniversity.edu.security.providers;

import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * 사용자의 정보를 이용해 인증 객체를 생성하고 검증하는 클래스.
 * Spring Security AuthenticationProvider과는 무관.
 * 사용 목적 : API를 호출할 때 인증수단으로 사용하기 위해.
 */
public class JsonWebTokenAuthenticationProvider {
	
	private String secretKey; // application.yml의 secret-key를 받을 String
	private String issuer;    // application.yml의 issuer를 받을 String
	
	
	
	public JsonWebTokenAuthenticationProvider(String secretKey, String issuer) {
		this.secretKey = secretKey;
		this.issuer = issuer;
	}

	/**
	 * 사용자가 요청할 때마다 Request Header[Authorization]에 전달한
	 * JsonWebToken을 가져와 복호화 시킨다.
	 * 복호화 된 결과에서 사용자의 이메일(= identify)을 추출하여 반환시킨다.
	 * 
	 * @param jsonWebToken ==> 사용자가 전달한 토큰
	 * @return jsonWebToken에서 추출한 사용자의 이메일
	 */
	public String decryptJsonWebToken(String jsonWebToken) {

		// 암/복호화 키 생성
		// SecretKey signKey = Keys.hmacShaKeyFor("application.yml에 작성한 비밀 키");
		SecretKey signKey = Keys.hmacShaKeyFor(this.secretKey.getBytes());
		
		// Claims ==> 밑에 builder에서 해준 claims값만 가져오겠다. 
		Claims claims = Jwts.parser()                        // JsonWebToken을 분석하기 위한 선언.
							.verifyWith(signKey)             // JsonWebToken을 복호화 하기 위한 비밀키 지정.
							.requireIssuer(this.issuer)   // 사용자가 전달한 JsonWebToken이 hello-spring 시스템에서 만든 것인지 확인한다.(최소한의 검증)
							.build()                         // JsonWebToken 복호화 시작.
							.parseSignedClaims(jsonWebToken) // 사용자가 전달한 jsonWebToken을. ==> 아래 builder 구조에서는 signKey
							.getPayload();                   // 복호화 된 결과에서 claim들만 모아서 반환시킨다. ==> Map 형태.
		
		// 사용자가 전달한 JsonWebToken을 복호화 한 뒤 identify 값을 추출한다.
		String email = claims.get("identify", String.class);
		
		return email;
	}
	
	/**
	 * 사용자의 이메일을 이용해 인증용 JWT 생성하고
	 * 결과를 사용자에게 보내주어야 한다.
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
		
		// 암/복호화 키 생성
		// SecretKey signKey = Keys.hmacShaKeyFor("application.yml에 작성한 비밀 키");
		SecretKey signKey = Keys.hmacShaKeyFor(this.secretKey.getBytes());
		
		String jsonWebToken = Jwts.builder()
								  // JsonWebToken을 발행한 시스템의 이름
								  .issuer(this.issuer)
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
		JsonWebTokenAuthenticationProvider jwtProvider = new JsonWebTokenAuthenticationProvider("sadn9xuin29dx29sd0x2fje9p2j90v40tafjdasjo22", "hello-spring");
		String jwt = jwtProvider.makeJsonWebToken("test@gmail.com", Duration.ofHours(3));
		System.out.println(jwt);
		
		// 복호화 진행
		String email = jwtProvider.decryptJsonWebToken(jwt);
		System.out.println(email);
	}
}

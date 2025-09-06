package com.clucid.server.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
	private Long accessExpiredMs = 3*24*60*60*1000L; // 몰라 걍 노빠꾸로 3일 가
	// private Long refreshExpiredMs = 3*24*60*60*1000L; // 3일
	private SecretKey secretKey;

	public JwtUtil(@Value("${custom.jwt.secret}")String secret){
		this.secretKey = new SecretKeySpec
			(secret.getBytes(StandardCharsets.UTF_8),
				Jwts.SIG.HS256.key().build().getAlgorithm());
	}
	public String getSub(String token){
		return Jwts.parser().verifyWith(secretKey).build()
			.parseSignedClaims(token).getPayload().get("sub",String.class);
	}
	public String getEmail(String token){
		return Jwts.parser().verifyWith(secretKey).build()
			.parseSignedClaims(token).getPayload().get("email",String.class);
	}

	public String getRole(String token){
		return Jwts.parser().verifyWith(secretKey).build()
			.parseSignedClaims(token).getPayload().get("role",String.class);
	}
	public Boolean isExpired(String token){
		return Jwts.parser().verifyWith(secretKey).build()
			.parseSignedClaims(token).getPayload().getExpiration().before(new Date());
	}

	public String createAccessJwt(String sub, String email,String nickname, String role){
		return Jwts.builder()
			.claim("sub",sub)
			.claim("email",email)
			.claim("nickname",nickname)
			.claim("role",role)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + accessExpiredMs))
			.signWith(secretKey)
			.compact();
	}
	// TODO: 리프레시 토큰을 생성하려면 하고, 꼭 JWT 로 생성할 필요는 없긴 함
}

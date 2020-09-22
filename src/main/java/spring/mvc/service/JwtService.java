package spring.mvc.service;

import java.text.ParseException;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class JwtService {

	public static final String USERNAME = "username";
	public static final String SECRET_KEY = "daychinhlachukybimatdaykgongdelorangoai";
	public static final int EXPIRE_TIME = 86400000;

	public String generateTokenLogin(String username) {
		String token = null;
		try {
			JWSSigner signer = new MACSigner(generateShareSecret());

			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
			builder.claim(USERNAME, username);
			builder.expirationTime(generateExpirationDate());

			JWTClaimsSet claimsSet = builder.build();
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

			signedJWT.sign(signer);

			token = signedJWT.serialize();
		} catch (KeyLengthException e) {
			e.printStackTrace();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		return token;
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + EXPIRE_TIME);
	}

	private byte[] generateShareSecret() {
		byte[] shareSecret = new byte[32];
		shareSecret = SECRET_KEY.getBytes();
		return shareSecret;
	}

	private JWTClaimsSet getClaimsFromToken(String token) {
		JWTClaimsSet claimsSet = null;
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			JWSVerifier verifier = new MACVerifier(generateShareSecret());
			if (signedJWT.verify(verifier)) {
				claimsSet = signedJWT.getJWTClaimsSet();
			}
		} catch (JOSEException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return claimsSet;
	}

	private Date getExpirationDateFromTOken(String token) {
		Date expiration = null;
		JWTClaimsSet claimsSet = getClaimsFromToken(token);
		expiration = claimsSet.getExpirationTime();
		return expiration;
	}

	private Boolean isTokenExpired(String token) {
		Date expireation = getExpirationDateFromTOken(token);
		return expireation.before(new Date());
	}

	public String getUsernameFromToken(String token) {
		String username = null;
		try {
			JWTClaimsSet claimsSet = getClaimsFromToken(token);
			username = claimsSet.getStringClaim(USERNAME);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return username;
	}

	public Boolean validateTokenLogin(String token) {
		if (token == null || token.trim().length() == 0) {
			return false;
		}
		String username = getUsernameFromToken(token);
		System.out.println("Username: "+username);
		if (username == null || username.isEmpty()) {
			return false;
		}
		if (isTokenExpired(token)) {
			return false;
		}
		return true;
	}
}

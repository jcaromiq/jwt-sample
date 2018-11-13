package es.joaquincaro.jwtsample.authentication;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.util.Date;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenAuthenticationService {

	private final long EXPIRATION_TIME = 3600_000; 
	private final String SECRET = "69abd4abf577d7cfd6d370f146611fea"; //TODO: JVM OR ENVIRONMENT PARAMETER
	

	public String generateToken(String payload) {
		String jwt = Jwts.builder().setSubject(payload)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		return jwt;
	}

	public Authentication getAuthentication(String token) {
		UsernamePasswordAuthenticationToken authenticationToken = null;
		if (token != null) {
			String data = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
			
			ObjectMapper mapper = new ObjectMapper();
			AuthDTO user = null;
			try {
				user = mapper.readValue(data, AuthDTO.class);
			} catch (IOException e) {
                throw new NonceExpiredException("");
			}
			if(user != null) {
				authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, emptyList());
			}
		}
		return authenticationToken;
	}

}

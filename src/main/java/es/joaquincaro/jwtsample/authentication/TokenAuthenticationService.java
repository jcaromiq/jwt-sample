package es.joaquincaro.jwtsample.authentication;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import static java.util.Collections.emptyList;

import java.io.IOException;

@Service
public class TokenAuthenticationService {
	
	private final long EXPIRATION_TIME = 864_000_000;
	private final String SECRET ="69abd4abf577d7cfd6d370f146611fea";
	static final String HEADER_STRING = "Authorization";

	
	public String generateToken(String payload)  {
		String jwt = Jwts.builder()
                .setSubject(payload)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return jwt;
    }
	
	public Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String data = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            ObjectMapper mapper = new ObjectMapper();
            AuthDTO user = null;
			try {
				user = mapper.readValue(data,AuthDTO.class);
			} catch (IOException e) {
				
			}
            
            return user != null ?
                    new UsernamePasswordAuthenticationToken(user.getUsername(), null, emptyList()) :
                    null;
        }
        return null;
	}

	

}

package es.joaquincaro.jwtsample.authentication;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController()
public class AuthenticationController {

	private AuthenticationManager authenticationManager;
	
	private final long EXPIRATION_TIME = 864_000_000;
	private final String SECRET ="69abd4abf577d7cfd6d370f146611fea";

	private AuthenticationController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody AuthDTO authDTO) throws JsonProcessingException {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken 
		= new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword());
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		AuthDTO authenticated = new AuthDTO();
		authenticated.setUsername(authentication.getName());
		String jwt = generateJWT(authenticated);
		return new ResponseEntity<>(jwt,HttpStatus.ACCEPTED);

    }
	
	private String generateJWT(AuthDTO authDTO) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jwt = Jwts.builder()
                .setSubject(mapper.writeValueAsString(authDTO))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
		
		return jwt;
	}

}

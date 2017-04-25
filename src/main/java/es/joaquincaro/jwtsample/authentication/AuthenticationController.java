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


@RestController()
public class AuthenticationController {

	private AuthenticationManager authenticationManager;
	private TokenAuthenticationService tokenAuthenticationService;
	

	private AuthenticationController(AuthenticationManager authenticationManager, TokenAuthenticationService tokenAuthenticationService) {
		this.authenticationManager = authenticationManager;
		this.tokenAuthenticationService = tokenAuthenticationService;
	}
	
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody AuthDTO authDTO) throws JsonProcessingException {
		
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken 
		= new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword());
		
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		//Creamos un nuevo dto que contendrá el nombre del usuario
		AuthDTO authenticated = new AuthDTO();
		authenticated.setUsername(authentication.getName());
		//Podriamos pasar directamente el nombre al servicio de tokenizar en lugar del objeto json, 
		// pero de esta manera podriamos incluir tambien los roles y demas info en el token
		ObjectMapper mapper = new ObjectMapper();
		String jwt = tokenAuthenticationService.generateToken(mapper.writeValueAsString(authenticated));
		
		return new ResponseEntity<>(jwt,HttpStatus.ACCEPTED);

    }
	
}

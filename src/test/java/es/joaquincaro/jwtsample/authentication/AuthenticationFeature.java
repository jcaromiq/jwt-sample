package es.joaquincaro.jwtsample.authentication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationFeature {

	@LocalServerPort
	int randomServerPort;

	private AuthDTO VALID_USER;
	private AuthDTO NO_VALID_USER;

	private String URI;

	private RestTemplate restTemplate;

	@Before
	public void setUp() {
		VALID_USER = new AuthDTO("admin", "admin");
		NO_VALID_USER = new AuthDTO("USUARIO_NO_VALIDO", "pass");

		URI = "http://localhost:" + randomServerPort + "/auth";
		restTemplate = new RestTemplate();

	}

	@Test
	public void return_token_for_valid_credentials() {
		String token = restTemplate.postForEntity(URI, VALID_USER, String.class).getBody();
		assertThat(token, not(nullValue()));
	}

	@Test
	public void return_403_for_invalid_credentials() {
		try {
			restTemplate.postForEntity(URI, NO_VALID_USER, String.class).getBody();
		} catch (HttpClientErrorException e) {
			assertThat(e.getRawStatusCode(), is(HttpStatus.FORBIDDEN.value()));
		}
	}

}

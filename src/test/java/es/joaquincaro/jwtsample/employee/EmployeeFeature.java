package es.joaquincaro.jwtsample.employee;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.joaquincaro.jwtsample.authentication.AuthDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeFeature {

	@LocalServerPort
	int randomServerPort;

	private AuthDTO VALID_USER;

	private String URI_EMPLOYEE;
	private String URI_AUTH;

	private RestTemplate restTemplate;

	@Before
	public void setUp() {
		VALID_USER = new AuthDTO("admin", "admin");

		URI_EMPLOYEE = "http://localhost:" + randomServerPort + "/employee";
		URI_AUTH = "http://localhost:" + randomServerPort + "/auth";
		restTemplate = new RestTemplate();

	}

	@Test
	public void get_all_employees_with_authenticated_user() {
		String token = restTemplate.postForEntity(URI_AUTH, VALID_USER, String.class).getBody();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		HttpEntity<String> entity = new HttpEntity<String>("", headers);

		List<Employee> employees = restTemplate.exchange(URI_EMPLOYEE, HttpMethod.GET, entity, List.class).getBody();

		assertThat(employees, not(nullValue()));
	}

	@Test
	public void get_exception_if_user_is_guest() {
		try {
			restTemplate.getForEntity(URI_EMPLOYEE, List.class);
		} catch (HttpClientErrorException e) {
			assertThat(e.getRawStatusCode(), is(HttpStatus.FORBIDDEN.value()));
		}
	}
}

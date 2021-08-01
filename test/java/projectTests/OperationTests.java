package projectTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.logic.Item;
import twins.logic.OperationBoundary;
import twins.logic.OperationId;
import twins.logic.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OperationTests {
	private int port;
	private String url;
	private String adminUrl;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/twins/operations";
		this.adminUrl = "http://localhost:" + port + "/twins/admin/operations";
		this.restTemplate = new RestTemplate();
	}

	@AfterEach
	public void tearDown() {
		this.restTemplate.delete(this.adminUrl + "/adminSpace/adminEmail");
	}

	@Test
	public void testValidOperationCreationReturnsOperationWithId() throws Exception {
		// GIVEN the server up
		// do nothing

		// WHEN I POST/twins/operations and send {{"type":"operationType", "item": ...,
		// "invokedBy": ..., "operationAttributes": ...}}
		OperationBoundary newOperation = new OperationBoundary(null, "operationType", new Item("id", "space"),
				new User("email", "Space")); // {}

		OperationBoundary response = this.restTemplate.postForObject(this.url, newOperation, OperationBoundary.class);

		// THEN the server returns response with id that is not null or empty
		assertThat(response.getOperationId().getId()).isNotNull().isNotEmpty();
	}

}

package twins;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.logic.Item;
import twins.logic.NewUserDetails;
import twins.logic.OperationBoundary;
import twins.logic.OperationId;
import twins.logic.User;
import twins.logic.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OperationTests {
	private int port;
	private String url;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/twins";
		this.restTemplate = new RestTemplate();
	}
	
	@AfterEach
	public void cleanUp() {
		UserBoundary ub = new UserBoundary();
		ub.setUserId("test@test.com", "dummy");
		ub.setUsername("test");
		ub.setRole("PLAYER");
		ub.setAvatar("test.jpg");
		this.restTemplate
			.delete(this.url + "/admin/operations/{userSpace}/{userEmail}",
					ub.getUserId().getSpace(),ub.getUserId().getEmail());
	}
	
	
	
	@Test
	public void checkServerIsUpAndFunctional() {
	}
	
	@Test
	public void testOperationCreatedAndServerStoresTheData() throws Exception {
		//GIVEN the server up, do nothing
		
		//WHEN i POST /twins/operations and send operation JSON
		/* 
		 *{
		 *	"operationId":{
		 *		"space":"dummy",
		 *		"id":"1"
		 *	},
		 *	"type":"operationType",
		 *	"item":{
		 *		"itemId":{
		 *		"space":"dummy",
		 *		"id":"1"
		 *		}
		 *	},
		 *	"createdTimestamp":"2021-03-07T09:57:13.109+0000",
		 *	"invokedBy":{
		 *		"userId":{
		 *			"space":"dummy",
		 *			"email":"test@test.com"
		 *		}
		 *	},
		 *	"operationAttributes":{
		 *		}
		 *	}
		 *}
		 */
		
		User user = new User("test@test.com", "dummy");
		
		Item item = new Item("1", "dummy");
		
		OperationBoundary newOperation = new OperationBoundary(new OperationId("dummy", "1"), "TransactionTest", item, user);
		
		OperationBoundary response = this.restTemplate
				.postForObject(this.url + "/operations", newOperation, OperationBoundary.class);
		
		
		//THEN the server stores the operation in the database
		
		
		assertThat(response.getOperationId().getId())
				.isNotNull();
	}
	
	@Test
	public void testAsyncOperationCreatedAndServerStoresTheData() throws Exception {
		//GIVEN the server up, do nothing
		
		//WHEN i POST /twins/operations and send operation JSON
		/* 
		 *{
		 *	"operationId":{
		 *		"space":"dummy",
		 *		"id":"1"
		 *	},
		 *	"type":"operationType",
		 *	"item":{
		 *		"itemId":{
		 *		"space":"dummy",
		 *		"id":"1"
		 *		}
		 *	},
		 *	"createdTimestamp":"2021-03-07T09:57:13.109+0000",
		 *	"invokedBy":{
		 *		"userId":{
		 *			"space":"dummy",
		 *			"email":"test@test.com"
		 *		}
		 *	},
		 *	"operationAttributes":{
		 *		}
		 *	}
		 *}
		 */
		
		User user = new User("test@test.com", "dummy");
		
		Item item = new Item("1", "dummy");
		
		OperationBoundary newOperation = new OperationBoundary(new OperationId("dummy", "1"), "TransactionTest", item, user);
		
		OperationBoundary response = this.restTemplate
				.postForObject(this.url + "/operations/async", newOperation, OperationBoundary.class);
		
		
		//THEN the server stores the operation in the database
		
		
		assertThat(response.getOperationId().getId())
				.isNotNull();
	}
	
	
	@Test
	public void testGetAllOperationsForUserFromDatabaseReturnsAllOperations() throws Exception {
		//GIVEN the server up
		//AND the server contains a user,item and operation inside
		
		User user = new User("test@test.com", "dummy");
		
		Item item = new Item("1", "dummy");
		
		OperationBoundary newOperation = new OperationBoundary(new OperationId("dummy", "1"), "TransactionTest", item, user);
		
		OperationBoundary newAsyncOperation = new OperationBoundary(new OperationId("dummy", "2"), "AsyncTransactionTest", item, user);
		
		OperationBoundary actual = this.restTemplate
				.postForObject(this.url + "/operations", newOperation, OperationBoundary.class);
		
		OperationBoundary actualAsync = this.restTemplate
				.postForObject(this.url + "/operations/async", newAsyncOperation, OperationBoundary.class);
		
		
		
		//WHEN i invoke GET /admin/operations/{userSpace}/{userEmail} Return all Operations for specific user in database
		OperationBoundary[] result = this.restTemplate
				.getForObject(this.url + "/admin/operations/{userSpace}/{userEmail}", OperationBoundary[].class,
						actual.getInvokedBy().getUserId().getSpace(),
						actual.getInvokedBy().getUserId().getEmail());
			
		
		//THEN the database is updated

		assertThat(result)
			.isNotNull()
			.isNotEmpty();
		
	}
	
	
	
}

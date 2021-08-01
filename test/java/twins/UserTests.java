package twins;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.logic.NewUserDetails;
import twins.logic.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserTests {
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
	
	// TODO check with shachar and nahum about why need userEmail to delete all users
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
	public void testUserCreatedAndServerStoresTheData() throws Exception {
		//GIVEN the server up, do nothing
		
		//WHEN i POST /twins/users and send user JSON
		NewUserDetails newUser = new NewUserDetails();
		newUser.setEmail("test@test.com");
		newUser.setRole("ADMIN");
		newUser.setUsername("test1");
		newUser.setAvatar("admin.jpg");
		
		UserBoundary response = this.restTemplate
				.postForObject(this.url + "/users", newUser, UserBoundary.class);
		
		
		//THEN the server stores the user in the database
		//invoke HTTP GET /users/login/{userSpace}/{userEmail}
		// assert the user retrieved is the user posted to the database
		
		UserBoundary userInDb = this.restTemplate
				.getForObject(this.url + "/users/login/{userSpace}/{userEmail}",UserBoundary.class,
						response.getUserId().getSpace(),
						response.getUserId().getEmail());
		
		assertThat(userInDb.getUserId().getEmail())
				.isNotNull()
				.isEqualTo(response.getUserId().getEmail());
	}
	
	@Test
	public void testUserUpdated() throws Exception {
		//GIVEN the server up
		//AND the server contains a user inside with content: 
		/*  {
		 *		"userID": {
 * 					"space" : "dummy",
 * 					"email" : "test@test.com",
 * 	 			},
 * 				"role" : "ADMIN",
 * 				"username" :"test1",
 * 				"avatar" : "admin.jpg",
 * 			}
		 */
		
		NewUserDetails oldUser = new NewUserDetails();
		oldUser.setEmail("test@test.com");
		oldUser.setRole("ADMIN");
		oldUser.setUsername("test1");
		oldUser.setAvatar("admin.jpg");
		
		UserBoundary actual = this.restTemplate
				.postForObject(this.url + "/users", oldUser, UserBoundary.class);
		
		//WHEN i invoke PUT /twins/users/{userSpace}/{userEmail} with user JSON changing role to PLAYER
		/*  {
		 *		"userID": {
 * 					"space" : "dummy",
 * 					"email" : "test@test.com",
 * 	 			},
 * 				"role" : "PLAYER",
 * 				"username" :"test1",
 * 				"avatar" : "admin.jpg",
 * 			}
		 */
		actual.setRole("PLAYER");
		UserBoundary updatedUser = actual;

		this.restTemplate
			.put(this.url + "/users/{userSpace}/{userEmail}", updatedUser,
					actual.getUserId().getSpace(),
					actual.getUserId().getEmail());
			
		
		//THEN the database is updated
		
		UserBoundary userInDb = this.restTemplate
				.getForObject(this.url + "/users/login/{userSpace}/{userEmail}", UserBoundary.class,
						actual.getUserId().getSpace(),
						actual.getUserId().getEmail());
		
		assertThat(userInDb.getRole())
			.isEqualTo(updatedUser.getRole());
		
	}
	
	
	// TODO check with shachar and nahum about why need userEmail to get all users in space
	@Test
	public void testGetAllUsersFromDatabaseReturnsAllUsers() throws Exception {
		//GIVEN the server up
		//AND the server contains a user inside with content: 
		/*  {
		 *		"userID": {
 * 					"space" : "dummy",
 * 					"email" : "test@test.com",
 * 	 			},
 * 				"role" : "ADMIN",
 * 				"username" :"test1",
 * 				"avatar" : "admin.jpg",
 * 			}
		 */
		
		NewUserDetails oldUser = new NewUserDetails();
		oldUser.setEmail("test@test.com");
		oldUser.setRole("ADMIN");
		oldUser.setUsername("test1");
		oldUser.setAvatar("admin.jpg");
		
		UserBoundary actual = this.restTemplate
				.postForObject(this.url + "/users", oldUser, UserBoundary.class);
		
		
		
		//WHEN i invoke GET /admin/users/{userSpace}/{userEmail} Return all Users in database
		UserBoundary[] result = this.restTemplate
				.getForObject(this.url + "/admin/users/{userSpace}/{userEmail}", UserBoundary[].class,
						actual.getUserId().getSpace(),
						actual.getUserId().getEmail());
			
		
		//THEN the database is updated

		assertThat(result)
			.isNotNull()
			.isNotEmpty();
		
	}
	
	
	
}

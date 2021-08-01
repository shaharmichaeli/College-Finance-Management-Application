package twins;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.logic.NewUserDetails;
import twins.logic.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTests {
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
	
	@Test
	public void testContext() {
	}
	
	@Test
	public void testCreateUsers() throws Exception {
		//GIVEN the server is up, do nothing
		
		//WHEN I post new users in /twins/users
		String[] emails = {"abc@1.com", "abc@2.com", "abc@3.com", "abc@4.com", "abc@5.com", "abc@6.com"};
		String[] roles = {"ADMIN", "MANAGER", "PLAYER", "PLAYER", "PLAYER", "PLAYER"};
		String[] usernames = {"ram", "naor", "gal", "nahum", "shachar", "nir"};
		String[] avatars = {"1.jpg", "2.jpg", "3.jpg", "4.jpg", "5.jpg", "6.jpg"};
		
		for(int i=0; i<emails.length; i++) {
			NewUserDetails userToPost = new NewUserDetails();
			userToPost.setEmail(emails[i]);
			userToPost.setRole(roles[i]);
			userToPost.setUsername(usernames[i]);
			userToPost.setAvatar(avatars[i]);
			
			UserBoundary response = this.restTemplate
					.postForObject(this.url+"/users", userToPost, UserBoundary.class);
		
			//THEN the server returns response with userDetails which is not null
//			if(response.getUserId() == null 
//					|| response.getAvatar() == null 
//					|| response.getRole() == null 
//					|| response.getUsername() == null)
//				throw new Exception("The users hasn't been created!");
			assertThat(response.getUserId()).isNotNull();
			assertThat(response.getRole()).isNotNull();
			assertThat(response.getAvatar()).isNotNull();
			assertThat(response.getUsername()).isNotNull();
		
			 String check = "{ "
			 			+ "\"email\" :" + response.getUserId().getEmail() + ","
			 			+ "\"role\" :" + response.getRole() + ","
			 			+ "\"username\" :" + response.getUsername() + ","
			 			+ "\"avatar\" :" + response.getAvatar() + 
				  			"}";
				  
			System.err.println(check);
		}	
		
	}
}

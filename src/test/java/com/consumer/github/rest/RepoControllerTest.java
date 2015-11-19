package com.consumer.github.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static  org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.consumer.github.Application;

import net.sf.ehcache.CacheManager;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RepoControllerTest {
	
	@Autowired
	WebApplicationContext context;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	private MockMvc mvc;
	
	
	@After
	public void destroy(){
		CacheManager.getInstance().shutdown();
	}
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.addFilter(springSecurityFilterChain).build();
		
		
	}
	
	@Test
	public void testValidUser() throws Exception {
		
		String token = getAccessToken("roy", "spring");
		RestTemplate template  = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);

		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		ResponseEntity<String> response = template
				.exchange(new URI("http://127.0.0.1:8080/listRepos/spring-projects"),
						HttpMethod.GET, entity, String.class);
		
		 response = template
				.exchange(new URI("http://127.0.0.1:8080/listRepos/spring-projects"),
						HttpMethod.GET, entity, String.class);
		assertEquals(HttpStatus.OK,response.getStatusCode());
	}
	
	@Test
	public void testInValidUser() throws Exception {
		String token = getAccessToken("roy", "spring");
		RestTemplate template  = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		
		ResponseEntity<String> response  = null;;
		try {
			response = template.exchange(
							new URI("http://127.0.0.1:8080/listRepos/fdwefdwfwg"),
							HttpMethod.GET, entity, String.class);
		} catch (HttpClientErrorException e) {
			// TODO Auto-generated catch block
			response = new ResponseEntity<String>(e.getMessage(),e.getStatusCode());
			
		}
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
	}

	@Test
	public void testValidSearchByUser() throws Exception {
		
		String token = getAccessToken("roy", "spring");
		RestTemplate template  = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		
		ResponseEntity<String> response  = null;;
			response = template.exchange(
							new URI("http://127.0.0.1:8080/searchRepos/spring-projects?search=security"),
							HttpMethod.GET, entity, String.class);
		System.out.println(response);
	
	}

	@Test
	public void testInValidSearchByUser() throws Exception {
		
		String token = getAccessToken("roy", "spring");
		RestTemplate template  = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		
		ResponseEntity<String> response  = null;
			
		try {
	
			response = template.exchange(
							new URI("http://127.0.0.1:8080/searchRepos/mferfwf?search=mfdwfdkfdwf"),
							HttpMethod.GET, entity, String.class);
		} catch (HttpClientErrorException e) {
			// TODO Auto-generated catch block
			response = new ResponseEntity<String>(e.getMessage(),e.getStatusCode());
			
		}
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
	
	}
	
	

	@Test
	public void testValidFilterByUserAndRepoName() throws Exception {
		String token = getAccessToken("roy", "spring");
		RestTemplate template  = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		
		ResponseEntity<String> response  = null;;
			response = template.exchange(
							new URI("http://127.0.0.1:8080/filterRepos/spring-projects?name=security"),
							HttpMethod.GET, entity, String.class);
	
	}
	

	@Test
	public void testInValidFilterByUserAndRepoName() throws Exception {
		String token = getAccessToken("roy", "spring");
		RestTemplate template  = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		
		ResponseEntity<String> response  = null;
			
		try {
	
			response = template.exchange(
							new URI("http://127.0.0.1:8080/filterRepos/spring-projects?name=gffegefg"),
							HttpMethod.GET, entity, String.class);
		} catch (HttpClientErrorException e) {
			// TODO Auto-generated catch block
			response = new ResponseEntity<String>(e.getMessage(),e.getStatusCode());
			
		}
		//Github appears to thin this is ok so lets not suprise the users by doing anything different.
		assertEquals(HttpStatus.OK,response.getStatusCode());

	
	}
	
	private String getAccessToken(String username, String password) throws Exception {
		String authorization = "Basic "
				+ new String(Base64Utils.encode("clientapp:123456".getBytes()));
		String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

		// @formatter:off
		String content = mvc
				.perform(
						post("/oauth/token")
								.header("Authorization", authorization)
								.contentType(
										MediaType.APPLICATION_FORM_URLENCODED)
								.param("username", username)
								.param("password", password)
								.param("grant_type", "password")
								.param("scope", "read write")
								.param("client_id", "clientapp")
								.param("client_secret", "123456"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.access_token", is(notNullValue())))
				.andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
				.andExpect(jsonPath("$.refresh_token", is(notNullValue())))
				.andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
				.andExpect(jsonPath("$.scope", is(equalTo("read write"))))
				.andReturn().getResponse().getContentAsString();

		// @formatter:on
		JSONObject json = new JSONObject(content);
				
		return "Bearer "+json.getString("access_token");
	}
}

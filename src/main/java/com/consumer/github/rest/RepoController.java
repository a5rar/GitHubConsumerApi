package com.consumer.github.rest;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.consumer.github.doman.Repository;

@CacheConfig(cacheNames = "repos")
@RestController
public class RepoController {

	@Value("${github.domain}")
	private String gitHubDomain;

	@Autowired
	private RestTemplate template;

	
	@Cacheable
	@RequestMapping(value = "/listRepos/{user}", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity<String> listRepos(@PathVariable String user) {

		StringBuilder uri = new StringBuilder(gitHubDomain).append("users/").append(user).append("/repos");

		ResponseEntity<String> repos = new ResponseEntity<String>(new String(
				template.exchange(uri.toString(), HttpMethod.GET, new HttpEntity<String>(""), String.class).getBody()),
				HttpStatus.OK);
		
		return repos;
	}

	@Cacheable
	@RequestMapping(value = "/searchRepos/{user}", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity<PagedResources<Resource<Repository>>> searchRepo(@PathVariable String user, String search,Pageable pageable) throws URIException {

		String uri = URIUtil.encodeQuery(gitHubDomain+"search/repositories?q="+search+"+user:"+user+
				"&page="+pageable.getPageNumber()+"&per_page="+pageable.getPageSize());
		
		JSONObject jsonObj =  new JSONObject(template.exchange(uri, HttpMethod.GET, new HttpEntity<String>(""), String.class).getBody());
		int totalElements = jsonObj.getInt("total_count");
		List<Repository> repositories = getContentItems(jsonObj);
		
		PagedResources<Resource<Repository>>  pagedResource = PagedResources.wrap(repositories, new PageMetadata(repositories.size(), pageable.getPageNumber(), totalElements));
		return new ResponseEntity<PagedResources<Resource<Repository>>>(
				pagedResource,
				HttpStatus.OK
				);
	}

	
	@Cacheable
	@RequestMapping(value = "/filterRepos/{user}", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity<PagedResources<Resource<Repository>>> filterRepo(@PathVariable String user, String name,Pageable pageable) throws URIException {

		String uri = URIUtil.encodeQuery(gitHubDomain+"search/repositories?q="+name+"+in:name"+"+user:"+user+
				"&page="+pageable.getPageNumber()+"&per_page="+pageable.getPageSize());
	
		JSONObject jsonObj =  new JSONObject(template.exchange(uri, HttpMethod.GET, new HttpEntity<String>(""), String.class).getBody());
		int totalElements = jsonObj.getInt("total_count");
		List<Repository> repositories = getContentItems(jsonObj);
		
		PagedResources<Resource<Repository>>  pagedResource = PagedResources.wrap(repositories, new PageMetadata(repositories.size(), pageable.getPageNumber(), totalElements));
		return new ResponseEntity<PagedResources<Resource<Repository>>>(
				pagedResource,
				HttpStatus.OK
				);
	}
	
	
	private List<Repository> getContentItems(JSONObject jsonObj) {
		JSONArray jsArray =  jsonObj.getJSONArray("items");
		List<Repository> content = new ArrayList<Repository>();
		for(int i=0; i<jsArray.length(); i++ ){
			JSONObject obj = jsArray.optJSONObject(i);
			content.add(new Repository(obj.toString()));
		}
		return content;
	}
	

}

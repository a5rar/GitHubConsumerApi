package com.consumer.github.doman;

public class Repository {
	
	private String repo;
	
	public Repository(String repo)
	{
		this.repo=repo;
	}

	public String getRepo() {
		return repo;
	}

	public void setRepo(String repo) {
		this.repo = repo;
	}

	@Override
	public String toString() {
		return "Repository [repo=" + repo + "]";
	}
	
	

}

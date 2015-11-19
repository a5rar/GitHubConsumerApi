# GitHubConsumerApi
GithubConsumerApi

Make sure you have java 8 installed.  It might work on earlier versions but i havent tested it.  The project is a spring boot application and such it runs using a embedded tomcat container.  No need for any extras features.

- Check out project and  navigate to target folder and run ->
- java -jar GithubConsumer-0.0.1-SNAPSHOT.jar
- once the application is started you can run the follwoing curl
- curl -X POST -vu clientapp:123456 http://localhost:8080/oauth/token -H "Accept: application/json" -d "password=spring&username=roy&grant_type=password&scope=read%20write&client_secret=123456&client_id=clientapp"
- coppy the access_token and use this as a bearer token in  the header with each request.
- 

rest services
http://localhost:8080/listRepos/spring-projects
http://localhost:8080/searchRepos/spring-projects?search=secur
http://localhost:8080/filterRepos/spring-projects?name=security


The services are cached on application level, so there will be a 6 minute delay between changesd made in github and them appearing in the application.

The search and filter service also returna paginated response.  The listy repository follows the github doesn't. Not sure why but github api appears to return all the repositories and doesnt have any pagination on its api.

Any problens let me know



- 



package com.consumer.github;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
class GlobalControllerExceptionHandler {
	

	@ExceptionHandler({HttpClientErrorException.class})
	public  ResponseEntity<String>  handleException(Exception ex,WebRequest request) {
		return  new ResponseEntity<String>("No User Found",HttpStatus.NOT_FOUND);
	}

}
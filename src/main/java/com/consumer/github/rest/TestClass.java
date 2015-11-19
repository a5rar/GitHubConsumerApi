package com.consumer.github.rest;

import org.springframework.util.Base64Utils;

public class TestClass {

	public static void main(String[] args){
		byte[] array = Base64Utils.encode("clientapp:123456".getBytes());
		for(byte a : array){
			System.out.print(a);
		}
		
	
	}
}

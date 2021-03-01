package org.powo.harvest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/1/login", produces = "application/json")
public class LoginController {

	@PostMapping
	public ResponseEntity<String> tryLogin(){
		return new ResponseEntity<>("OK", HttpStatus.OK);
		
	}
}

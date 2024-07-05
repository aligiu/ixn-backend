package com.hng.ixn.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

//	String username = "user";
//	String password = "pass";

	@RequestMapping("/dummy")
	public String login() {
		return "dummy";
	}
}

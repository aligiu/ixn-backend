package com.hng.ixn.home;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@RequestMapping("/")
	public String home() {
		return "You are viewing the home page";
	}

	@RequestMapping("/dummy")
	public String dummy() {
		return "This is the dummy page";
	}
}

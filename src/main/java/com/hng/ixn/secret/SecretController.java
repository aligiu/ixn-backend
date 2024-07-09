package com.hng.ixn.secret;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secret")
public class SecretController {

    @GetMapping("/some")
    public String home() {
        return "You are viewing secret";
    }

}

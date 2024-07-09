package com.hng.ixn.content;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content/protected")
public class ProtectedContentController {

    @GetMapping("/some")
    public String home() {
        return "You are viewing some protected content";
    }

}

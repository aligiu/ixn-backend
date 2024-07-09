package com.hng.ixn.content;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content/public")
public class PublicContentController {

    @GetMapping("/some")
    public String home() {
        return "You are viewing some public content";
    }

}

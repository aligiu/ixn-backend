package com.hng.ixn.data;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data/public")
public class PublicDataController {

    @GetMapping("/some")
    public String home() {
        return "You are viewing some public data";
    }

}

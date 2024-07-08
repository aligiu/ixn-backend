package com.hng.ixn.data;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data/protected")
public class ProtectedDataController {

    @GetMapping("/some")
    public String home() {
        return "You are viewing some protected data";
    }

}

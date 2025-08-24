package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HelloController
 * @Author pt
 * @Description
 * @Date 2025/8/24 17:16
 **/
@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from Spring Boot Backend!");
        return response;
    }
}


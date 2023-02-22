package com.springstudy.studypractice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TestController {

    @GetMapping("")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> testControllerGet() {
        return ResponseEntity.ok().body("GET TEST");
    }

    @PostMapping("")
    public ResponseEntity<String> testControllerPost() {
        return ResponseEntity.ok().body("POST TEST");
    }
}

package com.example.project_sem_4.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*")
public class TestController {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String helloAdmin() {
        return "you can see that message only if your account's role is admin";
    }
}

package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class general {
    @GetMapping("/")
    public String greeting() {
        System.out.println("invoked");
        return "greeting";
    }

}

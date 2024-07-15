package com.microService.UserService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index(){

        System.out.println("index.html");
        return "index";
    }
}

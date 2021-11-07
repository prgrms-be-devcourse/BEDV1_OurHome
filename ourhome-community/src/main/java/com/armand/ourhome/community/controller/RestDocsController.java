package com.armand.ourhome.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestDocsController {
    @GetMapping("/")
    public String renderRestDocs(){
        return "index";
    }
}

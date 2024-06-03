package com.dhabits.ss.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @RequestMapping("/UnAuthorized")
    @ResponseBody
    public String unAuthorized(){
        return "UnAuthorized";
    }
}

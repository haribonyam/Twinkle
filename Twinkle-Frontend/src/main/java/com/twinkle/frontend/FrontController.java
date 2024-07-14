package com.twinkle.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

    @GetMapping("/")
    public String MainPage(){

        return "main";
    }
    @GetMapping("/login")
    public String LoginPage(){

        return "login";
    }

    @GetMapping("/join")
    public String JoinPage(){

        return "join";
    }

    @GetMapping("/social")
    public String SocialLoginPage(){
        return "socialLogin";
    }

}

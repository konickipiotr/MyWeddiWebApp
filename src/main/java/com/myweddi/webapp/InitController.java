package com.myweddi.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class InitController {

    @GetMapping("/login")
    public String login(Model model, String message, String errormessage){
        model.addAttribute("message", message);
        model.addAttribute("errormessage", errormessage);
        return "login";
    }

    @GetMapping
    public String home(){
        return "login";
    }
}

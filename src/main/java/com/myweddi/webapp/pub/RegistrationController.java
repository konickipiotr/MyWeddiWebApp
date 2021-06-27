package com.myweddi.webapp.pub;

import com.myweddi.config.GlobalConfig;
import com.myweddi.config.Msg;
import com.myweddi.modules.registration.RegistrationForm;
import com.myweddi.users.authentication.Auth;
import com.myweddi.users.authentication.AuthRepository;
import com.myweddi.users.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final AuthRepository  authRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public RegistrationController(AuthRepository authRepository, RestTemplate restTemplate) {
        this.authRepository = authRepository;
        this.restTemplate = restTemplate;
    }

    private final String ROOT_PATH = GlobalConfig.domain + "/api/registration";

    @GetMapping
    public String goToRegistrationPage(Model model){
        model.addAttribute("registrationForm", new RegistrationForm());
        model.addAttribute("services", ServiceUtils.serviceTypeName);
        model.addAttribute("sevicerange", ServiceUtils.rangeTypeName);
        return "public/registration/registration";
    }

    @PostMapping
    public String registerUser(RegistrationForm rf, Model model, Principal principal){
        ResponseEntity<Long> response;
        try{
            response = restTemplate.postForEntity(ROOT_PATH, rf, Long.class);
        }catch (HttpClientErrorException e){
            model.addAttribute("error_message", e.getResponseBodyAsString());
            return "public/registration/registration";
        }
        Long id = response.getBody();
        Optional<Auth> oAuth = authRepository.findById(id);
        if(oAuth.isEmpty()){
            model.addAttribute("error_message",Msg.SOMETHING_WENT_WRONG);
            return "error";
        }
        Auth auth = oAuth.get();

        model.addAttribute("userid", auth.getId());
        model.addAttribute("accept_message", "Konto zostało założone. Na email " + auth.getUsername() + " został wysłany link aktywacyjny");
        return "public/registration/registration_complete";
    }

    @PostMapping("/sendagain")
    public String sendActivationLinkAgain(@RequestParam("userid") Long userid, Model model){
        String path = ROOT_PATH + "/sendagain";
        try{
            restTemplate.postForEntity(path, userid, String.class);
        }catch (HttpClientErrorException e){
            model.addAttribute("error_message", e.getResponseBodyAsString());
            return "error";
        }

        Optional<Auth> oAuth = authRepository.findById(userid);
        if(oAuth.isEmpty()){
            model.addAttribute("error_message",Msg.SOMETHING_WENT_WRONG);
            return "error";
        }
        Auth auth = oAuth.get();

        model.addAttribute("userid", userid);
        model.addAttribute("accept_message", "Na email " + auth.getUsername() + " ponownie został wysłany link aktywacyjny");
        return "public/registration/registration_complete";
    }

    @GetMapping("/activation/{activationcode}")
    public String activateAccount(@PathVariable("activationcode") String activationcode, Model model){

        String path = ROOT_PATH + "/activate";
        try{
            restTemplate.postForEntity(path, activationcode, Long.class);
            model.addAttribute("accept_message", Msg.ACCOUNT_ACTIVE);
        }catch (HttpClientErrorException e){
            model.addAttribute("error_message", e.getResponseBodyAsString());
        }

        return "login";
    }

}

package com.myweddi.api.registration;

import com.myweddi.config.Msg;
import com.myweddi.modules.registration.ActivationCode;
import com.myweddi.modules.registration.ActivationCodeRepository;
import com.myweddi.modules.registration.CodeGenerator;
import com.myweddi.modules.registration.RegistrationForm;
import com.myweddi.users.authentication.AccountStatus;
import com.myweddi.users.authentication.AccountType;
import com.myweddi.users.authentication.Auth;
import com.myweddi.users.authentication.AuthRepository;
import com.myweddi.utils.EmailSender;
import com.myweddi.utils.SentMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/registration")
public class RegistrationAPIController {

    private final AuthRepository authRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeGenerator codeGenerator;
    private final EmailSender emailSender;
    private final CreateUserService createUserService;

    @Autowired
    public RegistrationAPIController(AuthRepository authRepository, ActivationCodeRepository activationCodeRepository, PasswordEncoder passwordEncoder, CodeGenerator codeGenerator, EmailSender emailSender, CreateUserService createUserService) {
        this.authRepository = authRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.codeGenerator = codeGenerator;
        this.emailSender = emailSender;
        this.createUserService = createUserService;
    }

    @PostMapping
    public ResponseEntity<Long> registerUser(@RequestBody RegistrationForm rf){
        Optional<Auth> oUsername = this.authRepository.findByUsername(rf.getUsername());
        if(oUsername.isPresent())
            return new ResponseEntity(Msg.USERNAME_EXISTS, HttpStatus.FOUND);

        Auth auth = new Auth(rf.getUsername(), passwordEncoder.encode(rf.getPassword()), AccountType.valueOf(rf.getAccountType()));
        this.authRepository.save(auth);

        String newActivationCode = codeGenerator.generateActivationCode();
        this.activationCodeRepository.save(new ActivationCode(auth.getId(), newActivationCode));

        emailSender.sendTo(rf.getUsername());
        emailSender.setMsgType(SentMessageType.ACTIVATION);
        emailSender.addParam(newActivationCode);
        emailSender.sendEmail();

        createUserService.createUserAccount(auth, rf);
        return new ResponseEntity(auth.getId(), HttpStatus.OK);
    }

    @PostMapping("/activate")
    public ResponseEntity<Long> activate(@RequestBody String activateCode){

        Optional<ActivationCode> oActivationCode = this.activationCodeRepository.findByActivationCode(activateCode);
        if(oActivationCode.isEmpty())
            return new ResponseEntity(Msg.ACTIVATION_CODE_NOT_FOUND, HttpStatus.NOT_FOUND);

        ActivationCode activationCode = oActivationCode.get();
        Optional<Auth> oAuth = this.authRepository.findById(activationCode.getUserid());
        if(oAuth.isEmpty())
            return new ResponseEntity(Msg.USER_NOT_FOUND, HttpStatus.NOT_FOUND);

        Auth auth = oAuth.get();
        auth.setAccountStatus(AccountStatus.ACTIVE);
        this.activationCodeRepository.delete(activationCode);
        return new ResponseEntity(auth.getId(), HttpStatus.OK);
    }

    @PostMapping("/sendagain")
    public ResponseEntity<Void> sendActivationCodeAgain(@RequestBody Long userid){
        Optional<ActivationCode> optionalActivation = this.activationCodeRepository.findById(userid);
        if(optionalActivation.isEmpty())
            return new ResponseEntity(Msg.ACTIVATION_CODE_NOT_FOUND, HttpStatus.NOT_FOUND);

        ActivationCode activation = optionalActivation.get();
        String newActivationCode = codeGenerator.generateActivationCode();
        activation.setActivationCode(newActivationCode);
        this.activationCodeRepository.save(activation);

        Optional<Auth> oUser = this.authRepository.findById(userid);
        if(oUser.isEmpty())
            return new ResponseEntity(Msg.USER_NOT_FOUND, HttpStatus.NOT_FOUND);

        Auth auth = oUser.get();
        String email = auth.getUsername();
        emailSender.sendTo(email);
        emailSender.setMsgType(SentMessageType.ACTIVATION);
        emailSender.addParam(newActivationCode);
        emailSender.sendEmail();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

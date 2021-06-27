package com.myweddi.utils;

import com.myweddi.config.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailSender {

    private String to;
    private SentMessageType sentMessageType;
    private List<String> parms = new ArrayList<>();
    private JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendTo(String to){
        this.to = to;
    }

    public void setMsgType(SentMessageType sentMessageType){
        this.sentMessageType = sentMessageType;
    }

    public void addParam(String param){
        parms.add(param);
    }

    public void sendEmail(){
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(to);
        emailMessage.setSubject(prepare_topic());
        emailMessage.setText(prepare_message());

        javaMailSender.send(emailMessage);
    }

    private String prepare_message(){
        String message = "";
        switch (this.sentMessageType){
            case ACTIVATION:
                String link = GlobalConfig.domain + "/registration/activation/" + parms.get(0);

                message = "Witaj w MyWeddi!\n\n" +
                        "Kliknij w link poniżej aby aktywować konto \n" + link + "\n\n\n Link będzie aktywny przez tydzień";
            case PASSWORD_RESET:
        }
        return message;
    }

    private String prepare_topic(){
        switch (this.sentMessageType){
            case ACTIVATION: return "MyWeddi - aktywacja";
            case PASSWORD_RESET: return "MyWeddi - przypomnienei hasła";
        }
        return "";
    }
}

package com.myweddi.modules.registration;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ActivationCode {

    @Id
    private Long userid;
    private String activationCode;

    public ActivationCode(Long userid, String activationCode) {
        this.userid = userid;
        this.activationCode = activationCode;
    }

    public ActivationCode() {
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}

package com.myweddi.users.model;

import com.myweddi.modules.registration.RegistrationForm;
import com.myweddi.users.AccountVersion;
import com.myweddi.users.authentication.Auth;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private Long id;
    private String firstname;
    private String lastname;
    private Long activeweddingid;
    private String phone;
    private Long locationid;
    private int birthyear;
    private char gender;

    public User(RegistrationForm rf, Auth auth){
        this.id = auth.getId();
        this.firstname = rf.getFirstname();
        this.lastname = rf.getLastname();
        this.phone = rf.getPhone();
        this.birthyear = Integer.valueOf(rf.getBirthyear());
        this.gender = rf.getGender().charAt(0);
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Long getActiveweddingid() {
        return activeweddingid;
    }

    public void setActiveweddingid(Long activeweddingid) {
        this.activeweddingid = activeweddingid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getLocationid() {
        return locationid;
    }

    public void setLocationid(Long locationid) {
        this.locationid = locationid;
    }

    public int getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }
}

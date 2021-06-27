package com.myweddi.users.model;

import com.myweddi.modules.registration.RegistrationForm;
import com.myweddi.users.AccountVersion;
import com.myweddi.users.ServiceType;
import com.myweddi.users.authentication.Auth;

import javax.persistence.*;

@Entity
public class ServiceAccount {

    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private AccountVersion accountVersion;
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    private Long locationid;
    private int operationdistance;
    private String description;


    public ServiceAccount() {
    }

    public ServiceAccount(RegistrationForm rf, Auth auth) {
        this.id = auth.getId();
        this.name = rf.getName();
        this.accountVersion = AccountVersion.NORMAL;
        this.serviceType = ServiceType.valueOf(rf.getServiceType());
        try {
            this.operationdistance = Integer.valueOf(rf.getRange());
        }catch (NumberFormatException e){

        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountVersion getAccountVersion() {
        return accountVersion;
    }

    public void setAccountVersion(AccountVersion accountVersion) {
        this.accountVersion = accountVersion;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Long getLocationid() {
        return locationid;
    }

    public void setLocationid(Long locationid) {
        this.locationid = locationid;
    }

    public int getOperationdistance() {
        return operationdistance;
    }

    public void setOperationdistance(int operationdistance) {
        this.operationdistance = operationdistance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

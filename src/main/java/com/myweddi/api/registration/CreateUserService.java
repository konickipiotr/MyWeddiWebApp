package com.myweddi.api.registration;

import com.myweddi.modules.registration.RegistrationForm;
import com.myweddi.users.authentication.AccountType;
import com.myweddi.users.authentication.Auth;
import com.myweddi.users.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateUserService {

    private UserRepository userRepository;
    private LocationRepository locationRepository;
    private ServiceAccountRepository serviceAccountRepository;

    @Autowired
    public CreateUserService(UserRepository userRepository, LocationRepository locationRepository, ServiceAccountRepository serviceAccountRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.serviceAccountRepository = serviceAccountRepository;
    }

    public void createUserAccount(Auth auth, RegistrationForm rf){
        if(rf.getAccountType().equals("PRIVATE"))
            createPrivateUserAccount(auth, rf);
        else
            createServiceUserAccount(auth, rf);
    }

    private void createPrivateUserAccount(Auth auth, RegistrationForm rf){
        User user = new User(rf, auth);
        String slocation = rf.getLocation();
        if(slocation != null && !slocation.isBlank()){
            Optional<Location> oName = locationRepository.findByName(rf.getName());
            if(oName.isPresent()){
                user.setLocationid(oName.get().getId());
            }else {
                Location location = new Location(rf.getName());
                this.locationRepository.save(location);
                user.setLocationid(location.getId());
            }
        }
        this.userRepository.save(user);
    }

    private void createServiceUserAccount(Auth auth, RegistrationForm rf){
        ServiceAccount serviceAccount = new ServiceAccount(rf, auth);
        String slocation = rf.getLocation();
        if(slocation != null && !slocation.isBlank()){
            Optional<Location> oName = locationRepository.findByName(rf.getName());
            if(oName.isPresent()){
                serviceAccount.setLocationid(oName.get().getId());
            }else {
                Location location = new Location(rf.getName());
                this.locationRepository.save(location);
                serviceAccount.setLocationid(location.getId());
            }
        }
        this.serviceAccountRepository.save(serviceAccount);
    }
}

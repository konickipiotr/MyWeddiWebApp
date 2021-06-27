package com.myweddi.config;

import com.myweddi.users.authentication.AccountType;
import com.myweddi.users.authentication.Auth;
import com.myweddi.users.authentication.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

//@Service
public class DbInit implements CommandLineRunner {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DbInit(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        this.authRepository.deleteAll();

        Auth adminAuth = new Auth("admin", passwordEncoder.encode("11"), AccountType.ADMIN);
        Auth userAuth1 = new Auth("testuser1", passwordEncoder.encode("11"), AccountType.ADMIN);
        Auth userAuth2 = new Auth("testuser2", passwordEncoder.encode("11"), AccountType.ADMIN);
        Auth userAuth3 = new Auth("testuser3", passwordEncoder.encode("11"), AccountType.ADMIN);
        Auth userAuth4 = new Auth("testuser4", passwordEncoder.encode("11"), AccountType.ADMIN);
        Auth userAuth5 = new Auth("testuser5", passwordEncoder.encode("11"), AccountType.ADMIN);

        Auth djServiceAuth = new Auth("testdj", passwordEncoder.encode("11"), AccountType.ADMIN);
        Auth photoServiceAuth = new Auth("testphoto", passwordEncoder.encode("11"), AccountType.ADMIN);
        Auth weddinghServiceAuth = new Auth("testwedddingh", passwordEncoder.encode("11"), AccountType.ADMIN);
        this.authRepository.saveAll(Arrays.asList(adminAuth, userAuth1, userAuth2, userAuth3,
                userAuth4, userAuth5, djServiceAuth, photoServiceAuth,weddinghServiceAuth));

    }
}

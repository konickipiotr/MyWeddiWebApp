package com.myweddi.api.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.config.GlobalConfig;
import com.myweddi.modules.registration.ActivationCode;
import com.myweddi.modules.registration.ActivationCodeRepository;
import com.myweddi.modules.registration.CodeGenerator;
import com.myweddi.modules.registration.RegistrationForm;
import com.myweddi.users.AccountVersion;
import com.myweddi.users.ServiceType;
import com.myweddi.users.authentication.AccountStatus;
import com.myweddi.users.authentication.AccountType;
import com.myweddi.users.authentication.Auth;
import com.myweddi.users.authentication.AuthRepository;
import com.myweddi.users.model.*;
import com.myweddi.utils.EmailSender;
import com.myweddi.utils.SentMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class RegistrationAPIControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivationCodeRepository activationCodeRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ServiceAccountRepository serviceAccountRepository;

    @MockBean
    private CodeGenerator codeGenerator;
    @MockBean
    private EmailSender emailSender;

    private RegistrationForm user;
    private RegistrationForm service;

    @Autowired
    public RegistrationAPIControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, AuthRepository authRepository, PasswordEncoder passwordEncoder, ActivationCodeRepository activationCodeRepository, UserRepository userRepository, LocationRepository locationRepository, ServiceAccountRepository serviceAccountRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.activationCodeRepository = activationCodeRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.serviceAccountRepository = serviceAccountRepository;
    }

    @BeforeEach
    void setUp() {
        authRepository.deleteAll();
        activationCodeRepository.deleteAll();
        userRepository.deleteAll();
        locationRepository.deleteAll();
        serviceAccountRepository.deleteAll();

        user = new RegistrationForm();
        user.setAccountType("PRIVATE");
        user.setUsername("jan@xx.pl");
        user.setPassword("111111");
        user.setPasswordagain("111111");
        user.setBirthyear(1983);
        user.setGender("M");
        user.setLocation("Wrocław");
        user.setPhone("7854188");

        service = new RegistrationForm();
        service.setAccountType("SERVICE");
        service.setUsername("dancecenter@xx.pl");
        service.setPassword("111111");
        service.setPasswordagain("111111");
        service.setLocation("Wrocław");
        service.setPhone("7854188");
        service.setName("dancecenter");
        service.setRange("20");
        service.setServiceType("DANCE");
    }

    private void  do_not_send_email(){
        Mockito.doNothing().when(emailSender).sendTo(user.getUsername());
        Mockito.doNothing().when(emailSender).setMsgType(SentMessageType.ACTIVATION);
        Mockito.doNothing().when(emailSender).addParam("11");
        Mockito.doNothing().when(emailSender).sendEmail();
    }

    @Test
    void create_new_user_account() throws Exception {

        LocalDateTime before = LocalDateTime.now(GlobalConfig.zid);
        do_not_send_email();


        assertEquals(0, this.authRepository.findAll().size());
        mockMvc.perform(post("/api/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        LocalDateTime after = LocalDateTime.now(GlobalConfig.zid);
        List<Auth> all = this.authRepository.findAll();
        assertEquals(1, all.size());

        Auth auth = all.get(0);

        assertEquals(user.getUsername(), auth.getUsername());
        assertTrue(auth.getCreated().isAfter(before) && auth.getCreated().isBefore(after));
        assertEquals(AccountType.PRIVATE, auth.getAccountType());
        assertEquals(AccountStatus.INACTIVE, auth.getAccountStatus());
        assertTrue(passwordEncoder.matches(user.getPassword(), auth.getPassword()));
    }

    @Test
    void create_new_company_account() throws Exception {

        LocalDateTime before = LocalDateTime.now(GlobalConfig.zid);
        do_not_send_email();

        assertEquals(0, this.authRepository.findAll().size());
        mockMvc.perform(post("/api/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(service)))
                .andExpect(status().isOk());

        LocalDateTime after = LocalDateTime.now(GlobalConfig.zid);
        List<Auth> all = this.authRepository.findAll();
        assertEquals(1, all.size());

        Auth auth = all.get(0);

        assertEquals(service.getUsername(), auth.getUsername());
        assertTrue(auth.getCreated().isAfter(before) && auth.getCreated().isBefore(after));
        assertEquals(AccountType.SERVICE, auth.getAccountType());
        assertEquals(AccountStatus.INACTIVE, auth.getAccountStatus());
        assertTrue(passwordEncoder.matches(service.getPassword(), auth.getPassword()));
    }

    @Test
    void return_found_if_user_exist() throws Exception {
        this.authRepository.save(new Auth("jan@xx.pl", "11", AccountType.PRIVATE));
        do_not_send_email();

        RegistrationForm rf = new RegistrationForm();
        rf.setAccountType("PRIVATE");
        rf.setUsername("jan@xx.pl");


        assertEquals(1, this.authRepository.findAll().size());
        mockMvc.perform(post("/api/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rf)))
                .andExpect(status().isFound());

        List<Auth> all = this.authRepository.findAll();
        assertEquals(1, all.size());
    }

    @Test
    void generate_activation_code() throws Exception {
        do_not_send_email();

        String code = "123";
        Mockito.when(codeGenerator.generateActivationCode()).thenReturn(code);

        mockMvc.perform(post("/api/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        List<ActivationCode> activationCodeList = this.activationCodeRepository.findAll();
        assertEquals(code, activationCodeList.get(0).getActivationCode());
    }

    @Test
    void activate_account() throws Exception {
        do_not_send_email();

        String code = "123";
        Mockito.when(codeGenerator.generateActivationCode()).thenReturn(code);


        mockMvc.perform(post("/api/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        Auth auth = this.authRepository.findAll().get(0);
        assertEquals(AccountStatus.INACTIVE, auth.getAccountStatus());
        assertFalse(this.activationCodeRepository.findAll().isEmpty());

        mockMvc.perform(post("/api/registration/activate").content(code))
                .andExpect(status().isOk());

        assertTrue(this.activationCodeRepository.findAll().isEmpty());
        auth = this.authRepository.findAll().get(0);
        assertEquals(AccountStatus.ACTIVE, auth.getAccountStatus());
    }

    @Test
    void if_account_type_is_private_user_create_record_in_db() throws Exception {
        do_not_send_email();

        mockMvc.perform(post("/api/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        Auth auth = this.authRepository.findAll().get(0);
        User userA = this.userRepository.findAll().get(0);

        assertEquals(auth.getId(), userA.getId());
        assertNull(userA.getActiveweddingid());
        assertEquals(user.getPhone(), userA.getPhone());
        assertEquals(user.getFirstname(), userA.getFirstname());
        assertEquals(user.getLastname(), userA.getLastname());
        assertEquals(user.getGender().charAt(0), userA.getGender());
        assertEquals(user.getBirthyear(), userA.getBirthyear());
        assertNotNull(userA.getLocationid());


        Optional<Location> oLocation = this.locationRepository.findById(userA.getLocationid());
        if(oLocation.isPresent()) {
            Location location = oLocation.get();
            assertNotNull(location.getId());
            assertEquals(user.getName(), location.getName());
            assertFalse(location.isVerified());
            assertNull(location.getLatitude());
            assertNull(location.getLongitude());
        }
    }

    @Test
    void create_service_account() throws Exception {
        do_not_send_email();

        mockMvc.perform(post("/api/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(service)))
                .andExpect(status().isOk());

        Auth auth = this.authRepository.findAll().get(0);
        ServiceAccount serviceAccount = this.serviceAccountRepository.findAll().get(0);

        assertEquals(auth.getId(), serviceAccount.getId());
        assertEquals(AccountVersion.NORMAL, serviceAccount.getAccountVersion());
        assertEquals(ServiceType.DANCE, serviceAccount.getServiceType());
        assertEquals(20, serviceAccount.getOperationdistance());
    }
}

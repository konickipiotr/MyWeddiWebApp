package com.myweddi.modules.registration;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

@Service
public class CodeGenerator {

    private ActivationCodeRepository activationCodeRepository;

    public CodeGenerator(ActivationCodeRepository activationCodeRepository) {
        this.activationCodeRepository = activationCodeRepository;
    }

    private final int ACTIVATION_CODE_LENGTH = 40;

    public String generateActivationCode(){

        String code = RandomString.make(ACTIVATION_CODE_LENGTH);
        while (this.activationCodeRepository.existsByActivationCode(code)){
            code = RandomString.make(ACTIVATION_CODE_LENGTH);
        }
        return code;
    }
}

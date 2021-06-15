package com.bci.usuarios.phone;

import com.bci.usuarios.exceptions.ApiRequestException;
import com.bci.usuarios.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PhoneService {

    private final PhoneRepository phoneRepository;

    @Autowired
    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    public Phone addPhone(Phone phone){

        if (phone.getNumber() == null
                || phone.getNumber().length() == 0){
            throw new IllegalStateException("Telefono no suministrado");
        }

        if (phone.getCitycode() == null
                || phone.getCitycode().length() == 0){
            throw new ApiRequestException("Código de ciudad no suministrado");
        }

        if (phone.getCountrycode() == null
                || phone.getCountrycode().length() == 0){
            throw new ApiRequestException("Código de país no suministrado");
        }

        phoneRepository.save(phone);
        return phone;
    }

}

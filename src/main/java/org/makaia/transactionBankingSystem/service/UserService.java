package org.makaia.transactionBankingSystem.service;

import org.makaia.transactionBankingSystem.dto.dtoPerson.DTOPersonCreate;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.User;
import org.makaia.transactionBankingSystem.repository.UserRepository;
import org.makaia.transactionBankingSystem.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    UserRepository userRepository;
    UserValidation userValidation;

    @Autowired
    public UserService(UserRepository userRepository, UserValidation userValidation)
    {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
    }

    public UserDetails getUserByUserName(String userName) throws ApiException {
        userValidation.getUserValidation(userName);
        UserDetails user = this.userRepository.findByPersonId(userName);
        List<String> error = List.of("El usuario con nombre de usuario '" + userName + "' no se encuentra en la base de datos.");
        if (user == null){
            throw new ApiException (404, error);
        }
        return user;
    }

    public UserDetails createUser(DTOPersonCreate dtoPersonCreate) throws ApiException {
        userValidation.createUserValidation(dtoPersonCreate.getPassword());
        BCryptPasswordEncoder pass = new BCryptPasswordEncoder();
        String password = pass.encode(dtoPersonCreate.getPassword());
        dtoPersonCreate.setPassword(password);
        return this.userRepository.save(new User(dtoPersonCreate.getPassword(), dtoPersonCreate.getPerson()));
    }
}

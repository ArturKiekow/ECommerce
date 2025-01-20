package com.projetoartur.ecommerce.service;

import com.projetoartur.ecommerce.dto.CreateUserDto;
import com.projetoartur.ecommerce.entities.BillingAddressEntity;
import com.projetoartur.ecommerce.entities.UserEntity;
import com.projetoartur.ecommerce.repository.BillingAddressRepository;
import com.projetoartur.ecommerce.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.billingAddressRepository = billingAddressRepository;
    }


    public List<UserEntity> getAllUsers() {
        var users = userRepository.findAll();
        return users;
    }



    public UserEntity createUser(CreateUserDto dto){

        BillingAddressEntity billingAddress = new BillingAddressEntity();
        billingAddress.setAddress(dto.address());
        billingAddress.setNumber(dto.number());
        billingAddress.setComplement(dto.complement());

        //var savedBillingAddress = billingAddressRepository.save(billingAddress);

        UserEntity user = new UserEntity();
        user.setFullName(dto.fullName());
        user.setBillingAddress(billingAddress);

        return userRepository.save(user);
    }


    public Optional<UserEntity> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    public boolean deleteUserById(UUID userId) {
        var user = userRepository.findById(userId);

        if (user.isPresent()){
            userRepository.deleteById(userId);
           // billingAddressRepository.deleteById(user.get().getBillingAddress().getBillingAddressId());
        }
        return user.isPresent();
    }
}

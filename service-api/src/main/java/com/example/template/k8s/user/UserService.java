package com.example.template.k8s.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDetailRepository userDetailRepository;
    
    
    public Optional<UserDetail> getUserInfo(String username) {
    	return userDetailRepository.findById(username);
    }
    
}

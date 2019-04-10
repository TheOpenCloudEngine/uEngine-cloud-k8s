package com.example.template.k8s.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService {

    @Autowired
    UserDetailRepository userDetailRepository;

    public UserDetail getUserDetail(String username){
        new UserDetail().setUsername(username);
        return userDetailRepository.findById(username).orElseGet(()-> {
            UserDetail userDetail = new UserDetail();
            userDetail.setUsername(username);
            return userDetail;
        });
    }

    public Iterable<UserDetail> getUserDetailList(){
        return userDetailRepository.findAll();
    }

    public void save(UserDetail userDetail){
        userDetailRepository.save(userDetail);
    }

}

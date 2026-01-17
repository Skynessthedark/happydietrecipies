package com.happydieting.dev.service;

import com.happydieting.dev.data.UserData;
import com.happydieting.dev.model.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final ModelMapper modelMapper;

    public UserService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserData convertModel2Data(UserModel user) {
        if(Objects.isNull(user)) return null;

        UserData userData = modelMapper.map(user, UserData.class);
        userData.setPassword(null);
        return userData;
    }
}

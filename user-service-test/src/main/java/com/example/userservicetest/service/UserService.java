package com.example.userservicetest.service;

import com.example.userservicetest.dto.UserDto;
import com.example.userservicetest.jpa.UserEntity;

public interface UserService  {
//public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();

}

package com.example.userservicetest.service;

import com.example.userservicetest.dto.UserDto;

public interface UserService  {
//public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

}

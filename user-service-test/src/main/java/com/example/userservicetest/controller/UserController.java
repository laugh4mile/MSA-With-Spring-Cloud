package com.example.userservicetest.controller;

import com.example.userservicetest.dto.UserDto;
import com.example.userservicetest.jpa.UserEntity;
import com.example.userservicetest.jpa.UserRepository;
import com.example.userservicetest.service.UserService;
import com.example.userservicetest.vo.RequestUser;
import com.example.userservicetest.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user-service-test")
public class UserController {
    private UserService userService;
    private Environment env;

    @Autowired
    public UserController(UserService userService, Environment env){
        this.userService = userService;
        this.env = env;
    }

    @GetMapping("/health_check")
    public String status(){
        return String.format("포트번호 : %s ",
                env.getProperty("local.server.port"));
    }


    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result =  new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserDto userDto = userService.getUserByUserId(userId);

        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);

    }

    @CrossOrigin(origins = "http://192.168.61.252:3000")
    @PostMapping("/addTodos")
    public int addTodos(){
        System.out.println("들어 왔나~~");
        return 1212121;
    }
}

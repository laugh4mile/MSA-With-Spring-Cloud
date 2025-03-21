package com.example.userservicetest.service;
import com.example.userservicetest.dto.UserDto;
import com.example.userservicetest.jpa.UserEntity;
import com.example.userservicetest.jpa.UserRepository;
import com.example.userservicetest.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
//        userEntity.setEncryptedPwd("encrypted_password");
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null){
            throw new UsernameNotFoundException("User not fount");
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }


//    Environment env;
//    RestTemplate restTemplate;
//
//    OrderServiceClient orderServiceClient;
//
//    CircuitBreakerFactory circuitBreakerFactory;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserEntity userEntity = userRepository.findByEmail(username);
//
//        if (userEntity == null)
//            throw new UsernameNotFoundException(username + ": not found");
//
//        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
//                true, true, true, true,
//                new ArrayList<>());
//    }
//
//    @Autowired
//    public UserServiceImpl(UserRepository userRepository,
//                           BCryptPasswordEncoder passwordEncoder,
//                           Environment env,
//                           RestTemplate restTemplate,
//                           OrderServiceClient orderServiceClient,
//                           CircuitBreakerFactory circuitBreakerFactory) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.env = env;
//        this.restTemplate = restTemplate;
//        this.orderServiceClient = orderServiceClient;

//        this.circuitBreakerFactory = circuitBreakerFactory;

//    }
//    @Override
//    public UserDto getUserByUserId(String userId) {
//        UserEntity userEntity = userRepository.findByUserId(userId);
//
//        if (userEntity == null)
//            throw new UsernameNotFoundException("User not found");
//
//        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
//
////        List<ResponseOrder> orders = new ArrayList<>();
//        /* Using as rest template */
////        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
////        ResponseEntity<List<ResponseOrder>> orderListResponse =
////                restTemplate.exchange(orderUrl, HttpMethod.GET, null,
////                                            new ParameterizedTypeReference<List<ResponseOrder>>() {
////                });
////        List<ResponseOrder> ordersList = orderListResponse.getBody();
//
//        /* Using a feign client */
//        /* Feign exception handling */
////        List<ResponseOrder> ordersList = null;
////        try {
////            ordersList = orderServiceClient.getOrders(userId);
////        } catch (FeignException ex) {
////            log.error(ex.getMessage());
////        }
//
//        /* ErrorDecoder */
//        List<ResponseOrder> ordersList = orderServiceClient.getOrders(userId);
////        log.info("Before call orders microservice");
////        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
////        List<ResponseOrder> ordersList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
////                throwable -> new ArrayList<>());
////        log.info("After called orders microservice");
//
//        userDto.setOrders(ordersList);
//
//        return userDto;
//    }
//
//
//    @Override
//    public UserDto getUserDetailsByEmail(String email) {
//        UserEntity userEntity = userRepository.findByEmail(email);
//        if (userEntity == null)
//            throw new UsernameNotFoundException(email);
//
//        ModelMapper mapper = new ModelMapper();
//        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//        UserDto userDto = mapper.map(userEntity, UserDto.class);
//        return userDto;
//    }
}

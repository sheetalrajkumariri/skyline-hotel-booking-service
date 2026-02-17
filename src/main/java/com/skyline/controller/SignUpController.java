package com.skyline.controller;

import com.skyline.dto.UsersRequest;
import com.skyline.dto.UsersResponse;
import com.skyline.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SignUpController {
    @Autowired
    public UsersService usersService;

    @PostMapping("/sign-up")
    public UsersResponse createUser(@RequestBody UsersRequest request){
        log.info("Start:: createUser()inside the AuthController with request, {} ", request);
        return usersService.createUser(request);
    }
}

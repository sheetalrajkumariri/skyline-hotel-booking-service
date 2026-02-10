package com.skyline.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.skyline.dto.UsersRequest;
import com.skyline.dto.UsersResponse;
import com.skyline.service.UsersService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    @Autowired
    public UsersService usersService;

    @GetMapping("/get/{userId}")
    public UsersResponse findUserById(@PathVariable int userId){
        log.info("Start:: FindUserById()inside the UserController with id, {} ", userId);
        return usersService.findUserById(userId);
    }

    @GetMapping("/list")
    public List<UsersResponse> findAllUser(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy, @RequestParam String sortDir){
        log.info("Start::findAllUser() inside UserController | page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);
        return usersService.findAllUser(page, size, sortBy, sortDir);
    }
    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable int userId){
        log.info("Start:: DeleteUserById()inside the UserController with id, {} ", userId);
        return usersService.deleteUserById(userId);
    }

    @PutMapping("/update/{userId}")
    public UsersResponse updateUserById(@PathVariable int userId, @RequestBody UsersRequest request){
        log.info("Start:: UpdateUserById()inside the UserController with id, {} ", userId);
        return usersService.updateUserById(userId,request);
    }

}

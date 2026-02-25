package com.skyline.dto;

import lombok.Data;

import java.util.List;

@Data
public class UsersResponse {
    private int id;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String password;

}

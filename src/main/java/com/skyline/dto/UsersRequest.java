package com.skyline.dto;

import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

@Data
public class UsersRequest {
    private String username;
    private String email;
    private String phone;
    private String password;
    private String role;


}

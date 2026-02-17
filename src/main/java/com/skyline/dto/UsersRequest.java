package com.skyline.dto;

import lombok.Data;
import org.jspecify.annotations.Nullable;

@Data
public class UsersRequest {
    private String username;
    private String email;
    private String phone;
    private String password;


}

package com.skyline.service;

import com.skyline.dto.UsersRequest;
import com.skyline.dto.UsersResponse;

import java.util.List;

public interface UsersService {
    UsersResponse createUser(UsersRequest request);

    UsersResponse findUserById(int userId);

    List<UsersResponse> findAllUser(int page, int size, String sortBy, String sortDir);

    String deleteUserById(int userId);

    UsersResponse updateUserById(int userId, UsersRequest request);
}

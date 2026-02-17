package com.skyline.service.impl;

import com.skyline.entity.Role;
import com.skyline.exception.EmailAlreadyExistsException;
import com.skyline.repository.RoleRepository;
import com.skyline.util.EmailValidator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.skyline.dto.UsersRequest;
import com.skyline.dto.UsersResponse;
import com.skyline.entity.Users;
import com.skyline.exception.NotFoundException;
import com.skyline.repository.UsersRepository;
import com.skyline.service.UsersService;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsersResponse createUser(UsersRequest request) {
        log.info("Start:: createUser()inside the UserServiceImpl with request, {} ", request);
        if (!EmailValidator.isValid(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }
        Users user = modelMapper.map(request, Users.class);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        Role role = roleRepository.findRoleByName("USER").orElseThrow(() -> new NotFoundException("Role not found with name: " + "User"));
        user.setRoles(Set.of(role));
        user = usersRepository.save(user);
        UsersResponse usersResponse = modelMapper.map(user, UsersResponse.class);
        usersResponse.setRole(user.getRoles().stream().findFirst().get().getName());
        log.info("End:: createUser()inside the UserServiceImpl with request, {} ", request);
        return usersResponse;
    }

    @Override
    public UsersResponse findUserById(int userId) {
        log.info("Start:: findUserById()inside the UserServiceImpl with id, {} ", userId);
        Users users = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        UsersResponse response = modelMapper.map(users, UsersResponse.class);

        //  Manual role mapping
        if (users.getRoles() != null && !users.getRoles().isEmpty()) {
            response.setRole(users.getRoles().stream().findFirst().get().getName());
        }

        log.info("End:: findUserById() inside the UserServiceImpl with id, {}", userId);

        return response;
    }

    @Override
    public List<UsersResponse> findAllUser(int page, int size, String sortBy, String sortDir) {
        log.info("Start:: findAllUser() inside UserServiceImpl");

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Users> customerPage = usersRepository.findAll(pageable);

        List<UsersResponse> responseList = customerPage.getContent().stream().map(user -> {
            UsersResponse response = modelMapper.map(user, UsersResponse.class);

            // Manual role mapping
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                response.setRole(user.getRoles().stream().findFirst().get().getName());
            }

            return response;
        }).toList();

        log.info("End:: findAllUser() inside UserServiceImpl");

        return responseList;
    }

    @Override
    public String deleteUserById(int userId) {
        log.info("Start:: DeleteUserById()inside the UserServiceImpl with id, {} ", userId);
        Users users = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        usersRepository.delete(users);
        log.info("End:: DeleteUserById()inside the UserServiceImpl with id, {} ", userId);
        return "User deleted successfully";
    }

    @Override
    public UsersResponse updateUserById(int userId, UsersRequest request) {
        log.info("Start:: UpdateUserById()inside the UserServiceImpl with id, {} ", userId);
        Users users = usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        modelMapper.map(request, Users.class);
        Users update = usersRepository.save(users);
        log.info("End:: UpdateUserById()inside the UserServiceImpl with id, {} ", userId);
        return modelMapper.map(update, UsersResponse.class);
    }
}

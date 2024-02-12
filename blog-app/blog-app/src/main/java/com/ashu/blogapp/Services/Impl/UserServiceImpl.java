package com.ashu.blogapp.Services.Impl;

import com.ashu.blogapp.Entity.User;
import com.ashu.blogapp.Exceptions.ResourceNotFoundException;
import com.ashu.blogapp.Payloads.UserDto;
import com.ashu.blogapp.Repository.UserRepo;
import com.ashu.blogapp.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    // conversion of "User" to "UserDto" & vice-versa(this is temp conversion for permanent conversion we May use ModelMappers)
//    // as we are using "User" only for DB exposure but for performing operation we would use "UserDto"
//    public User dtoTOuser(UserDto userDto){
//        User user = new User();
//
//        user.setId(userDto.getId());
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setAbout(userDto.getAbout());
//        user.setPassword(userDto.getPassword());
//
//        return user;
//    }
//
//    public UserDto userTOdto(User user){
//        UserDto userDto = new UserDto();
//
//        userDto.setId(user.getId());
//        userDto.setName(user.getName());
//        userDto.setEmail(user.getEmail());
//        userDto.setAbout(user.getAbout());
//        userDto.setPassword(user.getPassword());
//
//        return userDto;
//    }

    // conversion of "User" to "UserDto" & vice-versa using ModelMappers Library
    // 1st add modelmapper dependency
    // Declare a bean of ModelMapper... then by Autowiring we can use it for conversion purpose
    @Autowired
    private ModelMapper modelMapper;

    public User dtoTOuser(UserDto userDto){
        User user = this.modelMapper.map(userDto, User.class);
        return user;
    }

    public UserDto userTOdto(User user){
        UserDto userDto = this.modelMapper.map(user, UserDto.class);

        return userDto;
    }




    @Override
    public UserDto createUser(UserDto userDto) {
        // converting userdtoTOuser
        User user = this.dtoTOuser(userDto);

        //performing opration
        User savedUser = this.userRepo.save(user);

        // returning with conversion back to userTOuserdto
        return this.userTOdto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepo.save(user);


        UserDto userDto1 = this.userTOdto(updatedUser);

        return userDto1;
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return this.userTOdto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();


        List<UserDto> userDtos = users.stream().map(user -> this.userTOdto(user)).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        this.userRepo.delete(user);

    }





}

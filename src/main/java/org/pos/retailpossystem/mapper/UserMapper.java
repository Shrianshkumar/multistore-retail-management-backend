package org.pos.retailpossystem.mapper;

import org.pos.retailpossystem.entity.User;
import org.pos.retailpossystem.payload.dto.UserDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public static UserDto mapToDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .role(user.getRole())
                .storeId(
                        user.getStore() == null
                                ? null
                                : user.getStore().getId()
                )
                .branchId(
                        user.getBranch() == null
                                ? null
                                : user.getBranch().getId()
                )
                .branch(
                        user.getBranch() == null
                                ? null
                                : BranchMapper.toDto(user.getBranch())
                )
                .branchName(
                        user.getBranch() == null
                                ? null
                                : user.getBranch().getName()
                )
                .lastLogin(user.getLastLogin())
                .build();
    }

    public static List<UserDto> mapToDtoList(List<User> users) {

        return users.stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public static Set<UserDto> mapToDtoSet(Set<User> users) {

        return users.stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toSet());
    }

    public static User toEntity(UserDto userDto) {

        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phone(userDto.getPhone())
                .fullName(userDto.getFullName())
                .role(userDto.getRole())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
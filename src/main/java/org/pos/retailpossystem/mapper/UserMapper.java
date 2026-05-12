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
    public static UserDto toDTO(User user) {
        UserDto UserDto = new UserDto();
        UserDto.setId(user.getId());
        UserDto.setEmail(user.getEmail());
        UserDto.setFullName(user.getFullName());
        UserDto.setBranchId(user.getBranch()==null?null:user.getBranch().getId());
        UserDto.setBranch(user.getBranch()==null?null: BranchMapper.toDto(user.getBranch()));
        UserDto.setRole(user.getRole());
        UserDto.setStoreId(user.getStore()==null?null:user.getStore().getId());
        UserDto.setPhone(user.getPhone());

        return UserDto;
    }

    public static List<UserDto> toDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static Set<UserDto> toDTOSet(Set<User> users) {
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toSet());
    }

    public static User toEntity(UserDto UserDto) {
        User createdUser = new User();
        createdUser.setEmail(UserDto.getEmail());
        createdUser.setPassword(UserDto.getPassword());
        createdUser.setCreatedAt(LocalDateTime.now());
        createdUser.setPhone(UserDto.getPhone());
        createdUser.setFullName(UserDto.getFullName());
        createdUser.setRole(UserDto.getRole());

        return createdUser;
    }
}

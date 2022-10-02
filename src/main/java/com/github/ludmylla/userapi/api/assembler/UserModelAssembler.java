package com.github.ludmylla.userapi.api.assembler;

import com.github.ludmylla.userapi.domain.dto.UserModel;
import com.github.ludmylla.userapi.domain.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserModelAssembler {

    @Autowired
    private ModelMapper mapper;

    public UserModel toModel(User user){
        return mapper.map(user, UserModel.class);
    }

    public List<UserModel> toCollectionModel(List<User> users) {
        return users.stream()
                .map(user -> toModel(user))
                .collect(Collectors.toList());
    }
}

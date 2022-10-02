package com.github.ludmylla.userapi.api.disassembler;

import com.github.ludmylla.userapi.domain.dto.input.UserInput;
import com.github.ludmylla.userapi.domain.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInputDisassembler {

    @Autowired
    private ModelMapper mapper;

    public User toDomainModel(UserInput userInput){
        return  mapper.map(userInput, User.class);
    }
}

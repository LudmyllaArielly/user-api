package com.github.ludmylla.userapi.domain.service.impl;

import com.github.ludmylla.userapi.domain.dto.UserDTO;
import com.github.ludmylla.userapi.domain.model.User;
import com.github.ludmylla.userapi.domain.repository.UserRepository;
import com.github.ludmylla.userapi.domain.service.UserService;
import com.github.ludmylla.userapi.domain.service.exceptions.DataIntegrityViolationException;
import com.github.ludmylla.userapi.domain.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Object not found."));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(UserDTO dto) {
        findByEmailUsed(dto);
        return userRepository.save(mapper.map(dto, User.class));
    }

    private void findByEmailUsed(UserDTO dto){
      Optional<User> user = userRepository.findByEmail(dto.getEmail());

      if(user.isPresent() && !user.get().getId().equals(dto.getId())){
        throw new DataIntegrityViolationException("Email in use");
      }
    }
}

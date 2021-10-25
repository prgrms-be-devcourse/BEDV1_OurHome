package com.armand.ourhome.community.user;

import com.armand.ourhome.community.dummy.Dummy;
import com.armand.ourhome.community.dummy.DummyDto;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserDto;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findAll(){
        return userRepository.findAll().stream()
                .map(UserDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto save(UserDto userDto){
        User save = userRepository.save(User.of(userDto));
        return UserDto.of(save);
    }

}

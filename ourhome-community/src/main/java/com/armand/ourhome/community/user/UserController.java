package com.armand.ourhome.community.user;

import com.armand.ourhome.community.dummy.DummyDto;
import com.armand.ourhome.domain.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(){
        List<UserDto> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(UserDto userDto){
        UserDto response = userService.save(userDto);
        return ResponseEntity.ok(response);
    }

}

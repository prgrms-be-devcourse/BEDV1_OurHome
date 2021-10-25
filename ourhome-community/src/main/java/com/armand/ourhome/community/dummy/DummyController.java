package com.armand.ourhome.community.dummy;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/dummy")
@RestController
public class DummyController {

    private final DummyService dummyService;

    @GetMapping
    public ResponseEntity<List<DummyDto>> getDummys(){
        List<DummyDto> response = dummyService.findAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<DummyDto> createDummy(DummyDto dummyDto){
        DummyDto response = dummyService.save(dummyDto);
        return ResponseEntity.ok(response);
    }

}

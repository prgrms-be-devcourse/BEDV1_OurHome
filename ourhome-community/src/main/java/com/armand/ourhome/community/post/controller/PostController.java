package com.armand.ourhome.community.post.controller;

import com.armand.ourhome.community.post.dto.request.ReqPostDto;
import com.armand.ourhome.community.post.dto.response.ResPostDto;
import com.armand.ourhome.community.post.dto.response.ResReturnIdDto;
import com.armand.ourhome.community.post.entity.ResidentialType;
import com.armand.ourhome.community.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Created by yunyun on 2021/10/29.
 */

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ResReturnIdDto> save(@RequestBody @Valid final ReqPostDto postDto) {
        return ResponseEntity.ok(ResReturnIdDto.builder()
                .id(postService.save(postDto))
                .build());
    }

    @GetMapping
    public ResponseEntity<Page<ResPostDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(postService.getAll(pageable));
    }

    @GetMapping("/residentialType/{residentialType}")
    public ResponseEntity<Page<ResPostDto>> getAllByResidentialType(Pageable pageable,
                                                                 @Valid @PathVariable final ResidentialType residentialType) {
        return ResponseEntity.ok(postService.getAllByResidentialType(residentialType, pageable));
    }

    @PostMapping("/{postId}")
    public ResponseEntity<ResReturnIdDto> update(@RequestBody @Valid final ReqPostDto postDto,
                                       @Valid @PathVariable final Long postId){
        return ResponseEntity.ok(ResReturnIdDto.builder()
                .id(postService.update(postDto, postId))
                .build());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResPostDto> getOne(@Valid @PathVariable final Long postId){
     return ResponseEntity.ok(postService.getOne(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ResReturnIdDto> delete(@Valid @PathVariable final Long postId){
        postService.delete(postId);
        return ResponseEntity.ok(ResReturnIdDto.builder()
                .id(postId)
                .build());
    }

}

package com.armand.ourhome.community.post.controller;

import com.armand.ourhome.community.post.dto.PostDto;
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
    public ResponseEntity<Long> save(@RequestBody @Valid final PostDto postDto) {
        return ResponseEntity.ok(postService.save(postDto));
    }

    @GetMapping
    public ResponseEntity<Page<PostDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(postService.getAll(pageable));
    }

    @GetMapping("/residentialType/{residentialType}")
    public ResponseEntity<Page<PostDto>> getAllByResidentialType(Pageable pageable,
                                                                 @Valid @PathVariable final ResidentialType residentialType) {
        return ResponseEntity.ok(postService.getAllByResidentialType(residentialType, pageable));
    }

    @PostMapping("/{postId}")
    public ResponseEntity<Long> update(@RequestBody @Valid final PostDto postDto,
                                       @Valid @PathVariable final Long postId){
        return ResponseEntity.ok(postService.update(postDto, postId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getOne(@Valid @PathVariable final Long postId){
     return ResponseEntity.ok(postService.getOne(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> delete(@Valid @PathVariable final Long postId){
        postService.delete(postId);
        return ResponseEntity.ok(postId);
    }

}

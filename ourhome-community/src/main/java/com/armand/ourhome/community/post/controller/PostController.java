package com.armand.ourhome.community.post.controller;

import com.armand.ourhome.community.post.dto.ContentDto;
import com.armand.ourhome.community.post.dto.PostDto;
import com.armand.ourhome.community.post.dto.TagDto;
import com.armand.ourhome.community.post.entity.PlaceType;
import com.armand.ourhome.community.post.entity.ResidentialType;
import com.armand.ourhome.community.post.entity.SquareType;
import com.armand.ourhome.community.post.entity.StyleType;
import com.armand.ourhome.community.post.service.PostService;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.EntityResponse;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by yunyun on 2021/10/29.
 */

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody @Valid final PostDto postDto) throws IOException {
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

package com.armand.ourhome.community.post.controller;

import com.armand.ourhome.community.post.dto.PostDto;
import com.armand.ourhome.community.post.entity.ResidentialType;
import com.armand.ourhome.community.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

//    @PostMapping
//    public ResponseEntity<Long> save(@RequestBody @Valid final PostDto postDto,
//                                     @RequestPart List<MultipartFile> files) throws IOException {
//        return ResponseEntity.ok(postService.save(postDto, postService.uploadToS3(files)));
//    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(postService.getAll(pageable));
    }

    @GetMapping("/residentialType/{residentialType}")
    public ResponseEntity<List<PostDto>> getAllByResidentialType(Pageable pageable,
                                                                 @Valid @PathVariable final ResidentialType residentialType) {
        return ResponseEntity.ok(postService.getAllByResidentialType(residentialType, pageable));
    }

    @PostMapping("/{postId}")
    public ResponseEntity<Long> update(@Valid @PathVariable final Long postId){
        return null;
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

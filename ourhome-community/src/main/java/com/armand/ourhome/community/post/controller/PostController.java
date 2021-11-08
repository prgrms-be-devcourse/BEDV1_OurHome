package com.armand.ourhome.community.post.controller;

import com.armand.ourhome.common.api.PageResponse;
import com.armand.ourhome.community.post.controller.common.CriteriaType;
import com.armand.ourhome.community.post.dto.request.CreatePostRequest;
import com.armand.ourhome.community.post.dto.request.UpdatePostRequest;
import com.armand.ourhome.community.post.dto.response.ResPost;
import com.armand.ourhome.community.post.dto.response.ResReturnId;
import com.armand.ourhome.community.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Created by yunyun on 2021/10/29.
 */

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ResReturnId> save(@Valid @RequestBody final CreatePostRequest postDto) {
        return ResponseEntity.ok(ResReturnId.builder()
                .id(postService.save(postDto))
                .build());
    }

    @GetMapping
    public ResponseEntity<PageResponse<List<ResPost>>> getAll(Pageable pageable,
                                                              @RequestParam(name= "user_id") final Long userId) {

        return ResponseEntity.ok(postService.getAll(pageable, userId));
    }

    @GetMapping("/type")
    public ResponseEntity<PageResponse<List<ResPost>>> getAllByCriteria(Pageable pageable,
                                                            @RequestParam(name = "criteria_type") CriteriaType criteriaType,
                                                            @RequestParam(name = "criteria") String criteria,
                                                            @RequestParam(name= "user_id") final Long userId){
        return ResponseEntity.ok(postService.getAllByCriteria(criteriaType, criteria, pageable, userId));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResReturnId> update(@RequestBody @Valid final UpdatePostRequest postDto,
                                       @PathVariable final Long id){
        return ResponseEntity.ok(ResReturnId.builder()
                .id(postService.update(postDto, id))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResPost> getOne(@PathVariable final Long id){
     return ResponseEntity.ok(postService.getOne(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResReturnId> delete(@PathVariable final Long id){
        postService.delete(id);
        return ResponseEntity.ok(ResReturnId.builder()
                .id(id)
                .build());
    }

}

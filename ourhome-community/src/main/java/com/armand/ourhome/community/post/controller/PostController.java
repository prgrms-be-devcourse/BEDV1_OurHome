package com.armand.ourhome.community.post.controller;

import com.armand.ourhome.community.post.controller.common.CriteriaType;
import com.armand.ourhome.community.post.dto.request.ReqPost;
import com.armand.ourhome.community.post.dto.request.ReqUserId;
import com.armand.ourhome.community.post.dto.response.ResPost;
import com.armand.ourhome.community.post.dto.response.ResReturnId;
import com.armand.ourhome.community.post.entity.Criteria;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ResReturnId> save(@Valid @RequestBody final ReqPost postDto) {
        return ResponseEntity.ok(ResReturnId.builder()
                .id(postService.save(postDto))
                .build());
    }

    @GetMapping
    public ResponseEntity<Page<ResPost>> getAll(Pageable pageable,
                                                @Valid @RequestBody final ReqUserId reqUserId) {
        return ResponseEntity.ok(postService.getAll(pageable, reqUserId.getUserId()));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ResPost>> getAllByCriteria(Pageable pageable,
                                                            @RequestParam(name = "criteria_type") CriteriaType criteriaType,
                                                            @RequestParam(name = "criteria") String criteria,
                                                            @Valid @RequestBody final ReqUserId reqUserId){
        return ResponseEntity.ok(postService.getAllBYCriteria(criteriaType, criteria, pageable, reqUserId.getUserId()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResReturnId> update(@RequestBody @Valid final ReqPost postDto,
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

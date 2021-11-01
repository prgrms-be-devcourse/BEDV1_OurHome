package com.armand.ourhome.community.post.controller;

import com.armand.ourhome.community.post.entity.*;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.community.post.service.PostService;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by yunyun on 2021/10/31.
 */



@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;

    private Post post;

    @BeforeEach
    void setUp(){
        user = userRepository.saveAndFlush(User.builder()
                .email("test@email.com")
                .password("1223")
                .nickname("화이팅!!")
                .description("모두의 집 개발자")
                .profileImageUrl("/user/idslielfjidjf-jielj19.jpg")
                .build());

        var userSaved = userRepository.findById(user.getId()).orElseThrow( () -> new RuntimeException("해당 사용자 정보는 존재하지 않습니다."));

        post = postRepository.saveAndFlush(Post.builder()
                .residentialType(ResidentialType.APARTMENT)
                .squareType(SquareType.SIZE_10_PYEONG)
                .styleType(StyleType.ASIAN_STYPE)
                .title("사는 집 입니다.")
                .user(userSaved)
                .contentList(List.of(Content.builder()
                        .description("집 안 내용입니다.")
                        .placeType(PlaceType.LIVINGROOM)
                        .mediaUrl("/post/postPicture.png")
                        .tags(List.of(Tag.builder()
                                .name("아파트")
                                .build()))

                        .build()))
                .build());

    }
    @AfterEach
    void tearDown(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시된 모든 정보를 추출할 수 있다.")
    void getAll() throws Exception {
        //When, Then
       mockMvc.perform(get("/api/v1/post?size=1&page=0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].squareType").value("SIZE_10_PYEONG"))
                .andDo(print());

    }

    @Test
    @DisplayName("저장되어 있는 모든 게시물 중 특정 거주형태 정보만 추출할 수 있다.")
    void getAllByResidentialType() throws Exception {
        //When, Then
        mockMvc.perform(get("/api/v1/post/residentialType/APARTMENT?size=1&page=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].residentialType").value("APARTMENT"))
                .andDo(print());

    }

    @Test
    @DisplayName("특정 게시물을 추출할 수 있다.")
    void getOne() throws Exception {
        //When, Then
        mockMvc.perform(get("/api/v1/post/"+post.getPostId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("postId").value(post.getPostId()))
                .andDo(print());
    }

    @Test
    @DisplayName("특정 게시물을 삭제 할 수 있다.")
    void deleteTest() throws Exception {
        //When, Then
        mockMvc.perform(delete("/api/v1/post/"+post.getPostId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(post.getPostId()))
                .andDo(print());
        assertThat(postRepository.count(), is(0L));
    }


}
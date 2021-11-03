package com.armand.ourhome.community.user.controller;

import com.armand.ourhome.community.post.entity.*;
import com.armand.ourhome.community.post.entity.Tag;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.community.user.dto.request.LoginRequest;
import com.armand.ourhome.community.user.dto.request.SignUpRequest;
import com.armand.ourhome.community.user.dto.request.UpdateInfoRequest;
import com.armand.ourhome.community.user.dto.request.UpdatePasswordRequest;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    User simpleUser = User.builder()
            .email("test@mail.com")
            .password("12341234")
            .nickname("test")
            .description("한줄설명")
            .profileImageUrl("S3 프로필 사진 URL")
            .build();
    User user;

    @BeforeEach
    void saveUser() {
        user = userRepository.save(simpleUser);
    }

    @AfterEach
    void deleteUser() {
        userRepository.deleteAll();
    }

//// -----------------------------------------------------------------------------------------

    @Test
    void 회원가입() throws Exception {
        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("new@mail.com")
                .password("12341234")
                .nickname("new-user")
                .build();
        String request = objectMapper.writeValueAsString(signUpRequest);

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/sign-up", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("email"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("password"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("nickname")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
                                        fieldWithPath("created_at").type(JsonFieldType.STRING).description("created_at")
                                )
                        )
                );
    }

    @Test
    void 로그인() throws Exception {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@mail.com")
                .password("12341234")
                .build();
        String request = objectMapper.writeValueAsString(loginRequest);

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/login", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("email"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("password")
                                ),
                                responseFields(
                                        fieldWithPath("token").type(JsonFieldType.NUMBER).description("token")
                                )
                        )
                );
    }

    @Test
    void 회원정보_수정() throws Exception {
        // Given
        UpdateInfoRequest updateInfoRequest = UpdateInfoRequest.builder()
                .nickname("변경할 닉네임")
                .description("변경할 한줄설명")
                .profileImageBase64(null)
                .build();
        String request = objectMapper.writeValueAsString(updateInfoRequest);

        // When
        ResultActions resultActions = mockMvc.perform(
                patch("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/update-info", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("user_id")
                                ),
                                requestFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("nickname"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("description"),
                                        fieldWithPath("profile_image_base64").type(JsonFieldType.NULL).description("profile_image_base64")
                                ),
                                responseFields(
                                        fieldWithPath("updated_at").type(JsonFieldType.STRING).description("updated_at")
                                )
                        )
                );

    }

    @Test
    void 비밀번호_변경() throws Exception {
        // Given
        UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
                .password("00000000")
                .build();
        String request = objectMapper.writeValueAsString(updatePasswordRequest);

        // When
        ResultActions resultActions = mockMvc.perform(
                patch("/api/users/{id}/password", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/update-password", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("user_id")
                                ),
                                requestFields(
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("password")
                                ),
                                responseFields(
                                        fieldWithPath("updated_at").type(JsonFieldType.STRING).description("updated_at")
                                )
                        )
                );

    }

    @Transactional
    @Test
    void 유저_페이지() throws Exception {
        // Given
        Post post = Post.builder()
                .title("제목")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.DETACHED_HOUCE)
                .styleType(StyleType.NORDIC_STYPE)
                .user(user)
                .contentList(
                        List.of(
                                Content.builder()
                                        .mediaUrl("S3 Post URL 1")
                                        .description("콘텐츠 설명 1")
                                        .placeType(PlaceType.BATHROOM)
                                        .tags(null)
                                        .build(),
                                Content.builder()
                                        .mediaUrl("S3 Post URL 2")
                                        .description("콘텐츠 설명 2")
                                        .placeType(PlaceType.LIVINGROOM)
                                        .tags(null)
                                        .build()
                        )
                ).build();
        postRepository.save(post);
        postRepository.flush(); // 트랜잭션 걸고 flush까지 해줘야함!

        //  When
        ResultActions resultActions = mockMvc.perform(
                // ~ api/users/1?token=1&page=0&size=8
                get("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", String.valueOf(user.getId()))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(8))
                        .param("sort", "postId,DESC")   // FIXME : id 변수명 규칙정해야함
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/page", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("user_id")
                                ),
                                requestParameters(
                                        parameterWithName("token").description("token"),
                                        parameterWithName("page").description("page"),
                                        parameterWithName("size").description("size"),
                                        parameterWithName("sort").description("sort")
                                ),
                                responseFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("nickname"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("description"),
                                        fieldWithPath("profile_image_url").type(JsonFieldType.STRING).description("profile_image_url"),
                                        fieldWithPath("follower_count").type(JsonFieldType.NUMBER).description("follower_count"),
                                        fieldWithPath("following_count").type(JsonFieldType.NUMBER).description("following_count"),
                                        fieldWithPath("bookmark_count").type(JsonFieldType.NUMBER).description("bookmark_count"),
                                        fieldWithPath("like_count").type(JsonFieldType.NUMBER).description("like_count"),
                                        fieldWithPath("post_count").type(JsonFieldType.NUMBER).description("post_count"),

                                        fieldWithPath("thumbnail_list[]").type(JsonFieldType.ARRAY).description("thumbnail_list"),
                                        fieldWithPath("thumbnail_list[].media_url").type(JsonFieldType.STRING).description("media_url"),
                                        fieldWithPath("thumbnail_list[].is_only").type(JsonFieldType.BOOLEAN).description("is_only")
                                )
                        )
                );
    }

}
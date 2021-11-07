package com.armand.ourhome.community.user.controller;

import com.armand.ourhome.community.TestHelper;
import com.armand.ourhome.community.follow.repository.FollowRepository;
import com.armand.ourhome.community.post.entity.*;
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
    private FollowRepository followRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    User me;

    @BeforeEach
    void saveUser() {
        me = userRepository.save(TestHelper.createUser());
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
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                                        fieldWithPath("created_at").type(JsonFieldType.STRING).description("생성시간")
                                )
                        )
                );
    }

    @Test
    void 로그인() throws Exception {
        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email(me.getEmail())
                .password(me.getPassword())
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
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                ),
                                responseFields(
                                        fieldWithPath("token").type(JsonFieldType.NUMBER).description("토큰(id)")
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
                patch("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", String.valueOf(me.getId()))
                        .content(request)
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/update-info", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("token").description("토큰(id)")
                                ),
                                requestFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("한줄설명"),
                                        fieldWithPath("profile_image_base64").type(JsonFieldType.NULL).description("프로필 사진 base64")
                                ),
                                responseFields(
                                        fieldWithPath("updated_at").type(JsonFieldType.STRING).description("수정시간")
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
                patch("/api/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", String.valueOf(me.getId()))
                        .content(request)
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/update-password", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("token").description("token")
                                ),
                                requestFields(
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                ),
                                responseFields(
                                        fieldWithPath("updated_at").type(JsonFieldType.STRING).description("수정시간")
                                )
                        )
                );

    }

    @Transactional
    @Test
    void 유저_페이지() throws Exception {
        // Given
        Post post = TestHelper.createPost(me);
        postRepository.save(post);
        postRepository.flush(); // 트랜잭션 걸고 flush까지 해줘야함!

        //  When
        ResultActions resultActions = mockMvc.perform(
                // ~ api/users/1?token=1&page=0&size=8
                get("/api/users/{id}", me.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", String.valueOf(me.getId()))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(8))
                        .param("sort", "id,DESC")
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/page", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("사용자 아이디")
                                ),
                                requestParameters(
                                        parameterWithName("token").description("토큰(id)"),
                                        parameterWithName("page").description("현재 페이지 넘버"),
                                        parameterWithName("size").description("페이지 콘텐츠 개수"),
                                        parameterWithName("sort").description("정렬 기준")
                                ),
                                responseFields(
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("한줄설명"),
                                        fieldWithPath("profile_image_url").type(JsonFieldType.STRING).description("프로필 사진 url"),
                                        fieldWithPath("follower_count").type(JsonFieldType.NUMBER).description("팔로워 수"),
                                        fieldWithPath("following_count").type(JsonFieldType.NUMBER).description("팔로잉 수"),
                                        fieldWithPath("is_following").type(JsonFieldType.NULL).description("해당 유저 팔로우 여부"),
                                        fieldWithPath("bookmark_count").type(JsonFieldType.NUMBER).description("북마크 수"),
                                        fieldWithPath("like_count").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                        fieldWithPath("post_count").type(JsonFieldType.NUMBER).description("사진 게시글 수"),

                                        fieldWithPath("thumbnail_list[]").type(JsonFieldType.ARRAY).description("썸네일 리스트"),
                                        fieldWithPath("thumbnail_list[].media_url").type(JsonFieldType.STRING).description("썸네일 이미지 url"),
                                        fieldWithPath("thumbnail_list[].is_only").type(JsonFieldType.BOOLEAN).description("다중 이미지 여부")
                                )
                        )
                );
    }

    @Transactional
    @Test
    void 팔로잉_페이지() throws Exception {
        // Given
        User user2 = TestHelper.createUser();
        // me -> user2 팔로잉
        followRepository.save(TestHelper.createFollow(me, user2));
        followRepository.flush();

        //  When
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/{id}/followings", me.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", String.valueOf(me.getId()))
                        .param("size", String.valueOf(10))
                        .param("is_first", String.valueOf(true))
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/following-page", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("사용자 아이디")
                                ),
                                requestParameters(
                                        parameterWithName("token").description("토큰(id)"),
                                        parameterWithName("size").description("페이지 콘텐츠 개수"),
                                        parameterWithName("is_first").description("최초 페이징 요청 여부")
                                ),
                                responseFields(
                                        fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("페이징 콘텐츠 리스트"),
                                        fieldWithPath("content[].user_id").type(JsonFieldType.NUMBER).description("사용자 id"),
                                        fieldWithPath("content[].profile_image_url").type(JsonFieldType.STRING).description("프로필 사진 url"),
                                        fieldWithPath("content[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("content[].description").type(JsonFieldType.STRING).description("한줄 설명"),
                                        fieldWithPath("content[].is_following").type(JsonFieldType.BOOLEAN).description("팔로우 여부"),

                                        fieldWithPath("last_id").type(JsonFieldType.NUMBER).description("페이징용 마지막 id (Cursor)")
                                )
                        )
                );
    }

    @Transactional
    @Test
    void 팔로워_페이지() throws Exception {
        // Given
        User user2 = TestHelper.createUser();
        // user2 -> me 팔로잉
        followRepository.save(TestHelper.createFollow(user2, me));
        followRepository.flush();

        //  When
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/{id}/followers", me.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", String.valueOf(me.getId()))
                        .param("size", String.valueOf(10))
                        .param("is_first", String.valueOf(true))
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "user/follower-page", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("사용자 아이디")
                                ),
                                requestParameters(
                                        parameterWithName("token").description("토큰(id)"),
                                        parameterWithName("size").description("페이지 콘텐츠 개수"),
                                        parameterWithName("is_first").description("최초 페이징 요청 여부")
                                ),
                                responseFields(
                                        fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("페이징 콘텐츠 리스트"),
                                        fieldWithPath("content[].user_id").type(JsonFieldType.NUMBER).description("사용자 id"),
                                        fieldWithPath("content[].profile_image_url").type(JsonFieldType.STRING).description("프로필 사진 url"),
                                        fieldWithPath("content[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("content[].description").type(JsonFieldType.STRING).description("한줄 설명"),
                                        fieldWithPath("content[].is_following").type(JsonFieldType.BOOLEAN).description("팔로우 여부"),

                                        fieldWithPath("last_id").type(JsonFieldType.NUMBER).description("페이징용 마지막 id (Cursor)")
                                )
                        )
                );
    }


}
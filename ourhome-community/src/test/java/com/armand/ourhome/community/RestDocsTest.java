package com.armand.ourhome.community;

import com.armand.ourhome.community.dummy.DummyDto;
import com.armand.ourhome.community.dummy.DummyService;
import com.armand.ourhome.community.user.UserService;
import com.armand.ourhome.domain.user.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("prod")
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
public class RestDocsTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    DummyService dummyService;
    @Autowired
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;

    @Transactional
    @Test
    void 더미_서비스_테스트() {
        // Given
        DummyDto dummyDto = DummyDto.builder().build();

        // When
        DummyDto save = dummyService.save(dummyDto);
        List<DummyDto> all = dummyService.findAll();

        // Then
        assertThat(save.getDummyId(), is(1L));
        assertThat(all, hasSize(1));
    }

    @Test
    void 더미_컨트롤러_테스트() throws Exception {
        // Given
        DummyDto dummyDto = DummyDto.builder().build();
        String request = objectMapper.writeValueAsString(dummyDto);

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/api/dummy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );

        // Then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document(
                                "dummy/save", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("dummyId").type(JsonFieldType.NULL).description("dummyId")
                                ),
                                responseFields(
                                        fieldWithPath("dummyId").type(JsonFieldType.NUMBER).description("dummyId")
                                )
                        )
                );
    }

    @Transactional
    @Test
    void 유저_서비스_테스트() {
        // Given
        UserDto userDto = UserDto.builder().build();

        // When
        UserDto save = userService.save(userDto);
        List<UserDto> all = userService.findAll();

        // Then
        assertThat(save.getId(), is(1L));
        assertThat(all, hasSize(1));
    }

    @Test
    void 유저_컨트롤러_테스트() throws Exception {
        // Given
        UserDto userDto = UserDto.builder().build();
        String request = objectMapper.writeValueAsString(userDto);

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
                                "user/save", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("id").type(JsonFieldType.NULL).description("id")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("id")
                                )
                        )
                );
    }


}

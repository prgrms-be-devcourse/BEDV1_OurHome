package com.armand.ourhome.market.review.controller;

import com.armand.ourhome.market.review.dto.request.*;
import com.armand.ourhome.market.review.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest
@ContextConfiguration(classes = ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Test
    @DisplayName("리뷰를 생성한다.")
    public void testAddReview() throws Exception {
        //given
        Long userId = 1L;
        Long itemId = 1L;
        String comment = "너무나도 좋은 제품입니다. 다음에 또 구매하겠습니다";
        int rating = 5;

        RequestAddReview request = RequestAddReview.builder()
                .userId(userId)
                .comment(comment)
                .itemId(itemId)
                .rating(rating)
                .build();

        given(reviewService.save(any())).willReturn(1L);

        //when
        ResultActions actions = mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        actions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("review-add",
                            requestFields(
                                    fieldWithPath("user_id").type(NUMBER).description("user_id"),
                                    fieldWithPath("item_id").type(NUMBER).description("item_id"),
                                    fieldWithPath("comment").type(STRING).description("comment"),
                                    fieldWithPath("rating").type(NUMBER).description("rating")
                            )
                        ));
    }

    @Test
    @DisplayName("리뷰를 수정한다")
    public void testUpdateReview() throws Exception {
        //given
        RequestUpdateReview request = RequestUpdateReview.builder()
                .comment("너무나도 좋은 제품입니다. 이 리뷰는 수정되었습니다.")
                .rating(4)
                .userId(1L)
                .build();

        given(reviewService.update(any(), any())).willReturn(1L);
        //when
        ResultActions actions = mockMvc.perform(patch("/api/reviews/{reviewId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("review-update",
                        pathParameters(
                                parameterWithName("reviewId").description("reviewId")
                        ),
                        requestFields(
                                fieldWithPath("user_id").type(NUMBER).description("user_id"),
                                fieldWithPath("rating").type(NUMBER).description("rating"),
                                fieldWithPath("comment").type(STRING).description("comment")
                        )
                    ));
    }


    @Test
    @DisplayName("리뷰를 삭제한다")
    public void testDeleteReview() throws Exception {
        //given
        Long reviewId = 1L;
        RequestDeleteReview request = RequestDeleteReview.builder()
                .userId(1L)
                .build();

        //when
        ResultActions actions = mockMvc.perform(delete("/api/reviews/{reviewId}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("review-delete",
                        pathParameters(
                                parameterWithName("reviewId").description("reviewId")
                        ),
                        requestFields(
                                fieldWithPath("user_id").type(NUMBER).description("user_id")
                        )
                ));
    }

    @Test
    @DisplayName("리뷰에 '도움이 돼요' 를 생성한다")
    public void testPraiseReview() throws Exception {
        //given
        Long reviewId = 1L;
        RequestPraiseReview request = new RequestPraiseReview(1L);

        given(reviewService.praiseReview(1L, request)).willReturn(1L);

        //when
        ResultActions actions = mockMvc.perform(post("/api/reviews/{reviewId}/praise", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("review/praise",
                        pathParameters(
                                parameterWithName("reviewId").description("reviewId")
                        ),
                        requestFields(
                                fieldWithPath("user_id").type(NUMBER).description("user_id")
                        )
                ));
    }

    @Test
    @DisplayName("리뷰에 '도움이 돼요' 를 제거한다")
    public void testRemovePraiseReview() throws Exception {
        //given
        Long reviewId = 1L;
        Long praiseId = 1L;
        RequestRemovePraiseReview request = new RequestRemovePraiseReview(1L);

        //when
        ResultActions actions = mockMvc.perform(delete("/api/reviews/{reviewId}/praise/{praiseId}", reviewId, praiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("review/praise",
                        pathParameters(
                                parameterWithName("reviewId").description("reviewId"),
                                parameterWithName("praiseId").description("praiseId")
                        ),
                        requestFields(
                                fieldWithPath("user_id").type(NUMBER).description("user_id")
                        )
                ));
    }
}
package com.armand.ourhome.market.review.controller;

import com.armand.ourhome.market.review.dto.request.*;
import com.armand.ourhome.market.review.dto.response.ResponseAddReview;
import com.armand.ourhome.market.review.dto.response.ResponseReviewImage;
import com.armand.ourhome.market.review.dto.response.ResponseUpdateReview;
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
import static org.springframework.restdocs.payload.JsonFieldType.*;
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
    @DisplayName("????????? ????????????.")
    public void testAddReview() throws Exception {
        //given
        Long userId = 1L;
        Long itemId = 1L;
        String comment = "???????????? ?????? ???????????????. ????????? ??? ?????????????????????";
        int rating = 5;

        RequestAddReview request = RequestAddReview.builder()
                .userId(userId)
                .comment(comment)
                .itemId(itemId)
                .rating(rating)
                .reviewImageBase64("imageBase64")
                .build();

        ResponseAddReview response = new ResponseAddReview(1L, new ResponseReviewImage(1L, "imageUrl"));

        given(reviewService.save(any())).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        actions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("review/review-add",
                            requestFields(
                                    fieldWithPath("user_id").type(NUMBER).description("????????? ?????????"),
                                    fieldWithPath("item_id").type(NUMBER).description("?????? ?????????"),
                                    fieldWithPath("comment").type(STRING).description("?????? ?????????"),
                                    fieldWithPath("rating").type(NUMBER).description("?????? ??????"),
                                    fieldWithPath("review_image_base64").type(STRING).description("?????? ????????? Base64 ?????????")
                            ),
                            responseFields(
                                    fieldWithPath("review_id").type(NUMBER).description("?????? ?????????"),
                                    fieldWithPath("review_image").type(OBJECT).description("?????? ????????? ??????"),
                                    fieldWithPath("review_image.id").type(NUMBER).description("?????? ????????? ?????????"),
                                    fieldWithPath("review_image.image_url").type(STRING).description("?????? ????????? URL")
                            )
                        ));
    }

    @Test
    @DisplayName("????????? ????????????")
    public void testUpdateReview() throws Exception {
        //given
        RequestUpdateReview request = RequestUpdateReview.builder()
                .comment("???????????? ?????? ???????????????. ??? ????????? ?????????????????????.")
                .rating(4)
                .userId(1L)
                .reviewImageBase64("imageBase64")
                .build();

        ResponseUpdateReview response = new ResponseUpdateReview(1L, new ResponseReviewImage(1L, "imageUrl"));

        given(reviewService.update(any(), any())).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(patch("/api/reviews/{reviewId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("review/review-update",
                        pathParameters(
                                parameterWithName("reviewId").description("reviewId")
                        ),
                        requestFields(
                                fieldWithPath("user_id").type(NUMBER).description("????????? ?????????"),
                                fieldWithPath("rating").type(NUMBER).description("?????? ??????"),
                                fieldWithPath("comment").type(STRING).description("?????? ??????"),
                                fieldWithPath("review_image_base64").type(STRING).description("?????? ????????? Base64 ?????????")
                        ),
                        responseFields(
                                fieldWithPath("review_id").type(NUMBER).description("?????? ?????????"),
                                fieldWithPath("review_image").type(OBJECT).description("?????? ????????? ??????"),
                                fieldWithPath("review_image.id").type(NUMBER).description("?????? ????????? ?????????"),
                                fieldWithPath("review_image.image_url").type(STRING).description("?????? ????????? URL")
                        )
                    ));
    }

    @Test
    @DisplayName("????????? ????????????")
    public void testDeleteReview() throws Exception {
        //given
        Long reviewId = 1L;
        RequestDeleteReview request = RequestDeleteReview.builder()
                .userId(1L)
                .reviewImageId(1L)
                .build();

        //when
        ResultActions actions = mockMvc.perform(delete("/api/reviews/{reviewId}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("review/review-delete",
                        pathParameters(
                                parameterWithName("reviewId").description("?????? ?????????")
                        ),
                        requestFields(
                                fieldWithPath("user_id").type(NUMBER).description("?????? ????????? ?????????"),
                                fieldWithPath("review_image_id").type(NUMBER).description("?????? ????????? ?????????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? '????????? ??????' ??? ????????????")
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
                                parameterWithName("reviewId").description("?????? ?????????")
                        ),
                        requestFields(
                                fieldWithPath("user_id").type(NUMBER).description("????????? ?????????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? '????????? ??????' ??? ????????????")
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
                .andDo(document("review/praise-remove",
                        pathParameters(
                                parameterWithName("reviewId").description("?????? ?????????"),
                                parameterWithName("praiseId").description("'????????? ??????' ?????????")
                        ),
                        requestFields(
                                fieldWithPath("user_id").type(NUMBER).description("????????? ?????????")
                        )
                ));
    }
}
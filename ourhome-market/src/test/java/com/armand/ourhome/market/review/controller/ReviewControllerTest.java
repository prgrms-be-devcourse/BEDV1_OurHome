package com.armand.ourhome.market.review.controller;

import com.armand.ourhome.market.review.service.ReviewService;
import com.armand.ourhome.market.review.dto.request.RequestAddReview;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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
                                    fieldWithPath("userId").type(NUMBER).description("userId"),
                                    fieldWithPath("itemId").type(NUMBER).description("itemId"),
                                    fieldWithPath("comment").type(STRING).description("comment"),
                                    fieldWithPath("rating").type(NUMBER).description("rating")
                            )
                        ));
    }


}
package com.armand.ourhome.market.item.controller;

import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.response.ResponseItem;
import com.armand.ourhome.market.item.dto.response.ResponseItemDetail;
import com.armand.ourhome.market.item.mapper.ItemMapper;
import com.armand.ourhome.market.item.service.ItemService;
import com.armand.ourhome.market.review.service.ReviewService;
import com.armand.ourhome.market.review.service.dto.ReviewDto;
import com.armand.ourhome.market.review.service.dto.response.PageResponse;
import com.armand.ourhome.market.review.service.dto.response.ResponseReview;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(PER_CLASS)
@AutoConfigureRestDocs
@ContextConfiguration(classes = ItemController.class)
@WebMvcTest
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ReviewService reviewService;

    private ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    private ResponseReview request;
    private Item item;

    @BeforeAll
    void setup() {
        request = ResponseReview.builder()
                .reviewId(1L)
                .createdAt(LocalDateTime.now())
                .userId(1L)
                .help(3)
                .comment("너무나도 좋은 제품입니다. 다음에 다시 구매할 것 같아요:)")
                .rating(5)
                .build();

        item = Item.builder()
                .price(1000)
                .name("item")
                .company(new Company("company"))
                .category(Category.DAILY_NECESSITIES)
                .description("이 제품은 그러한 제품입니다.")
                .imageUrl("image url")
                .stockQuantity(100)
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("상품 상세 정보를 조회한다.")
    void fetchItemDetail() throws Exception {
        //given
        List<ResponseReview> dtoList = List.of(this.request);
        PageResponse<List<ResponseReview>> reviews = PageResponse.<List<ResponseReview>>builder()
                .totalElements(1)
                .totalPages(1)
                .content(dtoList)
                .size(5)
                .build();

        ResponseItemDetail itemDto = ResponseItemDetail.builder()
                .companyName(item.getCompanyName())
                .createdAt(LocalDateTime.now())
                .description(item.getDescription())
                .imageUrl(item.getImageUrl())
                .name(item.getName())
                .category(item.getCategory())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .reviews(reviews)
                .build();

        given(itemService.fetchItemDetailWithReview(any(), any())).willReturn(itemDto);

        //when
        ResultActions actions = mockMvc.perform(get("/api/items/{itemId}", 1L)
                .characterEncoding("utf8")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("name").value(item.getName()))
                .andExpect(jsonPath("description").value(item.getDescription()))
                .andDo(print())
                .andDo(document("items-get-one",
                        pathParameters(
                                parameterWithName("itemId").description("itemId")
                        ),
                        responseFields(
                                fieldWithPath("name").type(STRING).description("name"),
                                fieldWithPath("description").type(STRING).description("description"),
                                fieldWithPath("imageUrl").type(STRING).description("imageUrl"),
                                fieldWithPath("price").type(NUMBER).description("price"),
                                fieldWithPath("stockQuantity").type(NUMBER).description("stockQuantity"),
                                fieldWithPath("companyName").type(STRING).description("companyName"),
                                fieldWithPath("category").type(STRING).description("category"),
                                fieldWithPath("createdAt").type(STRING).description("createdAt"),
                                fieldWithPath("reviews").type(OBJECT).description("reviews"),
                                fieldWithPath("reviews.totalElements").type(NUMBER).description("reviews.totalElements"),
                                fieldWithPath("reviews.totalPages").type(NUMBER).description("reviews.totalPages"),
                                fieldWithPath("reviews.size").type(NUMBER).description("reviews.size"),
                                fieldWithPath("reviews.content").type(ARRAY).description("reviews.content"),
                                fieldWithPath("reviews.content[].reviewId").type(NUMBER).description("reviews.content[].reviewId"),
                                fieldWithPath("reviews.content[].userId").type(NUMBER).description("reviews.content[].userId"),
                                fieldWithPath("reviews.content[].rating").type(NUMBER).description("reviews.content[].rating"),
                                fieldWithPath("reviews.content[].comment").type(STRING).description("reviews.content[].comment"),
                                fieldWithPath("reviews.content[].help").type(NUMBER).description("reviews.content[].help"),
                                fieldWithPath("reviews.content[].createdAt").type(STRING).description("reviews.content[].createdAt"),
                                fieldWithPath("serverDateTime").type(STRING).description("serverDateTime")
                        )
                        ));

    }
}
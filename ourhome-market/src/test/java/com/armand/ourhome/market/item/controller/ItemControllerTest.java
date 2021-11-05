package com.armand.ourhome.market.item.controller;

import com.armand.ourhome.common.api.PageResponse;
import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.response.ResponseItem;
import com.armand.ourhome.market.item.dto.response.ResponseItemDetail;
import com.armand.ourhome.market.item.mapper.ItemMapper;
import com.armand.ourhome.market.item.service.ItemService;
import com.armand.ourhome.market.review.service.ReviewService;
import com.armand.ourhome.market.review.dto.response.ResponseReview;
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
import java.util.ArrayList;
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
                .numberOfElements(1)
                .number(1)
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
                .andDo(document("item/item-get-one",
                        pathParameters(
                                parameterWithName("itemId").description("itemId")
                        ),
                        responseFields(
                                fieldWithPath("name").type(STRING).description("상품 이름"),
                                fieldWithPath("description").type(STRING).description("상품 설명"),
                                fieldWithPath("image_url").type(STRING).description("상품 이미지 URL"),
                                fieldWithPath("price").type(NUMBER).description("상품 가격"),
                                fieldWithPath("stock_quantity").type(NUMBER).description("상품 재고 수량"),
                                fieldWithPath("company_name").type(STRING).description("상품 제조사"),
                                fieldWithPath("category").type(STRING).description("상품 카테고리"),
                                fieldWithPath("created_at").type(STRING).description("상품 등록 일자"),
                                fieldWithPath("count").type(NUMBER).description("상품 리뷰 개수"),
                                fieldWithPath("average").type(NUMBER).description("상품 리뷰 평점"),
                                fieldWithPath("reviews").type(OBJECT).description("리뷰 정보"),
                                fieldWithPath("reviews.total_elements").type(NUMBER).description("리뷰 총 수"),
                                fieldWithPath("reviews.total_pages").type(NUMBER).description("리뷰 페이지 총 수"),
                                fieldWithPath("reviews.number").type(NUMBER).description("리뷰 페이지 넘버"),
                                fieldWithPath("reviews.number_of_elements").type(NUMBER).description("반환된 리뷰 개수"),
                                fieldWithPath("reviews.content").type(ARRAY).description("리뷰 내용"),
                                fieldWithPath("reviews.content[].review_id").type(NUMBER).description("리뷰 아이디"),
                                fieldWithPath("reviews.content[].user_id").type(NUMBER).description("리뷰 작성자 아이디"),
                                fieldWithPath("reviews.content[].rating").type(NUMBER).description("리뷰 평점"),
                                fieldWithPath("reviews.content[].comment").type(STRING).description("리뷰 코멘트"),
                                fieldWithPath("reviews.content[].help").type(NUMBER).description("리뷰 '도움이 돼요' 수"),
                                fieldWithPath("reviews.content[].is_praise").type(BOOLEAN).description("현 사용자의 해당 리뷰 '도움이 돼요' 여부"),
                                fieldWithPath("reviews.content[].created_at").type(STRING).description("리뷰 생성 일자"),
                                fieldWithPath("server_date_time").type(STRING).description("서버 시간")
                        )
                        ));

    }

    @Test
    @DisplayName("상품을 다건 조회한다")
    public void testFetchItemPages() throws Exception {
        //given
        Long lastItemId = 5L;
        int size = 5;

        List<ItemDto> items = new ArrayList<>();

        for (int i = 4; i >= 0; i--) {
            ItemDto item = ItemDto.builder()
                    .companyName("company")
                    .count((int) (Math.random() * 100) / 10)
                    .average(((int)(Math.random() * 5 * 100)) / 100.0)
                    .name("item")
                    .itemId(4L)
                    .price(1000)
                    .stockQuantity(100)
                    .category(Category.DAILY_NECESSITIES)
                    .createdAt(LocalDateTime.now())
                    .imageUrl("imgae")
                    .build();
            items.add(item);
        }

        given(itemService.fetchItemPagesBy(lastItemId, size)).willReturn(new ResponseItem(items));

        //when
        ResultActions actions = mockMvc.perform(get("/api/items")
                .param("lastItemId", "5")
                .param("size", "5")
                .characterEncoding("utf8"));

        //then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("item/item-get-all",
                        responseFields(
                                fieldWithPath("items").type(ARRAY).description("상품 정보"),
                                fieldWithPath("items[].item_id").type(NUMBER).description("상품 아이디"),
                                fieldWithPath("items[].name").type(STRING).description("상품 이름"),
                                fieldWithPath("items[].image_url").type(STRING).description("상품 이미지 URL"),
                                fieldWithPath("items[].price").type(NUMBER).description("상품 가격"),
                                fieldWithPath("items[].stock_quantity").type(NUMBER).description("상품 재고 수량"),
                                fieldWithPath("items[].company_name").type(STRING).description("상품 제조사"),
                                fieldWithPath("items[].category").type(STRING).description("상품 카테고리"),
                                fieldWithPath("items[].created_at").type(STRING).description("상품 생성 일자"),
                                fieldWithPath("items[].count").type(NUMBER).description("상품 리뷰 개수"),
                                fieldWithPath("items[].average").type(NUMBER).description("상품 리뷰 평점"),
                                fieldWithPath("server_date_time").type(STRING).description("서버 시간")
                        )));

    }


}
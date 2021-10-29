package com.armand.ourhome.market.order.controller;

import com.armand.ourhome.domain.OurHomeDomainConfig;
import com.armand.ourhome.market.OurHomeMarketApplication;
import com.armand.ourhome.market.order.domain.PaymentType;
import com.armand.ourhome.market.order.dto.OrderRequest;
import com.armand.ourhome.market.order.dto.OrderResponse;
import com.armand.ourhome.market.order.dto.UserRequest;
import com.armand.ourhome.market.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureRestDocs
@WebMvcTest
@ContextConfiguration(classes = OrderController.class)
class OrderControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void createOrder() throws Exception {

        // Given
        OrderResponse orderResponse = OrderResponse.builder()
                .id(1L)
                .paymentType(PaymentType.FUND_TRANSFER)
                .build();

        given(orderService.createOrder(any())).willReturn(orderResponse);

        // When
        final ResultActions resultActions = mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(orderResponse))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(1L));
    }

}
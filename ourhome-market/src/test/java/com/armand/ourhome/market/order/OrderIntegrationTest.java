package com.armand.ourhome.market.order;

import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.order.domain.DeliveryStatus;
import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.domain.OrderStatus;
import com.armand.ourhome.market.order.domain.PaymentType;
import com.armand.ourhome.market.order.dto.DeliveryRequest;
import com.armand.ourhome.market.order.dto.OrderRequest;
import com.armand.ourhome.market.order.dto.UserRequest;
import com.armand.ourhome.market.order.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createOrder() throws Exception {

        // Given
        UserRequest userRequest = UserRequest.builder()
                .id(1L)
                .build();

        OrderRequest orderRequest = OrderRequest.builder()
                .paymentType(PaymentType.FUND_TRANSFER)
                .userId(1L)
                .build();

        // When
        final ResultActions resultActions = mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("status").value("ACCEPTED"));
    }

    @Test
    public void lookUpOrder() throws Exception {

        // Given
        UserRequest userRequest = UserRequest.builder()
                .id(1L)
                .build();

        OrderRequest orderRequest = OrderRequest.builder()
                .paymentType(PaymentType.FUND_TRANSFER)
                .userId(1L)
                .build();

        // When
        final ResultActions resultActions = mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("status").value("ACCEPTED"));
    }
}

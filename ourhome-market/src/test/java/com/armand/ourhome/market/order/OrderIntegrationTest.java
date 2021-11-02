package com.armand.ourhome.market.order;

import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.TestHelper;
import com.armand.ourhome.market.order.domain.PaymentType;
import com.armand.ourhome.market.order.dto.OrderItemRequest;
import com.armand.ourhome.market.order.dto.OrderRequest;
import com.armand.ourhome.market.order.dto.OrderResponse;
import com.armand.ourhome.market.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderService orderService;

    private User userWithAddress;
    private User userWithOutAddress;

    private Item soapItem;
    private Item ramenItem;

    @BeforeAll
    void setUp() {
        userWithAddress = userRepository.save(TestHelper.createUser());
        userWithOutAddress = userRepository.save(TestHelper.createUserWithoutAddress());
        soapItem = itemRepository.save(TestHelper.createSoapItem());
        ramenItem = itemRepository.save(TestHelper.createRamenitem());
    }

    @Test
    @DisplayName("사용자는 주문을 할 수 있다.")
    public void createOrder() throws Exception {
        // Given
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        orderItemRequests.add(OrderItemRequest.builder()
                .orderCount(2)
                .itemId(ramenItem.getId())
                .build());
        orderItemRequests.add(OrderItemRequest.builder()
                .orderCount(1)
                .itemId(soapItem.getId())
                .build());

        OrderRequest orderRequest = OrderRequest.builder()
                .paymentType(PaymentType.FUND_TRANSFER)
                .userId(userWithOutAddress.getId())
                .address("Seoul City")
                .orderItemRequests(orderItemRequests)
                .build();

        // When
        final ResultActions resultActions = mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value("ACCEPTED"));
    }

    @Test
    @DisplayName("사용자가 주소를 적지 않으면 사용자의 주소로 주문된다.")
    public void createOrderWithoutAddress() throws Exception {

        // Given
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        orderItemRequests.add(OrderItemRequest.builder()
                .orderCount(2)
                .itemId(ramenItem.getId())
                .build());
        orderItemRequests.add(OrderItemRequest.builder()
                .orderCount(1)
                .itemId(soapItem.getId())
                .build());

        OrderRequest orderRequest = OrderRequest.builder()
                .paymentType(PaymentType.FUND_TRANSFER)
                .userId(userWithAddress.getId())
                .orderItemRequests(orderItemRequests)
                .build();;

        // When
        final ResultActions resultActions = mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // Then
        assertThat(orderRequest.getAddress()).isNull();

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status").value("ACCEPTED"))
                .andExpect(jsonPath("address").exists());
    }

    @Test
    @DisplayName("사용자는 주문을 조회할 수 있다.")
    public void lookUpOrder() throws Exception {

        // Given
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        orderItemRequests.add(OrderItemRequest.builder()
                .orderCount(2)
                .itemId(ramenItem.getId())
                .build());
        orderItemRequests.add(OrderItemRequest.builder()
                .orderCount(1)
                .itemId(soapItem.getId())
                .build());

        int total = ramenItem.getPrice() * 2 + soapItem.getPrice();

        OrderRequest orderRequest = OrderRequest.builder()
                .paymentType(PaymentType.FUND_TRANSFER)
                .userId(userWithAddress.getId())
                .address("Seoul City")
                .orderItemRequests(orderItemRequests)
                .build();

        OrderResponse order = orderService.createOrder(orderRequest);

        // When
        final ResultActions resultActions = mockMvc.perform(get("/orders/{order_id}", order.getId())
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(order.getId()))
                .andExpect(jsonPath("status").value("ACCEPTED"))
                .andExpect(jsonPath("paymentType").value("FUND_TRANSFER"))
                .andExpect(jsonPath("totalPrice").value(total));
    }

    @Test
    @DisplayName("사용자는 주문을 취소할 수 있다.")
    public void cancelOrder() throws Exception {

        // Given
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        orderItemRequests.add(OrderItemRequest.builder()
                .orderCount(2)
                .itemId(ramenItem.getId())
                .build());
        orderItemRequests.add(OrderItemRequest.builder()
                .orderCount(1)
                .itemId(soapItem.getId())
                .build());

        OrderRequest orderRequest = OrderRequest.builder()
                .paymentType(PaymentType.FUND_TRANSFER)
                .userId(userWithOutAddress.getId())
                .address("Seoul City")
                .orderItemRequests(orderItemRequests)
                .build();

        OrderResponse order = orderService.createOrder(orderRequest);

        // When
        final ResultActions resultActions = mockMvc.perform(post("/orders/cancel/{order_id}", order.getId())
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(order.getId()))
                .andExpect(jsonPath("status").value("CANCELLED"))
                .andExpect(jsonPath("deliveryResponse.status").value("CANCELLED"));

    }
}

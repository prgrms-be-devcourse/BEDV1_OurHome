package com.armand.ourhome.market.order;

import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.TestHelper;
import com.armand.ourhome.market.item.service.ItemService;
import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.domain.PaymentType;
import com.armand.ourhome.market.order.dto.OrderRequest;
import com.armand.ourhome.market.order.dto.UserRequest;
import com.armand.ourhome.market.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @BeforeEach
    void setUp() {
        Item soapItem = TestHelper.createSoapItem();
        Item ramenItem = TestHelper.createRamenitem();

        itemRepository.save(soapItem);
        itemRepository.save(ramenItem);
    }

    @Test
    @DisplayName("사용자는 주문을 할 수 있다.")
    public void createOrder() throws Exception {

        // Given
        User user = TestHelper.createUserWithoutAddress();
        userRepository.save(user);
        OrderRequest orderRequest = TestHelper.createOrderRequest();

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
    @DisplayName("사용자가 주소를 적지 않으면 사용자의 주소로 주문된다.")
    public void createOrderWithoutAddress() throws Exception {

        // Given
        User user = TestHelper.createUser();
        userRepository.save(user);
        OrderRequest orderRequest = TestHelper.createOrderRequestWithoutAddress();

        // When
        final ResultActions resultActions = mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // Then
        assertThat(orderRequest.getAddress()).isNull();

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("status").value("ACCEPTED"))
                .andExpect(jsonPath("address").exists());
    }

    @Test
    @DisplayName("사용자는 주문을 조회할 수 있다.")
    public void lookUpOrder() throws Exception {

        // Given
        User user = TestHelper.createUser();
        userRepository.save(user);
        OrderRequest orderRequest = TestHelper.createOrderRequest();
        orderService.createOrder(orderRequest);

        // When
        final ResultActions resultActions = mockMvc.perform(get("/orders/{order_id}", 1L)
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("status").value("ACCEPTED"))
                .andExpect(jsonPath("paymentType").value("FUND_TRANSFER"))
                .andExpect(jsonPath("totalPrice").value(3500));
    }

    @Test
    @DisplayName("사용자는 주문을 취소할 수 있다.")
    public void cancelOrder() throws Exception {

        // Given
        User user = TestHelper.createUser();
        userRepository.save(user);
        OrderRequest orderRequest = TestHelper.createOrderRequest();
        orderService.createOrder(orderRequest);

        // When
        final ResultActions resultActions = mockMvc.perform(post("/orders/cancel/{order_id}", 1L)
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("status").value("CANCELLED"))
                .andExpect(jsonPath("deliveryResponse.status").value("CANCELLED"));

    }
}

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
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.exception.VoucherCannotUseException;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import com.armand.ourhome.market.voucher.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@AutoConfigureRestDocs
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
	private VoucherRepository<Voucher> voucherRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private OrderService orderService;

	private User userWithAddress;
	private User userWithOutAddress;

	private Item soapItem;
	private Item ramenItem;

	private Voucher voucher;

	@BeforeAll
	void setUp() {
		userWithAddress = userRepository.save(TestHelper.createUser());
		userWithOutAddress = userRepository.save(TestHelper.createUserWithoutAddress());
		soapItem = itemRepository.save(TestHelper.createSoapItem());
		ramenItem = itemRepository.save(TestHelper.createRamenitem());
		voucher = voucherRepository.save(TestHelper.createPercentVoucher(10, 1000));
		walletRepository.save(TestHelper.createWallet(userWithOutAddress, voucher));
	}

	@Test
	@DisplayName("???????????? ????????? ??? ??? ??????.")
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
			.voucherId(voucher.getId())
			.build();

		// When
		final ResultActions resultActions = mockMvc.perform(post("/orders")
				.content(objectMapper.writeValueAsString(orderRequest))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// Then
		resultActions.andExpect(status().isOk())
			.andDo(
				document(
					"orders/create", preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					requestFields(
						fieldWithPath("payment_type").type(JsonFieldType.STRING)
							.description("?????? ??????"),
						fieldWithPath("user_id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
						fieldWithPath("address").type(JsonFieldType.STRING).description("??????"),
						fieldWithPath("voucher_id").type(JsonFieldType.NUMBER)
							.description("????????? ?????????"),
						fieldWithPath("order_item_requests").type(JsonFieldType.ARRAY)
							.description("?????? ?????????"),
						fieldWithPath("order_item_requests[].order_count").type(
								JsonFieldType.NUMBER)
							.description("?????? ??????"),
						fieldWithPath("order_item_requests[].item_id").type(JsonFieldType.NUMBER)
							.description("????????? ?????????")
					),
					responseFields(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
						fieldWithPath("status").type(JsonFieldType.STRING).description("?????? ??????"),
						fieldWithPath("payment_type").type(JsonFieldType.STRING)
							.description("?????? ??????"),
						fieldWithPath("address").type(JsonFieldType.STRING).description("??????"),
						fieldWithPath("total_price").type(JsonFieldType.NUMBER)
							.description("?????? ??????"),
						fieldWithPath("delivery_response").type(JsonFieldType.OBJECT)
							.description("?????? ??????"),
						fieldWithPath("delivery_response.status").type(JsonFieldType.STRING)
							.description("?????? ??????"),
						fieldWithPath("delivery_response.code").type(JsonFieldType.STRING)
							.description("?????? ??????")
					)
				))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));


	}

	@Test
	@DisplayName("???????????? ????????? ?????? ????????? ???????????? ????????? ????????????.")
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
			.build();

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
	@DisplayName("???????????? ????????? ????????? ??? ??????.")
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
			.andDo(
				document(
					"orders/get", preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					pathParameters(
						parameterWithName("order_id").description("?????? ?????????")
					),
					responseFields(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
						fieldWithPath("status").type(JsonFieldType.STRING).description("?????? ??????"),
						fieldWithPath("payment_type").type(JsonFieldType.STRING)
							.description("?????? ??????"),
						fieldWithPath("address").type(JsonFieldType.STRING).description("??????"),
						fieldWithPath("total_price").type(JsonFieldType.NUMBER)
							.description("?????? ??????"),
						fieldWithPath("delivery_response").type(JsonFieldType.OBJECT)
							.description("?????? ??????"),
						fieldWithPath("delivery_response.status").type(JsonFieldType.STRING)
							.description("?????? ??????"),
						fieldWithPath("delivery_response.code").type(JsonFieldType.STRING)
							.description("?????? ??????")
					)
				))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	@DisplayName("???????????? ????????? ????????? ??? ??????.")
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
		final ResultActions resultActions = mockMvc.perform(
				post("/orders/cancel/{order_id}", order.getId())
					.content(objectMapper.writeValueAsString(orderRequest))
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// Then
		resultActions.andExpect(status().isOk())
			.andDo(
				document(
					"orders/delete", preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					pathParameters(
						parameterWithName("order_id").description("?????? ?????????")
					),
					responseFields(
						fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
						fieldWithPath("status").type(JsonFieldType.STRING).description("?????? ??????"),
						fieldWithPath("payment_type").type(JsonFieldType.STRING)
							.description("?????? ??????"),
						fieldWithPath("address").type(JsonFieldType.STRING).description("??????"),
						fieldWithPath("total_price").type(JsonFieldType.NUMBER)
							.description("?????? ??????"),
						fieldWithPath("delivery_response").type(JsonFieldType.OBJECT)
							.description("?????? ??????"),
						fieldWithPath("delivery_response.status").type(JsonFieldType.STRING)
							.description("?????? ??????"),
						fieldWithPath("delivery_response.code").type(JsonFieldType.STRING)
							.description("?????? ??????")
					)
				))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("id").value(order.getId()))
			.andExpect(jsonPath("status").value("CANCELLED"))
			.andExpect(jsonPath("delivery_response.status").value("CANCELLED"));

	}
}

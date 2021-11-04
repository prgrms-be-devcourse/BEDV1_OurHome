package com.armand.ourhome.market.voucher;

import static com.armand.ourhome.market.voucher.dto.VoucherType.FIXED;
import static com.armand.ourhome.market.voucher.dto.VoucherType.PERCENT;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import com.armand.ourhome.market.voucher.service.VoucherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
public class VoucherIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private VoucherService voucherService;

  @Autowired
  private VoucherRepository<Voucher> voucherRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private User user;

  @BeforeAll
  @Test
  void setUp() throws Exception {
    // 유저 등록
    user = userRepository.save(User.builder()
        .address("경기도 성남시")
        .description("useruser")
        .email("user@gmail.com")
        .nickname("nickname")
        .password("pw123!")
        .profileImageUrl("imageUrl")
        .createdAt(LocalDateTime.now())
        .build());

    // 저장 테스트
    // given
    RequestVoucher requestVoucher = RequestVoucher.builder()
        .value(4000)
        .minLimit(20000)
        .voucherType(FIXED)
        .build();

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/vouchers")
            .content(objectMapper.writeValueAsString(requestVoucher))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("value").value(4000))
        .andExpect(jsonPath("min_limit").value(20000))
        .andExpect(jsonPath("voucher_type").value("FIXED"))
        .andDo(document("voucher/voucher-save", preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("value").type(JsonFieldType.NUMBER).description("할인 비율 또는 할인 금액"),
                fieldWithPath("minLimit").type(JsonFieldType.NUMBER).description("바우처 사용 최소금액"),
                fieldWithPath("voucherType").type(JsonFieldType.STRING).description("바우처 타입")
            ),
            responseFields(
                fieldWithPath("voucher_id").type(JsonFieldType.NUMBER).description("바우처 아이디"),
                fieldWithPath("value").type(JsonFieldType.NUMBER).description("할인 비율 또는 할인 금액"),
                fieldWithPath("minLimit").type(JsonFieldType.NUMBER).description("바우처 사용 최소금액"),
                fieldWithPath("voucherType").type(JsonFieldType.STRING).description("바우처 타입"),
                fieldWithPath("created_at").type(JsonFieldType.STRING).description("바우처 최초 생성시간"),
                fieldWithPath("updated_at").type(JsonFieldType.STRING).description("바우처 최신 수정시간")
            )
        ));
  }

  @Test
  @DisplayName("저장된 바우처를 모두 불러올 수 있다")
  void testLookUpVouchers() throws Exception {
    // given

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/vouchers")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("totalElements").value(voucherRepository.count()));
  }

  @Test
  @DisplayName("바우처를 수정할 수 있다")
  void testUpdateVoucher() throws Exception {
    // given
    RequestVoucher reqeustVoucher = RequestVoucher.builder()
        .value(1000)
        .minLimit(20000)
        .voucherType(FIXED)
        .build();

    VoucherDto save = voucherService.save(reqeustVoucher);

    RequestVoucher updatedVoucher = RequestVoucher.builder()
        .value(3000)
        .minLimit(10000)
        .voucherType(FIXED)
        .build();

    // when
    ResultActions resultActions = mockMvc.perform(patch("/api/vouchers/{id}", save.getId())
            .content(objectMapper.writeValueAsString(updatedVoucher))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("voucher_id").value(save.getId()))
        .andExpect(jsonPath("value").value(3000))
        .andExpect(jsonPath("min_limit").value(10000))
        .andExpect(jsonPath("voucher_type").value("FIXED"));
  }

  @Test
  @DisplayName("바우처를 제거할 수 있다")
  void testDeleteVoucher() throws Exception {
    // given

    // when
    ResultActions resultActions = mockMvc.perform(delete("/api/vouchers/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk());
  }

  @Test
  @DisplayName("바우처를 유저에게 할당할 수 있다")
  void testAssignToUser() throws Exception {
    // given
    RequestVoucher reqeustVoucher = RequestVoucher.builder()
        .value(10)
        .minLimit(20000)
        .voucherType(PERCENT)
        .build();

    VoucherDto voucherDto = voucherService.save(reqeustVoucher);

    // when
    ResultActions resultActions = mockMvc.perform(
            post("/api/vouchers/{id}/assign-to-user", voucherDto.getId())
                .param("userId", String.valueOf(user.getId())))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("voucher_dto.voucher_type").value(voucherDto.getVoucherType().toString()))
        .andExpect(jsonPath("voucher_dto.min_limit").value(voucherDto.getMinLimit()))
        .andExpect(jsonPath("voucher_dto.value").value(voucherDto.getValue()));
  }

}


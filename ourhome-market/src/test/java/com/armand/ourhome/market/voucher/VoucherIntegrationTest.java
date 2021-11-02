package com.armand.ourhome.market.voucher;

import static com.armand.ourhome.market.voucher.dto.VoucherType.FIXED;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import com.armand.ourhome.market.voucher.service.VoucherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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

@ActiveProfiles("test")
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
  private ObjectMapper objectMapper;

  private RequestVoucher reqeustVoucher;

  @BeforeEach
  void setUp() {
    reqeustVoucher = RequestVoucher.builder()
        .value(5000)
        .minLimit(20000)
        .voucherType(FIXED)
        .build();
  }

  @AfterEach
  void tearDown() {
    voucherRepository.deleteAll();
  }

  @Test
  @DisplayName("저장된 바우처를 모두 불러올 수 있다")
  void testLookUpVouchers() throws Exception {
    // given
    VoucherDto save = voucherService.save(reqeustVoucher);

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/vouchers")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("totalElements").value(1));
  }

  @Test
  @DisplayName("바우처를 저장할 수 있다")
  void testSaveVoucher() throws Exception {
    // given

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/vouchers")
            .content(objectMapper.writeValueAsString(reqeustVoucher))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("value").value(5000))
        .andExpect(jsonPath("min_limit").value(20000))
        .andExpect(jsonPath("voucher_type").value("FIXED"));
  }

  @Test
  @DisplayName("바우처를 수정할 수 있다")
  void testUpdateVoucher() throws Exception {
    // given
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
    VoucherDto save = voucherService.save(reqeustVoucher);

    // when
    ResultActions resultActions = mockMvc.perform(delete("/api/vouchers/{id}", save.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk());
  }

}

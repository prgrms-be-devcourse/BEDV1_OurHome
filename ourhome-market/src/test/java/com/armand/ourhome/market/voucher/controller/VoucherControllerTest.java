package com.armand.ourhome.market.voucher.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.VoucherType;
import com.armand.ourhome.market.voucher.service.VoucherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@WebMvcTest
@ContextConfiguration(classes = VoucherController.class)
class VoucherControllerTest {

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private VoucherService voucherService;

  @Test
  void testSaveVoucher() throws Exception {
    // given
    VoucherDto voucherResponse = VoucherDto.builder()
        .id(1L)
        .value(20)
        .minLimit(30000)
        .voucherType(VoucherType.PERCENT)
        .build();

    given(voucherService.save(any())).willReturn(voucherResponse);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/vouchers")
            .content(objectMapper.writeValueAsString(voucherResponse))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("id").value(1L));
  }

}
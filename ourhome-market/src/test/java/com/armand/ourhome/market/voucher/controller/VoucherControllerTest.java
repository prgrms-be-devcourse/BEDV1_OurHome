package com.armand.ourhome.market.voucher.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.armand.ourhome.market.voucher.dto.VoucherDto;
import com.armand.ourhome.market.voucher.dto.VoucherType;
import com.armand.ourhome.market.voucher.dto.request.RequestVoucher;
import com.armand.ourhome.market.voucher.service.VoucherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@TestInstance(Lifecycle.PER_CLASS)
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
    RequestVoucher requestVoucher = RequestVoucher.builder()
        .value(20)
        .minLimit(30000)
        .voucherType(VoucherType.PERCENT)
        .build();
    VoucherDto voucherResponse = VoucherDto.builder()
        .id(1L)
        .value(20)
        .minLimit(30000)
        .voucherType(VoucherType.PERCENT)
        .build();

    given(voucherService.save(any())).willReturn(voucherResponse);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/vouchers")
            .content(objectMapper.writeValueAsString(requestVoucher))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("voucher_id").value(1L));
  }

  @Test
  void testUpdateVoucher() throws Exception {
    // given
    RequestVoucher updatedVoucher = RequestVoucher.builder()
        .value(10)
        .minLimit(30000)
        .voucherType(VoucherType.PERCENT)
        .build();

    VoucherDto updatedResponse = VoucherDto.builder()
        .id(1L)
        .value(10)
        .minLimit(30000)
        .voucherType(VoucherType.PERCENT)
        .build();

    given(voucherService.update(anyLong(), any())).willReturn(updatedResponse);

    // when
    ResultActions resultActions = mockMvc.perform(patch("/api/vouchers/{id}", 1L)
            .content(objectMapper.writeValueAsString(updatedVoucher))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("voucher_id").value(1L))
        .andExpect(jsonPath("value").value(10));
  }

}
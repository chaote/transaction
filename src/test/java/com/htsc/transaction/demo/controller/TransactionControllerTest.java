package com.htsc.transaction.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htsc.transaction.demo.common.ErrorCodeEnums;
import com.htsc.transaction.demo.exception.DuplicateTransactionException;
import com.htsc.transaction.demo.exception.TransactionNotFoundException;
import com.htsc.transaction.demo.model.PageResponse;
import com.htsc.transaction.demo.model.dos.TransactionDO;
import com.htsc.transaction.demo.model.request.TransactionCreateReq;
import com.htsc.transaction.demo.model.request.TransactionUpdateReq;
import com.htsc.transaction.demo.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    private final String trxId = "TRX123456789";

    @Test
    void create_ReturnSuccess() throws Exception {
        // Arrange
        TransactionCreateReq request = new TransactionCreateReq();
        request.setTrxId(trxId);
        request.setAmount(new BigDecimal("100.50"));
        request.setStatus("PENDING");
        request.setType("CASH");
        request.setUserId("user001");

        when(transactionService.createTransaction(any(TransactionCreateReq.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("1000")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data", is(true)));
    }

    @Test
    void create_ReturnError_DuplicateException() throws Exception {
        // Arrange
        TransactionCreateReq request = new TransactionCreateReq();
        request.setTrxId(trxId);
        request.setAmount(new BigDecimal("100.50"));
        request.setStatus("PENDING");
        request.setType("CASH");
        request.setUserId("user001");

        when(transactionService.createTransaction(any(TransactionCreateReq.class)))
                .thenThrow(new DuplicateTransactionException(trxId));

        // Act & Assert
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnums.TRANS_DUPLICATE.getErrorCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnums.TRANS_DUPLICATE.getErrorName())))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void selectPage_Return_WithDefaultParameters() throws Exception {
        // Arrange
        List<TransactionDO> transactionList = Arrays.asList(
                createTransactionDO(trxId, new BigDecimal("100.50"), "PENDING"),
                createTransactionDO("TRX987654321", new BigDecimal("200.75"), "COMPLETED")
        );

        PageResponse<TransactionDO> pageResponse = PageResponse.convertPage(
                transactionList, 1, 20, transactionList.size());

        when(transactionService.selectPage(eq(1), eq(20))).thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/transaction/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("1000")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.currentPage", is(1)))
                .andExpect(jsonPath("$.data.pageSize", is(20)))
                .andExpect(jsonPath("$.data.totalElements", is(2)));
    }

    @Test
    void selectPage_Return_WithCustomParameters() throws Exception {
        // Arrange
        List<TransactionDO> transactionList = Arrays.asList(
                createTransactionDO(trxId, new BigDecimal("100.50"), "PENDING"),
                createTransactionDO("TRX987654321", new BigDecimal("200.75"), "COMPLETED")
        );

        PageResponse<TransactionDO> pageResponse = PageResponse.convertPage(
                transactionList, 2, 10, 25);

        when(transactionService.selectPage(eq(2), eq(10))).thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/transaction/page")
                        .param("currentPage", "2")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("1000")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.currentPage", is(2)))
                .andExpect(jsonPath("$.data.pageSize", is(10)))
                .andExpect(jsonPath("$.data.totalElements", is(25)));
    }

    @Test
    void selectOne_ReturnTransaction() throws Exception {
        // Arrange
        TransactionDO transactionDO = createTransactionDO(trxId, new BigDecimal("100.50"), "PENDING");

        when(transactionService.selectOne(trxId)).thenReturn(transactionDO);

        // Act & Assert
        mockMvc.perform(get("/api/transaction/{trxId}", trxId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("1000")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data.trxId", is(trxId)))
                .andExpect(jsonPath("$.data.amount", is(100.50)))
                .andExpect(jsonPath("$.data.status", is("PENDING")));
    }

    @Test
    void selectOne_ReturnNull() throws Exception {
        // Arrange
        when(transactionService.selectOne(trxId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/transaction/{trxId}", trxId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("1000")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void delete_ReturnSuccess() throws Exception {
        // Arrange
        when(transactionService.deleteTransaction(trxId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/transaction/{trxId}", trxId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("1000")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data", is(true)));
    }

    @Test
    void delete_ReturnError_NotFoundException() throws Exception {
        // Arrange
        when(transactionService.deleteTransaction(trxId))
                .thenThrow(new TransactionNotFoundException(trxId));

        // Act & Assert
        mockMvc.perform(delete("/api/transaction/{trxId}", trxId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnums.TRANS_NOT_FOUND.getErrorCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnums.TRANS_NOT_FOUND.getErrorName())))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void update_ReturnSuccess() throws Exception {
        // Arrange
        TransactionUpdateReq request = new TransactionUpdateReq();
        request.setAmount(new BigDecimal("150.25"));
        request.setStatus("COMPLETED");

        when(transactionService.updateTransaction(eq(trxId), any(TransactionUpdateReq.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(patch("/api/transaction/{trxId}", trxId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("1000")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.data", is(true)));
    }

    @Test
    void update_ReturnError_NotFoundException() throws Exception {
        // Arrange
        TransactionUpdateReq request = new TransactionUpdateReq();
        request.setAmount(new BigDecimal("150.25"));
        request.setStatus("COMPLETED");

        when(transactionService.updateTransaction(eq(trxId), any(TransactionUpdateReq.class)))
                .thenThrow(new TransactionNotFoundException(trxId));

        // Act & Assert
        mockMvc.perform(patch("/api/transaction/{trxId}", trxId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnums.TRANS_NOT_FOUND.getErrorCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnums.TRANS_NOT_FOUND.getErrorName())))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    private TransactionDO createTransactionDO(String trxId, BigDecimal amount, String status) {
        TransactionDO transactionDO = new TransactionDO();
        transactionDO.setTrxId(trxId);
        transactionDO.setAmount(amount);
        transactionDO.setStatus(status);
        transactionDO.setTrxTime(new Date());
        return transactionDO;
    }
}
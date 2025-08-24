package com.htsc.transaction.demo.service;

import com.htsc.transaction.demo.exception.DuplicateTransactionException;
import com.htsc.transaction.demo.exception.TransactionNotFoundException;
import com.htsc.transaction.demo.model.PageResponse;
import com.htsc.transaction.demo.model.dos.TransactionDO;
import com.htsc.transaction.demo.model.request.TransactionCreateReq;
import com.htsc.transaction.demo.model.request.TransactionUpdateReq;
import com.htsc.transaction.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionCreateReq transactionCreateReq;
    private TransactionUpdateReq transactionUpdateReq;
    private TransactionDO transactionDO;
    private String trxId;

    @BeforeEach
    void setUp() {
        trxId = "TRX123456789";

        transactionCreateReq = new TransactionCreateReq(
                trxId,
                new BigDecimal("100.50"),
                "PENDING",
                null,
                null
        );

        transactionUpdateReq = new TransactionUpdateReq(
                new BigDecimal("150.25"),
                "COMPLETED"
        );

        transactionDO = new TransactionDO();
        transactionDO.setTrxId(trxId);
        transactionDO.setAmount(new BigDecimal("100.50"));
        transactionDO.setStatus("PENDING");
        transactionDO.setTrxTime(new Date());
    }

    @Test
    void createTransaction_ReturnTrue() {

        when(transactionRepository.exist(trxId)).thenReturn(false);
        when(transactionRepository.save(any(TransactionDO.class))).thenReturn(1);

        boolean result = transactionService.createTransaction(transactionCreateReq);

        assertTrue(result);
        verify(transactionRepository).exist(trxId);
        verify(transactionRepository).save(any(TransactionDO.class));
    }

    @Test
    void createTransaction_ThrowDuplicate() {
        // Arrange
        when(transactionRepository.exist(trxId)).thenReturn(true);

        assertThrows(DuplicateTransactionException.class, () -> {
            transactionService.createTransaction(transactionCreateReq);
        });

        verify(transactionRepository).exist(trxId);
        verify(transactionRepository, never()).save(any(TransactionDO.class));
    }

    @Test
    void createTransaction_ReturnFalse() {
        // Arrange
        when(transactionRepository.exist(trxId)).thenReturn(false);
        when(transactionRepository.save(any(TransactionDO.class))).thenReturn(0);

        boolean result = transactionService.createTransaction(transactionCreateReq);

        // Assert
        assertFalse(result);
        verify(transactionRepository).exist(trxId);
        verify(transactionRepository).save(any(TransactionDO.class));
    }

    @Test
    void selectPage_EmptyPage_WhenCurrentPageLessThanOne() {
        // Act
        PageResponse<TransactionDO> result = transactionService.selectPage(0, 10);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        verify(transactionRepository, never()).size();
        verify(transactionRepository, never()).selectPage(anyInt(), anyInt());
    }

    @Test
    void selectPage_EmptyPage_WhenPageSizeLessThanOne() {
        // Act
        PageResponse<TransactionDO> result = transactionService.selectPage(1, 0);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        verify(transactionRepository, never()).size();
        verify(transactionRepository, never()).selectPage(anyInt(), anyInt());
    }

    @Test
    void selectPage_ShouldReturn() {
        // Arrange
        int currentPage = 1;
        int pageSize = 10;
        int totalElements = 25;
        List<TransactionDO> transactionList = Arrays.asList(transactionDO, new TransactionDO());

        when(transactionRepository.size()).thenReturn(totalElements);
        when(transactionRepository.selectPage(0, pageSize)).thenReturn(transactionList);

        PageResponse<TransactionDO> result = transactionService.selectPage(currentPage, pageSize);

        // Assert
        assertNotNull(result);
        assertEquals(transactionList.size(), result.getContent().size());
        assertEquals(totalElements, result.getTotalElements());
        assertEquals(currentPage, result.getCurrentPage());
        assertEquals(pageSize, result.getPageSize());
        verify(transactionRepository).size();
        verify(transactionRepository).selectPage(0, pageSize);
    }

    @Test
    void selectPage_ShouldReturnEmptyPage_WhenOffsetExceedsTotalElements() {
        // Arrange
        int currentPage = 3;
        int pageSize = 10;
        int totalElements = 15;

        when(transactionRepository.size()).thenReturn(totalElements);

        // Act
        PageResponse<TransactionDO> result = transactionService.selectPage(currentPage, pageSize);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(totalElements, result.getTotalElements());
        verify(transactionRepository).size();
        verify(transactionRepository, never()).selectPage(anyInt(), anyInt());
    }

    @Test
    void selectPage_ShouldLimitPageSize() {
        // Arrange
        int currentPage = 1;
        int pageSize = 200; // > MAX_PAGE_SIZE (100)
        int totalElements = 150;
        List<TransactionDO> transactionList = Collections.singletonList(transactionDO);

        when(transactionRepository.size()).thenReturn(totalElements);
        when(transactionRepository.selectPage(0, 100)).thenReturn(transactionList); // Limited to 100

        // Act
        PageResponse<TransactionDO> result = transactionService.selectPage(currentPage, pageSize);

        // Assert
        assertNotNull(result);
        assertEquals(transactionList.size(), result.getContent().size());
        assertEquals(totalElements, result.getTotalElements());
        assertEquals(100, result.getPageSize()); // limited 100
        verify(transactionRepository).size();
        verify(transactionRepository).selectPage(0, 100);
    }

    @Test
    void selectOne_ReturnTrue() {
        // Arrange
        when(transactionRepository.selectOne(trxId)).thenReturn(transactionDO);

        TransactionDO result = transactionService.selectOne(trxId);

        // Assert
        assertNotNull(result);
        assertEquals(trxId, result.getTrxId());
        verify(transactionRepository).selectOne(trxId);
    }

    @Test
    void selectOne_NotExists() {
        // Arrange
        when(transactionRepository.selectOne(trxId)).thenReturn(null);

        TransactionDO result = transactionService.selectOne(trxId);

        assertNotNull(result);
        assertNull(result.getTrxId());
        assertNull(result.getAmount());
        assertNull(result.getStatus());

        // Assert
        verify(transactionRepository, times(1)).selectOne(trxId);
    }

    @Test
    void deleteTransaction_ReturnTrue() {
        // Arrange
        when(transactionRepository.exist(trxId)).thenReturn(true);
        when(transactionRepository.deletedById(trxId)).thenReturn(1);

        boolean result = transactionService.deleteTransaction(trxId);

        // Assert
        assertTrue(result);
        verify(transactionRepository).exist(trxId);
        verify(transactionRepository).deletedById(trxId);
    }

    @Test
    void deleteTransaction_NotFoundException() {
        // Arrange
        when(transactionRepository.exist(trxId)).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.deleteTransaction(trxId);
        });

        verify(transactionRepository).exist(trxId);
        verify(transactionRepository, never()).deletedById(anyString());
    }

    @Test
    void deleteTransaction_ReturnFalse() {
        // Arrange
        when(transactionRepository.exist(trxId)).thenReturn(true);
        when(transactionRepository.deletedById(trxId)).thenReturn(0);

        boolean result = transactionService.deleteTransaction(trxId);

        // Assert
        assertFalse(result);
        verify(transactionRepository).exist(trxId);
        verify(transactionRepository).deletedById(trxId);
    }

    @Test
    void updateTransaction_ReturnTrue() {
        // Arrange
        when(transactionRepository.exist(trxId)).thenReturn(true);
        when(transactionRepository.selectOne(trxId)).thenReturn(transactionDO);
        when(transactionRepository.updateById(any(TransactionDO.class))).thenReturn(1);

        boolean result = transactionService.updateTransaction(trxId, transactionUpdateReq);

        // Assert
        assertTrue(result);
        verify(transactionRepository).exist(trxId);
        verify(transactionRepository).selectOne(trxId);
        verify(transactionRepository).updateById(any(TransactionDO.class));
    }

    @Test
    void updateTransaction_TransactionNotFoundException() {
        // Arrange
        when(transactionRepository.exist(trxId)).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.updateTransaction(trxId, transactionUpdateReq);
        });

        verify(transactionRepository).exist(trxId);
        verify(transactionRepository, never()).selectOne(anyString());
        verify(transactionRepository, never()).updateById(any(TransactionDO.class));
    }

    @Test
    void updateTransaction_ReturnFalse() {
        // Arrange
        when(transactionRepository.exist(trxId)).thenReturn(true);
        when(transactionRepository.selectOne(trxId)).thenReturn(transactionDO);
        when(transactionRepository.updateById(any(TransactionDO.class))).thenReturn(0);

        boolean result = transactionService.updateTransaction(trxId, transactionUpdateReq);

        // Assert
        assertFalse(result);
        verify(transactionRepository).exist(trxId);
        verify(transactionRepository).selectOne(trxId);
        verify(transactionRepository).updateById(any(TransactionDO.class));
    }

    @Test
    void updateTransaction_OnlyUpdateAmount() {
        // Arrange
        TransactionUpdateReq partialUpdateReq = new TransactionUpdateReq(
                new BigDecimal("150.25"),
                null
        );


        when(transactionRepository.exist(trxId)).thenReturn(true);
        when(transactionRepository.selectOne(trxId)).thenReturn(transactionDO);
        when(transactionRepository.updateById(any(TransactionDO.class))).thenReturn(1);

        // Act
        boolean result = transactionService.updateTransaction(trxId, partialUpdateReq);

        assertTrue(result);
        verify(transactionRepository).exist(trxId);
        verify(transactionRepository).selectOne(trxId);
        verify(transactionRepository).updateById(any(TransactionDO.class));

        assertEquals("PENDING", transactionDO.getStatus());
        assertEquals(new BigDecimal("150.25"), transactionDO.getAmount());
    }

    @Test
    void updateTransaction_OnlyUpdateStatus() {
        // Arrange
        TransactionUpdateReq partialUpdateReq = new TransactionUpdateReq(
                null,
                "COMPLETED"
        );


        when(transactionRepository.exist(trxId)).thenReturn(true);
        when(transactionRepository.selectOne(trxId)).thenReturn(transactionDO);
        when(transactionRepository.updateById(any(TransactionDO.class))).thenReturn(1);

        // Act
        boolean result = transactionService.updateTransaction(trxId, partialUpdateReq);

        assertTrue(result);
        verify(transactionRepository).exist(trxId);
        verify(transactionRepository).selectOne(trxId);
        verify(transactionRepository).updateById(any(TransactionDO.class));

        assertEquals("COMPLETED", transactionDO.getStatus());
        assertEquals(new BigDecimal("100.50"), transactionDO.getAmount());
    }
}
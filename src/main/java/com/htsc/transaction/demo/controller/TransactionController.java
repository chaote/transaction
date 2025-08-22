package com.htsc.transaction.demo.controller;


import com.htsc.transaction.demo.model.PageResponse;
import com.htsc.transaction.demo.model.Response;
import com.htsc.transaction.demo.model.dos.TransactionDO;
import com.htsc.transaction.demo.model.request.TransactionCreateReq;
import com.htsc.transaction.demo.model.request.TransactionUpdateReq;
import com.htsc.transaction.demo.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @ author: chaote
 * @ date: 2025/8/20  19:12
 * @ <p><p>
 */
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public Response<Boolean> create(@Valid @RequestBody TransactionCreateReq req){
        return Response.success(transactionService.createTransaction(req));
    }

    @GetMapping("/page")
    public Response<PageResponse<TransactionDO>> selectPage(
            @RequestParam(value = "currentPage",required = false,defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize",required = false,defaultValue = "20") int pageSize){
        PageResponse<TransactionDO> transactionDOPageResponse
                = transactionService.selectPage(currentPage, pageSize);
        return Response.success(transactionDOPageResponse);
    }

    @GetMapping("/{trxId}")
    public Response<TransactionDO> selectOne(@PathVariable String trxId){
        return Response.success(transactionService.selectOne(trxId));
    }

    @DeleteMapping("/{trxId}")
    public Response<Boolean> delete(@PathVariable String trxId){
        return Response.success(transactionService.deleteTransaction(trxId));
    }

    @PatchMapping("/{trxId}")
    public Response<Boolean> update(
            @PathVariable String trxId,
            @RequestBody TransactionUpdateReq req){
        return Response.success(transactionService.updateTransaction(trxId,req));
    }
}

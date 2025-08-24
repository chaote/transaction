package com.htsc.transaction.demo.service;

import com.htsc.transaction.demo.exception.DuplicateTransactionException;
import com.htsc.transaction.demo.exception.TransactionNotFoundException;
import com.htsc.transaction.demo.model.PageResponse;
import com.htsc.transaction.demo.model.dos.TransactionDO;
import com.htsc.transaction.demo.model.request.TransactionCreateReq;
import com.htsc.transaction.demo.model.request.TransactionUpdateReq;
import com.htsc.transaction.demo.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ author: chaote
 * @ date: 2025/8/20  22:29
 * @ <p>交易服务类<p>
 */
@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private static final int MAX_PAGE_SIZE = 100;

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean createTransaction(TransactionCreateReq req){
        logger.info("create transaction,trxId:{}",req.trxId());
        if (transactionRepository.exist(req.trxId())){
            throw new DuplicateTransactionException(req.trxId());
        }
        TransactionDO transactionDO = new TransactionDO();
        BeanUtils.copyProperties(req,transactionDO);
        transactionDO.setTrxTime(new Date());
        int count = transactionRepository.save(transactionDO);
        return count > 0;
    }

    public PageResponse<TransactionDO> selectPage(int currentPage,int pageSize){

        //空
        if (currentPage < 1 || pageSize <=0){
            return PageResponse.convertPage(new ArrayList<>(),currentPage,pageSize,0);
        }

        //最大页
        int limitedPageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        //计算偏移量
        int offset = (currentPage - 1) * limitedPageSize;

        int totalElements = transactionRepository.size();

        if (offset >= totalElements) {
            return PageResponse.convertPage(new ArrayList<>(), currentPage, limitedPageSize, totalElements);
        }

        List<TransactionDO> transactionDO = transactionRepository.selectPage(offset,limitedPageSize);
        return PageResponse.convertPage(transactionDO,currentPage,limitedPageSize,totalElements);
    }

    public TransactionDO selectOne(String trxId){
        TransactionDO transactionDO = transactionRepository.selectOne(trxId);
        if (transactionDO == null){
            return new TransactionDO() ;
        }
        return transactionDO;
    }

    public boolean deleteTransaction(String trxId){
        logger.info("delete transaction,trxId:{}",trxId);
        //删除是个高风险操作，一般需要校验
        checkLegality(trxId);
        if (!transactionRepository.exist(trxId)){
            throw new TransactionNotFoundException(trxId);
        }
        int count = transactionRepository.deletedById(trxId);
        return count > 0;
    }

    public boolean updateTransaction(String trxId,TransactionUpdateReq req){
        logger.info("update transaction,trxId:{}",trxId);
        if (!transactionRepository.exist(trxId)){
            throw new TransactionNotFoundException(trxId);
        }

        TransactionDO oldValue = transactionRepository.selectOne(trxId);
        oldValue.setTrxId(trxId);
        if (req.amount() != null){
            oldValue.setAmount(req.amount());
        }
        if (req.status() != null){
            oldValue.setStatus(req.status());
        }

        int count = transactionRepository.updateById(oldValue);
        return count > 0;
    }

    private void checkLegality(String trxId) {
        //todo
        //根据业务规则校验合法合规性，比如某种状态下不允许删除等
    }

}

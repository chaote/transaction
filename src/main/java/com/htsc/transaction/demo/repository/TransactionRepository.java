package com.htsc.transaction.demo.repository;

import com.htsc.transaction.demo.dao.TransactionDAO;
import com.htsc.transaction.demo.model.dos.TransactionDO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ author: chaote
 * @ date: 2025/8/21  16:48
 * @ <p><p>
 */
@Repository
public class TransactionRepository {

    @Resource
    private TransactionDAO transactionDAO;

    public int save(TransactionDO transactionDO){
        return transactionDAO.save(transactionDO);
    }

    public int deletedById(String trxId){
        return transactionDAO.deleteById(trxId);
    }

    public int updateById(TransactionDO transactionDO){
        return transactionDAO.updateByTrxId(transactionDO);
    }

    public List<TransactionDO> selectPage(int offset,int limit){
        return transactionDAO.selectPage(offset, limit);
    }

    public TransactionDO selectOne(String trxId){
        return transactionDAO.selectOne(trxId);
    }
    public int size(){
        return transactionDAO.size();
    }

    public boolean exist(String trxId){
        return transactionDAO.exist(trxId);
    }







}

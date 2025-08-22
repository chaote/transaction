package com.htsc.transaction.demo.dao;

import com.htsc.transaction.demo.model.dos.TransactionDO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

/**
 * @ author: chaote
 * @ date: 2025/8/21  19:58
 * @ <p><p>
 */
@Component
public class TransactionDAO {

    //模拟数据库持久化存储
    //key：trxId
    private final Map<String, TransactionDO> STORE = new ConcurrentSkipListMap<>();

    public int save(TransactionDO transactionDO){
        try {
            STORE.put(transactionDO.getTrxId(), transactionDO);
        }catch (Exception e){
            return 0;
        }
        return 1;

    }

    public int deleteById(String trxId){
        try {
            STORE.remove(trxId);
        }catch (Exception e){
            return 0;
        }
        return 1;

    }

    public int updateByTrxId(TransactionDO transactionDO){
        try {
            STORE.put(transactionDO.getTrxId(), transactionDO);
        }catch (Exception e){
            return 0;
        }
        return 1;

    }

    public List<TransactionDO> selectPage(int offset,int limit){
        return STORE.values().stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public TransactionDO selectOne(String trxId){
        return STORE.get(trxId);
    }

    public int size(){
        return STORE.size();
    }

    public boolean exist(String trxId){
        return STORE.get(trxId) != null;
    }
}

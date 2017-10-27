package com.xuxl.number.biz.bo;

import com.google.common.collect.Maps;
import com.xuxl.number.api.exception.NoAvailableNumGenException;
import com.xuxl.number.biz.entity.NumGenEntity;
import com.xuxl.number.biz.entity.NumGenType;
import com.xuxl.number.biz.repository.NumGenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class NumTakeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(NumTakeManager.class);

    private static final String OPTIMISTIC_LOCK = "number.optimistic.lock";

//    private ConfigCache configCache = ConfigCache.getInstance();

    private Map<String, NumTake> numTakes = Maps.newHashMap();

    private AtomicInteger version;

    private DBNumTake.NumFillExecutor numFillExecutor;

    @Resource
    private NumGenRepository numGenRepository;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void init() {

        //需要乐观锁来刷新

        numFillExecutor = new DBNumTake.NumFillExecutor() {
            @Override
            public long execute(final String bizCode, final String subBizCode, final int fillSize) {
                return transactionTemplate.execute(new TransactionCallback<Long>() {
                    @Override
                    public Long doInTransaction(TransactionStatus status) {
                        try {
                            numGenRepository.updateValue(bizCode, subBizCode, fillSize);
                            NumGenEntity entity = numGenRepository.find(bizCode, subBizCode);
                            return entity.getCurrentValue();
                        } catch (Exception e) {
                            status.setRollbackOnly();
                        }
                        return 0L;
                    }
                });
            }
        };
        registerNumTakes();
    }

    private synchronized void registerNumTakes() {
        List<NumGenEntity> entities = numGenRepository.findAll();
        if (entities != null && !entities.isEmpty()) {
            NumTake numTake;
            for (NumGenEntity entity : entities) {
                Integer type = entity.getType();
                if (NumGenType.DB.getType() == type) {
                    numTake = new DBNumTake(entity.getBizCode(), entity.getSubBizCode(), entity.getBufferSize(), numFillExecutor);
                    registerNumTake(numTake);
                } else if (NumGenType.Redis.getType() == type) {
                    numTake = new RedisNumTake(entity.getBizCode(), entity.getSubBizCode(), entity.getCurrentValue(), redisTemplate);
                    registerNumTake(numTake);
                }
            }
        }
    }


    public long take(String bizCode, String subBizCode) {
        String key = String.format("%s_%s", bizCode, subBizCode);
        NumTake numTake = numTakes.get(key);
        if (numTake != null) {
            return numTake.take();
        }
        throw new NoAvailableNumGenException(bizCode, subBizCode);
    }

    private synchronized void registerNumTake(NumTake numTake) {
        String bizCode = numTake.getBizCode();
        String subBizCode = numTake.getSubBizCode();
        String key = String.format("%s_%s", bizCode, subBizCode);
        if (numTakes.containsKey(key)) {
            LOGGER.warn("bizCode:{},subBizCode:{} is exist", bizCode, subBizCode);
        } else {
            numTakes.put(key, numTake);
            numTake.init();
        }
    }

    @PreDestroy
    public void destroy() {
        for (NumTake numTake : numTakes.values()) {
            numTake.destroy();
        }
    }

}

package com.xuxl.number.biz.bo;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.Charset;

public class RedisNumTake extends AbstractNumTake {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    private StringRedisTemplate template;

    private final String key;

    private final long initValue;

    public RedisNumTake(String bizCode, String subBizCode, long initValue, StringRedisTemplate template) {
        super(bizCode, subBizCode);
        this.template = template;
        this.initValue = initValue;
        this.key = String.format("num:gen:%s:%s", getBizCode(), getSubBizCode());
    }

    @Override
    protected void init0() {
        this.template.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.setNX(key.getBytes(CHARSET), String.valueOf(initValue).getBytes(CHARSET));
            }
        });
    }

    @Override
    public long take() {
        return template.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.incr(key.getBytes(CHARSET));
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

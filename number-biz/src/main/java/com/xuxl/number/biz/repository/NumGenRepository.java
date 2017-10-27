package com.xuxl.number.biz.repository;

import com.xuxl.number.biz.entity.NumGenEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NumGenRepository {

    List<NumGenEntity> findAll();

    int updateValue(@Param("bizCode") String bizCode, @Param("subBizCode") String subBizCode, @Param("value") int value);

    NumGenEntity find(@Param("bizCode") String bizCode, @Param("subBizCode") String subBizCode);
}

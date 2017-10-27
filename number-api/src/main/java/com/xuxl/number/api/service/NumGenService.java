package com.xuxl.number.api.service;

public interface NumGenService {

    /**
     * 生成ID
     *
     * @param bizCode    业务号
     * @param subBizCode 子业务号
     * @return
     */
    long genLongID(String bizCode, String subBizCode);

}

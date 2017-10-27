package com.xuxl.number.biz.service;

import com.xuxl.number.biz.bo.NumTakeManager;
import com.xuxl.number.api.service.NumGenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("numGenService")
public class NumGenServiceImpl implements NumGenService {

    @Resource
    private NumTakeManager numTakeManager;

    @Override
    public long genLongID(String bizCode, String subBizCode) {
        return numTakeManager.take(bizCode, subBizCode);
    }
}

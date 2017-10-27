package com.xuxl.number.biz.bo;

public interface NumTake {

    void init();

    void destroy();

    long take();

    String getBizCode();

    String getSubBizCode();


}

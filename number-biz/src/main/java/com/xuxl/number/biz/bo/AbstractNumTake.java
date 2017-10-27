package com.xuxl.number.biz.bo;

import java.util.Objects;

public abstract class AbstractNumTake implements NumTake {

    private final String bizCode;

    private final String subBizCode;

    private volatile boolean isInit = false;

    public AbstractNumTake(String bizCode, String subBizCode) {
        this.bizCode = bizCode;
        this.subBizCode = subBizCode;
    }

    @Override
    public String getBizCode() {
        return bizCode;
    }

    @Override
    public String getSubBizCode() {
        return subBizCode;
    }

    @Override
    public void init() {
        if (!isInit) {
            isInit = true;
            init0();
        }
    }

    protected abstract void init0();

    @Override
    public void destroy() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractNumTake that = (AbstractNumTake) o;
        return Objects.equals(bizCode, that.bizCode) &&
                Objects.equals(subBizCode, that.subBizCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bizCode, subBizCode);
    }
}

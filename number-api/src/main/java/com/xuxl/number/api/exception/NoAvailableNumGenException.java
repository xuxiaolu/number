package com.xuxl.number.api.exception;

public class NoAvailableNumGenException extends RuntimeException {

    private static final long serialVersionUID = 7042875611073685332L;

    public NoAvailableNumGenException(String bizCode, String subBizCode) {
        super(String.format("bizCode: %s,subBizCode: %s,is not exists", bizCode, subBizCode));
    }
}

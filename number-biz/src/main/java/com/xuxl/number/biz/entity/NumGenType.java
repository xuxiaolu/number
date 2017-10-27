package com.xuxl.number.biz.entity;

public enum NumGenType {

    DB(1),
    Redis(2);

    private final int type;

    NumGenType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}

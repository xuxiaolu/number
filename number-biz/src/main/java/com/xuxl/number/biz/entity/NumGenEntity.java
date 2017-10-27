package com.xuxl.number.biz.entity;

import java.io.Serializable;
import java.util.Date;

public class NumGenEntity implements Serializable {

    private static final long serialVersionUID = -6259050669060603134L;

    // 自增长ID
    private Integer id;

    // 业务码
    private String bizCode;

    //子业务码
    private String subBizCode;

    // 业务说明
    private String bizDesc;

    //使用哪种方式生成
    private Integer type;

    // 最新值，db方式比较准，redis方式是初始值
    private Long currentValue;

    // 缓冲区大小，即队列还剩多少时候去获取下一批序号
    private Integer bufferSize;

    // 创建时间
    private Date createTime;

    // 更新时间
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getSubBizCode() {
        return subBizCode;
    }

    public void setSubBizCode(String subBizCode) {
        this.subBizCode = subBizCode;
    }

    public String getBizDesc() {
        return bizDesc;
    }

    public void setBizDesc(String bizDesc) {
        this.bizDesc = bizDesc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Long currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NumGenEntity{");
        sb.append("id=").append(id);
        sb.append(", bizCode='").append(bizCode).append('\'');
        sb.append(", subBizCode='").append(subBizCode).append('\'');
        sb.append(", bizDesc='").append(bizDesc).append('\'');
        sb.append(", type=").append(type);
        sb.append(", currentValue=").append(currentValue);
        sb.append(", bufferSize=").append(bufferSize);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }
}

package com.lee.async.event.common.enums;

/**
 * @author liwei
 * @date 2020/06/16
 */
public enum EventStatus implements CodeAndDescEnum{
    CREATE(10, "创建"),
    SENT(20, "已发送"),
    FINISH(30, "完成"),
    FAIL(99,"失败");


    private final int code;
    private final String desc;

    EventStatus(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}

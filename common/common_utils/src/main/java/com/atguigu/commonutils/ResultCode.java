package com.atguigu.commonutils;

import lombok.Getter;

@Getter
public enum ResultCode {
    /**
     * 返回结果枚举，每个枚举代表着一个状态
     */

    SUCCESS(20000, "操作成功！"),
    ERROR(20001, "操作失败！");
    private Integer code;
    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

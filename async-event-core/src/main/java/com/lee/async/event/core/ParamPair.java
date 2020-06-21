package com.lee.async.event.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liwei
 * @date 2020/06/20
 */
@Getter
@Setter
public class ParamPair {
    private String paramClassStr;
    private String paramValueStr;

    public ParamPair() {
    }

    public ParamPair(String paramClassStr, String paramValueStr) {
        this.paramClassStr = paramClassStr;
        this.paramValueStr = paramValueStr;
    }
}

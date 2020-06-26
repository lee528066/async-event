package com.lee.async.event.core;

/**
 * @author liwei
 * @date 2020/06/20
 */
public class ParamPair {
    private String paramClassStr;
    private String paramValueStr;

    public ParamPair() {
    }

    public ParamPair(String paramClassStr, String paramValueStr) {
        this.paramClassStr = paramClassStr;
        this.paramValueStr = paramValueStr;
    }

    public String getParamClassStr() {
        return paramClassStr;
    }

    public void setParamClassStr(String paramClassStr) {
        this.paramClassStr = paramClassStr;
    }

    public String getParamValueStr() {
        return paramValueStr;
    }

    public void setParamValueStr(String paramValueStr) {
        this.paramValueStr = paramValueStr;
    }
}

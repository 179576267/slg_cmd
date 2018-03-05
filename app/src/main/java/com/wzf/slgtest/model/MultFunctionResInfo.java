package com.wzf.slgtest.model;

/**
 * @Description:
 * @author: wangzhenfei
 * @date: 2018-01-16 10:18
 */

public class MultFunctionResInfo {
    public int msgId;
    public String status;
    public String account;
    public String objectIndex;

    public MultFunctionResInfo(int msgId, String status, String account, String objectIndex) {
        this.msgId = msgId;
        this.status = status;
        this.account = account;
        this.objectIndex = objectIndex;
    }
}

package com.wzf.slgtest.model;

import android.text.TextUtils;

/**
 * @Description:
 * @author: wangzhenfei
 * @date: 2018-01-09 15:28
 */

public class TcpHandlerParam {

    public String account;
    public String battleId;
    public String objectIndex;


    public TcpHandlerParam(String account) {
        this.account = account;
    }


    public TcpHandlerParam(String account, String battleId, String objectIndex) {
        this.account = account;
        this.battleId = battleId;
        this.objectIndex = objectIndex;
    }

    public boolean isMain(){
        return TextUtils.isEmpty(battleId);
    }
}

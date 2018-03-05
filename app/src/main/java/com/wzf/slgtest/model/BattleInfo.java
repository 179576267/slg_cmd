package com.wzf.slgtest.model;

import com.douqu.game.core.protobuf.SGCommonProto;

/**
 * @Description:
 * @author: wangzhenfei
 * @date: 2018-01-09 15:18
 */

public class BattleInfo {
    public String account;
    public SGCommonProto.ServerInfo serverInfo;
    public String objectIndex;
    public String battleId;


    public BattleInfo(String account, SGCommonProto.ServerInfo serverInfo, String objectIndex, String battleId) {
        this.account = account;
        this.serverInfo = serverInfo;
        this.objectIndex = objectIndex;
        this.battleId = battleId;
    }
}

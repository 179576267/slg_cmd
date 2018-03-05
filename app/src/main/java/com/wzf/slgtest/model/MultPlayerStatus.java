package com.wzf.slgtest.model;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Description:
 * @author: wangzhenfei
 * @date: 2018-01-16 10:40
 */

public class MultPlayerStatus extends PlayerStatus {
    public ChannelHandlerContext ctx;
    public long start;
    public long end;
    public MultPlayerStatus(String account, String status, String objectIndex, ChannelHandlerContext ctx) {
        super(account, status, objectIndex);
        this.ctx = ctx;
    }

    public long getOffsetTime(){
        if(end > start){
            return end - start;
        }
        return 0;
    }
}

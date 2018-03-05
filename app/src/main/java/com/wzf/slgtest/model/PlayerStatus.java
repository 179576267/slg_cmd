package com.wzf.slgtest.model;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Administrator
 */
public class PlayerStatus {
        public PlayerStatus(String account, String status, String objectIndex) {
            this.account = account;
            this.status = status;
            this.objectIndex = objectIndex;
        }



    public String account;
        public String status;
        public String objectIndex;

    }
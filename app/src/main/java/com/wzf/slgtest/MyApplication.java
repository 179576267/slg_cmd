package com.wzf.slgtest;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Description:
 * @author: wangzhenfei
 * @date: 2017-10-13 10:57
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication application;
    private ChannelHandlerContext ctx;
    public String objecIndex;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public  static  MyApplication getAppInstance(){
        return application;
    }
}

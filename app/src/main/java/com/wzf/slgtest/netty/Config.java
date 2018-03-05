package com.wzf.slgtest.netty;

/**
 * @Description:
 * @author: wangzhenfei
 * @date: 2017-10-13 11:00
 */

public class Config {
    public static final String IP_ZHENFEI = "bean326.imwork.net";
    public static final String IP_PENGYU = "192.168.2.234";
    public static final String IP_SANSHAO = "192.168.2.228";
    public static final String IP_249 = "192.168.2.249";
    public static final String IP_PRODUCT = "118.25.8.249";
    public static final ServerConfig [] servers = new ServerConfig[]{
        new ServerConfig("振飞", IP_ZHENFEI),
        new ServerConfig("三少", IP_SANSHAO),
        new ServerConfig("彭宇", IP_PENGYU),
        new ServerConfig("249", IP_249),
        new ServerConfig("外网测试", IP_PRODUCT),
    };

    public static String IP = IP_PRODUCT;
    public static String ACCOUNT = "10009";

    /**
     * 多账户功能测试
     */
    public static boolean multPlayerTest;

    public static int msgId;
    public static byte [] data;

    public static final String fullAccount =
            "1;6101;100000|1;6102;100000|1;6103;100000|1;6104;100000|1;6105;100000|1;6106;100000|1;6107;100000|1;6108;100000|1;6109;100000|" +
            "1;6201;100000|1;6202;100000|1;6203;100000|1;6204;100000|1;6205;100000|1;6206;100000|1;6207;100000|1;6208;100000|1;6209;100000|" +
            "1;6301;100000|1;6302;100000|1;6303;100000|1;6304;100000|1;6305;100000|1;6306;100000|1;6307;100000|1;6308;100000|1;6309;100000|" +
            "1;6401;100000|1;6402;100000|1;6403;100000|1;6404;100000|1;6405;100000|1;6406;100000|1;6407;100000|1;6408;100000|1;6409;100000|"+
            "1;21;1000000|" +//精炼石
            "1;44;1000000|" +// 顶级经验书
            "2;14;1000000|" +// 强化石
            "1;31;1000000|" +//英雄纹章
            "2;11;100000000|" +//金币
            "2;13;1000000|" +//英魂
            "2;101;10000000|" + //荣誉
            "2;14;10000000"; //魔晶
}

package com.wzf.slgtest.utils;

/**
 * 字符串工具类
 *
 * @author tangjun
 */
public class StringUtils {

    /**
     * string 转换为 int 异常捕获
     *
     * @param code
     * @return
     */
    public static int stringToInt(String code) {
        int num = 0;
        try {
            num = Integer.parseInt(code == null ? "0" : code);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return num;
    }

    /*
    * string 转为 int 并去掉开头的 0
    * */
    public static int stringToIntRemoveStartZero(String code) {
        String newStr = code.replaceFirst("^0*", "");
        return Integer.parseInt(newStr);
    }

    /**
     * string 转换为 double 异常捕获
     *
     * @param code
     * @return
     */
    public static double stringToDouble(String code) {
        double num = 0;
        try {
            num = Double.parseDouble(code == null ? "0" : code);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return num;
    }


    /**
     * string 转换为 Long 异常捕获
     *
     * @param code
     * @return
     */
    public static long stringToLong(String code) {
        long num = 0;
        try {
            num = Long.parseLong(code == null ? "0" : code);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return num;
    }

}

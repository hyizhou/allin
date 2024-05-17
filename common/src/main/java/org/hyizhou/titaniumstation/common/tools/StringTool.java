package org.hyizhou.titaniumstation.common.tools;

/**
 * 字符串工具类
 * @author hyizhou
 * @date 2024/2/4
 */
public class StringTool {
    /**
     * 将数字字符串转换成整数
     */
    public static Integer toDouble(String str){
        if (str == null || str.length() == 0){
            return 0;
        }
        // 判断字符是否表达整数，不是将通过异常提醒更换对应方法
        if (!str.matches("^\\d+$")){
            throw new IllegalArgumentException("该字符串不是整数");
        }
        return Integer.parseInt(str);
    }
}

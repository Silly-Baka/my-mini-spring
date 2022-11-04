package sillybaka.springframework.utils;

/**
 * 使用一定规则来处理字符串值的策略接口
 * <p>Date: 2022/11/4
 * <p>Time: 18:03
 *
 * @Author SillyBaka
 **/
@FunctionalInterface
public interface StringValueResolver {


    /**
     * 按照一定规则解析给定的字符串
     * @param str 需要被解析的字符串
     * @return 解析后的字符串
     */
    String resolveStringValue(String str);
}

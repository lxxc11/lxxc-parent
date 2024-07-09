package com.lvxc.common.utils;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;

/**
 * @author zhangl
 * @description: 过滤自定义敏感词
 * @date 2024/4/7
 */
public class FilterSensitiveWordsUtil {

    /**
     * 判断当前文本是否是敏感词
     *
     * @param word
     * @return
     */
    public static Boolean isContainsSensitive(String word) {
        return SensitiveWordHelper.contains(word);
    }

    /**
     * 用星号替换敏感词
     *
     * @param text
     * @return
     */
    public static String replaceSensitive(String text) {
        return SensitiveWordHelper.replace(text);
    }

    /**
     * 用words替换的敏感词
     *
     * @param text
     * @return
     */
    public static String replaceDefineSensitive(String text, char words) {
        return SensitiveWordHelper.replace(text, words);
    }



}

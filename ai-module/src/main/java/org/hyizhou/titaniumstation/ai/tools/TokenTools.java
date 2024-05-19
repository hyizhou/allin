package org.hyizhou.titaniumstation.ai.tools;

/**
 * 本地简易计算Token，每个中文字、空格、标点、英文单词算一个token
 * @date 2024/5/18
 */
public class TokenTools{
    public static long simpleCount(String text){
        long tokenCount = 0;
        boolean inEnglishWord = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // 检查是否为汉字
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                tokenCount++;
                inEnglishWord = false; // 遇到汉字，重置英文单词标志
            }
            // 检查是否为英文字符，连续的英文字符视为一个单词
            else if (Character.isLetter(c)) {
                if (!inEnglishWord) {
                    tokenCount++; // 开始一个新的英文单词
                    inEnglishWord = true;
                }
            }
            // 标点符号、空格或其他非字母非汉字字符也算作一个token
            else {
                tokenCount++;
                inEnglishWord = false; // 遇到标点符号、空格或非字母非汉字字符，重置英文单词标志
            }
        }

        // 确保最后一个字符如果是英文且属于一个单词，则计入token
        if (inEnglishWord && Character.isLetter(text.charAt(text.length() - 1))) {
            tokenCount++;
        }

        return tokenCount;
    }
}

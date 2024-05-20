package org.hyizhou.titaniumstation.ai.tools;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.*;

import java.util.Arrays;

/**
 * 本地简易计算Token，每个中文字、空格、标点、英文单词算一个token
 * @date 2024/5/18
 */
public class TokenTools{
    private static final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();

    /**
     * 使用 GPT3.5 的分词规则，简易计算的token数。<br>
     * 注意此处只是用于本地简易计算，不同模型间计算方式不同，具体得看请求模型后的返回参数
     * @param text 文本
     * @return token数
     */
    public static int simpleCount(String text){
        Encoding enc = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
        IntArrayList intArrayList = enc.encode(text);
        return intArrayList.size();
    }

    /**
     * 按照 token 分词规则截取字符串
     */
    public static String subString(String text, int start, int end){
        if (start > end){
            throw new IllegalArgumentException("start must be less than end");
        }
        Encoding enc = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
        IntArrayList encoded = enc.encode(text);
        if (encoded.size() > end){
            throw new IllegalArgumentException(String.format("end must be less than text token size. Token size=%d, end=%d", encoded.size(), end));
        }
        int[] encodedArr = Arrays.copyOfRange(encoded.toArray(), start, end);
        IntArrayList beforeEncoded = new IntArrayList();
        for (int i : encodedArr) {
            beforeEncoded.add(i);
        }
        return enc.decode(beforeEncoded);
    }


    public static void main(String[] args) {
        String s = "今天天气晴朗，the weather good.";
        System.out.println(simpleCount(s));
        System.out.println(subString(s, 1, simpleCount(s)));
    }
}

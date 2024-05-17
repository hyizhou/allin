package org.hyizhou.titaniumstation.common.ai.response;

/**
 * 响应消息对象
 * @date 2024/5/17
 */
public record ContentResp(
        /*
        消息内容，这里要与type字段搭配，若是字符类型则这里就是文本内容，若是媒体类型，则这里存放媒体id，需额外到媒体接口获取
         */
        String content,
        /*
        消息类型，分为媒体和文本类型
         */
        String type,
        /*
        消息角色
         */
        String role,
        /*
        对话id
         */
        String id,
        /*
        结束标志，此字段主要用于流式处理中，若是stop则结束
         */
        String finishReason,
        /*
        提示词token数
         */
        Long promptTokens,
        /*
        生成token数
         */
        Long generationTokens
) {
}

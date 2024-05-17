package org.hyizhou.titaniumstation.ai.llmTools;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.function.Function;

/**
 * 一个“函数工具”的示例
 * <p>
 * 函数工具创建并注入过程：<br>
 * 方法一：实现 Function 接口实现函数功能，将其注入Bean中 <br>
 * 方法二（推荐）：实现 Function 接口，通过 FunctionCallbackWrapper 包装成 FunctionCallback(函数回调对象），注入到Bean中
 * <p>
 * AbstractFunctionCallSupport类中 functionCallbackContext 属性，也就是 FunctionCallbackContext 类，专门用来在Spring容器中
 * 获取到“函数回调”。
 * <p>
 * 此类并未自动创建，若未null的话，则“函数回调”只能从 xxxChatClient 的 xxxChatOptions 中得到，
 * @author hyizhou
 * @date 2024/4/30
 */
public class Weather implements Function<Weather.Request, Weather.Response> {
    @JsonClassDescription("查询城市天气")  // 无函数描述时此就是函数描述，只是位置不同，这个注解可以理解为对参数的解释
    public record Request(
            @JsonProperty(required = true) @JsonPropertyDescription("城市名称") String city ,
            @JsonProperty(required = true) @JsonPropertyDescription("城市所在省份") String x ){}
    public record Response(String weather, Unit unit){}
    public enum Unit { C, F }

    @Override
    public Response apply(Request city) {
        System.out.println("!!!!!!调用了方法一次!!!!!!!!!");
        return new Response("晴，北风三级，30度到39度，狮子座有大量流星雨，能见度高，适合观看", Unit.C);
    }
}

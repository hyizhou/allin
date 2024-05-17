package org.hyizhou.titaniumstation.ai.function;

import org.springframework.ai.model.function.AbstractFunctionCallSupport;
import org.springframework.ai.model.function.FunctionCallbackContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是对 AbstractFunctionCallSupport 类的扩展，添加了流式处理的功能
 * @author hyizhou
 * @date 2024/5/8
 */
public abstract class AbstractFunctionCallSupportPro<Msg, Completion,Req, Resp>  extends AbstractFunctionCallSupport<Msg, Req, Resp> {
    protected AbstractFunctionCallSupportPro(FunctionCallbackContext functionCallbackContext) {
        super(functionCallbackContext);
    }

    /*
    流式响应是否为增量模式，若是增量模式，则需要将
     */
    private boolean isIncremental = false;
    public void setIncremental(boolean incremental){
        this.isIncremental = incremental;
    }

    /**
     * 流式的函数工具调用，此处默认流式调用并非增量模式
     * 判断flux前5个元素，若方法isToolFunctionCall判断通过，则等待最后一个元素出现，交给doStreamChatCompletion处理，返回其返回值
     * 前五个元素都没有函数类型，则返回这个原始的flux
     */
    protected Flux<Completion> callSteamWithFunctionSupport(Req request){
        Flux<Completion> flux = doStreamChatCompletion(request);
        // 一个标志，记录是否为函数调用格式
        boolean isFunction = true;
        if (isIncremental) {
            // TODO 暂空缺
            return Flux.empty();
        }else {
            // 缓存重要，不然会产生多个流
            Flux<Completion> cacheFlux = flux.cache(100);
            Mono<Boolean> hasFunctionCall = cacheFlux.take(3).any(this::isToolFunction);
            Mono<Flux<Completion>> resultWithoutFunction = hasFunctionCall.filter(e -> !e).map(b -> cacheFlux);
            Mono<Flux<Completion>> resultWithFunction = hasFunctionCall.filter(e -> e).flatMap(e -> cacheFlux.last().map(resp -> handleFunctionCallOrReturnOfSteam(request, resp)));

            return resultWithFunction.switchIfEmpty(resultWithoutFunction).flatMapMany(Flux::from);
        }
    }

    /**
     * 对大语言模型流式调用
     */
    abstract protected Flux<Completion> doStreamChatCompletion(Req request);

    /**
     * 在外部判断是否为函数调用消息，然后调用本方法
     * @param request
     * @param completion
     * @return
     */
    public Flux<Completion> handleFunctionCallOrReturnOfSteam(Req request, Completion completion){
        Resp resp = warpWith(completion);
        Req requestUpdate = handleFunctionCall(request, resp);
        return callSteamWithFunctionSupport(requestUpdate);

    }


    /**
     * 调用函数并将结果封装进新的请求
     */
    private Req handleFunctionCall(Req request, Resp response){

        List<Msg> conversationHistory = new ArrayList<>(this.doGetUserMessages(request));

        Msg responseMessage = this.doGetToolResponseMessage(response);

        // Add the assistant response to the message conversation history.
        conversationHistory.add(responseMessage);

        return this.doCreateToolResponseRequest(request, responseMessage, conversationHistory);
    }

    /**
     * 用于流中元素判断，是否为函数调用类型
     */
    abstract protected boolean isToolFunction(Completion completion);

    /**
     * 为了复用代码，包装成Resp类型
     */
    abstract protected Resp warpWith(Completion completion);
}
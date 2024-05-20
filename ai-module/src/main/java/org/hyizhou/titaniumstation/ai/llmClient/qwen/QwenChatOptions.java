package org.hyizhou.titaniumstation.ai.llmClient.qwen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallingOptions;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.*;

/**
 * qwen模型的参数。由于原始格式参数嵌套在不同层级，此处将所有平铺在此类中以便统一赋值
 *
 * 注意：变量名和json名一定要与目标处的变量名一致，这样才能通过 `ModelOptionsUtils.merge` 方法将值合并到目标上
 * @author hyizhou
 * @date 2024/4/23
 */
public class QwenChatOptions implements ChatOptions, FunctionCallingOptions {

    private @JsonProperty("model") String model;
    private @JsonProperty("temperature") Float temperature;
    private @JsonProperty("top_p") Float topP;
    private @JsonProperty("top_k") Integer topK;

    /*
    流式请求时使用，true表使用增量模式
     */
    private @JsonProperty("incremental_output") Boolean incrementalOutput;

    private @JsonProperty("result_format") QwenAiApi.ResultFormat resultFormat;

    private @JsonProperty("enable_search") Boolean enableSearch;

    /*
    插件列表，格式`{"name": {}, "name2":{"param": "value"}}`
     */
    private Map<String, Object> plugins;

    /*
     工具列表，模型将收到json格式表示的工具函数列表
     */
    @NestedConfigurationProperty
    private @JsonProperty("tools") List<QwenAiApi.FunctionTool> tools;

    /*
      存储所有能被调用的函数，不过默认不启用
     */
    @NestedConfigurationProperty
    @JsonIgnore
    private List<FunctionCallback> functionCallbacks = new ArrayList<>();

    /*
      会话中供AI调用的函数列表，可以理解为启用这里指定的函数
     */
    @NestedConfigurationProperty
    @JsonIgnore
    private Set<String> functions = new HashSet<>();

    @Override
    public List<FunctionCallback> getFunctionCallbacks() {
        return this.functionCallbacks;
    }

    @Override
    public void setFunctionCallbacks(List<FunctionCallback> functionCallbacks) {
        this.functionCallbacks = functionCallbacks;
    }

    @Override
    public Set<String> getFunctions() {
        return this.functions;
    }

    @Override
    public void setFunctions(Set<String> functions) {
        this.functions = functions;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Float getTemperature() {
        return this.temperature;
    }

    @Override
    public Float getTopP() {
        return this.topP;
    }

    @Override
    public Integer getTopK() {
        return this.topK;
    }

    public String getModel(){
        return this.model;
    }

    public Map<String, Object> getPlugins() {
        return this.plugins;
    }

    public QwenAiApi.ResultFormat getResultFormat() {
        return this.resultFormat;
    }

    public Boolean getEnableSearch() {
        return this.enableSearch;
    }

    /**
     * builder类，用于构建 QwenAIChatOptions
     */
    public static class Builder {
        protected QwenChatOptions options;

        public Builder() {
            this.options  = new QwenChatOptions();
        }

        public Builder withMode(String model) {
            this.options.model = model;
            return this;
        }

        public Builder withIncrementalOutput(boolean bool){
            this.options.incrementalOutput = bool;
            return this;
        }

        public Builder withResultFormat(QwenAiApi.ResultFormat resultFormat){
            this.options.resultFormat = resultFormat;
            return this;
        }

        public Builder withEnableSearch(boolean bool){
            this.options.enableSearch = bool;
            return this;
        }

        /**
         * 通过此处添加插件信息
         * @param name 插件名
         * @param params 插件参数，若无参可填null
         */
        public Builder addPlugins(String name, Map<String, Object> params) {
            if (params == null){
                params = new HashMap<>();
            }
            if (this.options.plugins == null) {
                this.options.plugins = Map.of(name, params);
            } else {
                this.options.plugins.put(name, params);
            }
            return this;
        }

        public Builder withTools(List<QwenAiApi.FunctionTool> tools){
            this.options.tools = tools;
            return this;
        }

        public Builder withFunction(String function){
            this.options.functions.add(function);
            return this;
        }

        public QwenChatOptions build() {
            return this.options;
        }
    }

}

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

import java.util.Arrays;

/**
 * 通过Qwen官方API测试模型
 * @author hyizhou
 * @date 2024/4/23
 */
public class TestOfficialQwen {
    public static void callWithMessage() throws NoApiKeyException, InputRequiredException {
        // 生成
        Generation gen = new Generation();
        Message userMsg = Message.builder().role(Role.USER.getValue())
                .content("介绍你自己").build();
        GenerationParam param =
                GenerationParam.builder().model("qwen1.5-72b-chat")
                        .messages(Arrays.asList(userMsg))
                        .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                        .topP(0.8)
                        .build();
        GenerationResult result = gen.call(param);
        System.out.println(result);
    }

    public static void main(String[] args) {
//        System.setProperty("DASHSCOPE_API_KEY", "sk-73bd3760b32344e68ce95795d8d22613");
        try {
            callWithMessage();
        } catch (NoApiKeyException | InputRequiredException e) {
            e.printStackTrace();
        }
    }
}

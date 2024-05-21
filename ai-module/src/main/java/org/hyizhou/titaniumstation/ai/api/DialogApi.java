package org.hyizhou.titaniumstation.ai.api;

import org.hyizhou.titaniumstation.ai.service.DialogService;
import org.hyizhou.titaniumstation.common.ai.request.CreationDialogReq;
import org.hyizhou.titaniumstation.common.ai.request.UpdateDialogReq;
import org.hyizhou.titaniumstation.common.ai.response.DialogInfoResp;
import org.springframework.web.bind.annotation.*;

/**
 * 会话相关api
 * @date 2024/5/16
 */
@RestController
@RequestMapping("/api/ai")
public class DialogApi {
    private final DialogService dialogService;

    public DialogApi(DialogService dialogService) {
        this.dialogService = dialogService;
    }


    /**
     * 创建一个会话
     * @param model 创建信息，提供服务商名称、模型名
     * @return 创建会话的唯一id
     */
    @PostMapping("/dialogs")
    public String createDialog(@RequestBody CreationDialogReq model){
        return dialogService.create(model);
    }


    /**
     * 删除指定的会话
     * @param dialogId 会话id
     */
    @DeleteMapping("/dialogs/{id}")
    public void deleteDialog(@PathVariable("id") String dialogId){
        dialogService.delete(dialogId);
    }

    /**
     * 获取会话信息，这也包含相关策略
     * @param dialogId 会话id
     * @return 会话信息
     */
    @GetMapping("/dialogs/{id}")
    public DialogInfoResp getDialogInfo(@PathVariable("id") String dialogId){
        return dialogService.getDialogInfo(dialogId);
    }

    @PutMapping("/dialogs/{id}")
    public void updateDialog(@PathVariable("id") String dialogId, @RequestBody UpdateDialogReq model){
        dialogService.updateDialogInfo(dialogId, model);
    }
}

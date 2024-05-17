package org.hyizhou.titaniumstation.ai.api;

import org.hyizhou.titaniumstation.ai.service.DialogService;
import org.hyizhou.titaniumstation.common.ai.request.CreationDialogReq;
import org.springframework.web.bind.annotation.*;

/**
 * 会话相关api
 * @date 2024/5/16
 */
@RestController
@RequestMapping("/api/ai/dialog")
public class DialogApi {
    private final DialogService dialogService;

    public DialogApi(DialogService dialogService) {
        this.dialogService = dialogService;
    }


    @PostMapping("/create")
    public String createDialog(@RequestBody CreationDialogReq model){
        return dialogService.create(model);
    }


    @DeleteMapping("/delete")
    public void deleteDialog(String dialogId){
        dialogService.delete(dialogId);
    }
}

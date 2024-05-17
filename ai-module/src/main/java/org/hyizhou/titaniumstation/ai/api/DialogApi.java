package org.hyizhou.titaniumstation.ai.api;

import org.hyizhou.titaniumstation.ai.service.DialogService;
import org.hyizhou.titaniumstation.common.ai.model.CreationDialogModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String createDialog(@RequestBody CreationDialogModel model){
        return dialogService.create(model);
    }
}

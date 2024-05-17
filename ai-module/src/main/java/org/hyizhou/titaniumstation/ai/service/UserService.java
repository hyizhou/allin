package org.hyizhou.titaniumstation.ai.service;

import lombok.Getter;
import lombok.Setter;
import org.hyizhou.titaniumstation.common.entity.UserEntity;
import org.springframework.stereotype.Service;

/**
 * 获取用户的Service
 * @date 2024/5/17
 */
@Service
public class UserService {

    public UserEntity user = new UserEntity(1, "test");
    @Getter
    @Setter
    private String currentDialogId;

    /**
     * 获取当前用户
     * @return 用户实体
     */
    public UserEntity getCurrentUser(){
        return user;
    }

}

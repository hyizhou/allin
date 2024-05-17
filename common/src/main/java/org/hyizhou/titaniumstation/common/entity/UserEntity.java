package org.hyizhou.titaniumstation.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 用户数据表
 * @date 2024/5/16
 */

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private int id;

    @Column
    private String name;

    public UserEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

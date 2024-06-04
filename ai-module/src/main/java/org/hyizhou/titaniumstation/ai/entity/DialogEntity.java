package org.hyizhou.titaniumstation.ai.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyizhou.titaniumstation.common.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对话表实体类
 * @date 2024/5/16
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "dialogs")
public class DialogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String dialogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 假设有一个User实体类代表用户

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String serviceProvider;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(columnDefinition = "TEXT")
    private String dialogsSummary; // 假设使用TEXT类型存储对话总结

    /*
    一对一的关系，级联满上
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "history_strategy_id", nullable = true)
    private HistoryStrategyEntity historyStrategy;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isBusy;

    @Column(nullable = true)
    @Convert(converter = JsonStringToListConverter.class)
    private List<String> functions;


    // 自定义转换器，json和list转换
    static class JsonStringToListConverter implements AttributeConverter<List<String>, String> {
        @Override
        public String convertToDatabaseColumn(List<String> attribute) {
            if (attribute == null) {
                return null;
            }
            return new Gson().toJson(attribute);
        }

        @Override
        public List<String> convertToEntityAttribute(String dbData) {
            return new Gson().fromJson(dbData, new TypeToken<List<String>>(){}.getType());
        }
    }
}

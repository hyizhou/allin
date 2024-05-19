## 模块介绍

此模块专为大语言模型提供支持，支持通义千问（qwen），只需要简单修改配置文件即可切换。

在Spring AI 基础上，进一步拓展了支持的模型。

## 进度
- 通义千问：
  - [x] 普通调用回调函数支持 
  - [x] 流式调用回调函数支持

## 表设计
### 对话表 (dialogs): 存储对话的基本信息。

- dialog_id (主键, 自增): 对话唯一标识。
- user_id (外键): 发起对话的用户ID。
- model : 大语言模型名
- service_provider：模型提供商
- start_time: 对话开始时间。
- dialogs_summary：对话总结
- history_strategy_id (外键，可空): 关联到历史策略表的历史策略ID。


### 消息表 (messages): 存储每条消息的详细信息。

- message_id (主键, 自增): 消息唯一标识。
- dialog_id (外键): 关联到对话表的对话ID。
- is_summary (bool): 是否为摘要消息。
- content: 消息内容。
- timestamp: 消息发送时间。
- role：消息角色
- type：消息类型

### history_strategy (历史策略): 对话历史传递策略的详细信息。
- strategy_id (主键, 自增): 历史策略唯一标识。
- close (bool)：关闭策略，全局、用户、会话策略全都不对此会话生效，所有消息原始传递。
- token_size (长整数)：限制传递的token数量，可为空
- message_size (长整数)：限制传递的消息条数，可为空
- summary_rule (枚举)：摘要生成策略，可为空。为空时不生效，可选：关键字保留、对话总结 两种策略

### user_ai_config (用户AI配置): 用户AI部分配置的详细信息。
- id (主键, 自增): 表唯一标识。
- user_id： 关联到用户表的用户ID。
- history_strategy_id (外键): 关联到历史策略表的历史策略ID，作为默认历史传递策略。
- dialog_id (外键): 关联到对话表的对话ID，作为默认对话属性。

### prompt_templates (prompt模板): 存储提示词模板的详细信息。
- template_id (主键, 自增): 模板唯一标识。
- description：对此模板的描述。
- category：模板分类。如问候、帮助等
- template_text：提示词内容
- example：对话示例，这里要存储固定结构的文本，不过这里不用管
- create_time：模板创建时间
- update_time：模板最后更新时间
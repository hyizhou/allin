## 模块介绍

此模块专为大语言模型提供支持，支持通义千问（qwen），只需要简单修改配置文件即可切换。

在Spring AI 基础上，进一步拓展了支持的模型。

## 待完成
- [x] 流式响应到客户端无token消耗信息，qwen 已修复， **但** openai 客户但的源码修改不到，只能等后续更新了
- [] Flux 提示：在非阻塞上下文中使用阻塞调用可能会导致线程匮乏
- [] 专为 openRouter 创建进行配置的属性类，毕竟有些地方与 openai 的配置不同
- [] 函数调用部分的代码qwen的可能有问题，因为没有指定哪些函数可以调用就默认调用了

## 表设计
### 对话表 (dialogs): 存储对话的基本信息。

- dialog_id (主键, 自增): 对话唯一标识。
- user_id (外键): 发起对话的用户ID。
- model : 大语言模型名
- service_provider：模型提供商
- start_time: 对话开始时间。
- dialogs_summary：对话总结
- history_strategy_id (外键，可空): 关联到历史策略表的历史策略ID。
- is_busy：表上一条对话未处理完毕，不接受新的消息
- functions (json): 大语言模型进行函数调用，开启的函数名称，json格式，可为空。
- agent_id (外键，可空): 关联到agents表的agents_id。


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
- close (bool)：关闭策略，全局、用户、会话策略全都不对此会话生效，所有消息（排除总结类的）按照时间顺序原始传递。
- token_size (长整数)：限制传递的token数量，可为空，注意不包括用户新发送的这条消息
- message_size (长整数)：限制传递的消息条数，可为空，注意不包括用户新发送的这条消息
- summary_rule (枚举)：摘要生成策略，可为空。为空时不生效，可选：关键字保留、对话总结 两种策略

### user_ai_config (用户AI配置): 用户AI部分配置的详细信息。
- id (主键, 自增): 表唯一标识。
- user_id： 关联到用户表的用户ID。
- history_strategy_id (外键): 关联到历史策略表的历史策略ID，作为默认历史传递策略。
- dialog_id (外键): 关联到对话表的对话ID，作为默认对话属性。

### prompt_templates (prompt模板): 存储提示词模板的详细信息。
- template_id (主键, 自增): 模板唯一标识。
- description：对此模板的描述。
- labels：标签，可用于分类。如问候、帮助类型等
- template_text：提示词内容
- example：对话示例，这里要存储固定结构的文本
- create_at：模板创建时间
- update_at：模板最后更新时间

### agents (智能体配置表)：配置智能体，智能体可以理解为AI助手。
- agent_id (主键, 自增): 智能体唯一标识。
- user_id (外键)： 关联到用户表的用户ID。
- avatar：智能体头像。
- name：智能体名称。
- description：智能体介绍
- prompt_template_id (外键): 关联到prompt_templates表的模板ID，用于发送消息时自动应用模板
- history_strategy_id (外键)：关联到history_strategy表的ID，用于设定默认的智能体历史消息处理策略
- playbook：智能体的设定
- labels：智能体的标签，可用于分类，使用逗号分离字符
- model：智能体使用的AI模型
- service_provider：智能体使用的AI模型提供商
- temperature：智能体使用的AI模型随机性
- top_p：智能体使用的AI模型采样率
- functions (json): 大语言模型进行函数调用，开启的函数名称，json格式，可为空。
- is_deleted：是否删除，0表示未删除，1表示已删除
- is_public：是否公开，0表示不公开，1表示公开
- create_at ：创建时间
- update_at：更新时间

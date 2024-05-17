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


### 消息表 (messages): 存储每条消息的详细信息。

- message_id (主键, 自增): 消息唯一标识。
- dialog_id (外键): 关联到对话表的对话ID。
- sequence：消息顺序
- content: 消息内容。
- timestamp: 消息发送时间。
- is_from_ai (布尔值): 标记消息是否来自大语言模型，若不是肯定就是来自用户的消息
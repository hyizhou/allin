# 设计文档

## 网络

### 记录当前网速
将记录每一秒的网速情况，这主要用于让前端页面使用表格展示一段时间内的网速情况。

由于不需要持久化存储，且需要滚动更新，因此选择由 redis 来存储这部分数据。

### 统计网络流量

将每小时、每天，不同网络接口所用流量记录在数据表。

- 每分钟网络流量直接存放在Redis或缓存中
- 统计每月、每周或任意时间长度的流量，可根据这两个表进行统计

#### 统一每小时的流量
| 列名              | 数据类型           | 描述                       |
|-------------------|--------------------|----------------------------|
| id                | INT                | 主键，自动递增             |
| interface_name    | VARCHAR(100)       | 接口名称                   |
| period_start      | TIME               | 统计起始时间               |
| bytes_received    | BIGINT UNSIGNED    | 接收字节数                 |
| bytes_sent        | BIGINT UNSIGNED    | 发送字节数                 |
| created_at        | TIMESTAMP          | 记录创建时间戳             |
| hour              | DATETIME             | 统计小时                   |

主键: PRIMARY KEY (id)
唯一键: UNIQUE KEY netconthourly_unique (hour)

表名为：`netconthourly`


#### 统计每天流量

| 列名              | 数据类型           | 描述                  |
|-------------------|--------------------|---------------------|
| id                | INT                | 主键，自动递增             |
| interface_name    | VARCHAR(100)       | 接口名称                |
| period_start      | TIME               | 统计起始时间，可能会有未统计整天的情况 |
| bytes_received    | BIGINT UNSIGNED    | 接收字节数               |
| bytes_sent        | BIGINT UNSIGNED    | 发送字节数               |
| created_at        | TIMESTAMP          | 记录创建时间戳             |
| date              | DATE               | 统计日期                |

主键: PRIMARY KEY (id)
唯一键: UNIQUE KEY netcontdaily_unique (date)

表名为：`netcontdaily`

## 待办
- redis 存储数据的话，每个key要根据设备不同添加标识，不然数据都混在一起了。
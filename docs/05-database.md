# 数据库设计文档

## 1. 数据库概述

本项目使用 MySQL 8.0 作为数据库，采用 MyBatis 作为 ORM 框架。数据库设计遵循 GB/T 28181 标准中的设备信息规范，所有表名以 `docking_` 为前缀。

### 1.1 数据库连接配置

| 配置项 | 值 |
|-------|-----|
| 数据库 | gb28181_docking_platform |
| 字符集 | UTF-8 |
| 排序规则 | utf8mb4_general_ci |
| 引擎 | InnoDB |

### 1.2 MyBatis Generator 配置

项目使用 MyBatis Generator 自动生成实体类和 Mapper 接口，配置文件位于 `orm/src/main/resources/config/generatorConfig.xml`。

**生成策略**:
- 表名匹配规则: `docking_%`
- 生成模式: 支持两种
  1. MyBatis3Simple: 简单 CRUD
  2. MyBatis3DynamicSql: 动态 SQL

---

## 2. 数据表设计

### 2.1 设备表 (docking_device)

**表描述**: 存储注册到平台的设备信息

| 字段名 | 类型 | 是否为空 | 默认值 | 说明 |
|-------|------|---------|-------|------|
| id | BIGINT | NOT NULL | AUTO_INCREMENT | 主键 ID |
| device_id | VARCHAR(20) | NOT NULL | - | 设备国标编码 (20位) |
| name | VARCHAR(255) | NULL | - | 设备名称 |
| manufacturer | VARCHAR(255) | NULL | - | 设备厂商 |
| model | VARCHAR(255) | NULL | - | 设备型号 |
| firmware | VARCHAR(255) | NULL | - | 固件版本 |
| transport | VARCHAR(10) | NULL | 'UDP' | 传输协议 (UDP/TCP) |
| stream_mode | VARCHAR(10) | NULL | 'UDP' | 流传输模式 |
| online | TINYINT | NULL | 0 | 在线状态 (0:离线, 1:在线) |
| register_time | DATETIME | NULL | - | 注册时间 |
| keepalive_time | DATETIME | NULL | - | 最后心跳时间 |
| ip | VARCHAR(50) | NULL | - | 设备 IP 地址 |
| create_time | DATETIME | NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | NULL | CURRENT_TIMESTAMP | 更新时间 |
| port | INT | NULL | - | 设备端口 |
| expires | INT | NULL | 3600 | 注册有效期 (秒) |
| subscribe_cycle_for_catalog | INT | NULL | - | 目录订阅周期 |
| subscribe_cycle_for_mobile_position | INT | NULL | - | 移动位置订阅周期 |
| mobile_position_submission_interval | INT | NULL | - | 移动位置提交间隔 |
| subscribe_cycle_for_alarm | INT | NULL | - | 报警订阅周期 |
| host_address | VARCHAR(255) | NULL | - | 主机地址 |
| charset | VARCHAR(20) | NULL | 'GB2312' | 字符编码 |
| ssrc_check | TINYINT | NULL | 0 | SSRC 校验 (0:禁用, 1:启用) |
| geo_coord_sys | VARCHAR(20) | NULL | - | 地理坐标系 |
| media_server_id | VARCHAR(50) | NULL | - | 媒体服务器 ID |
| custom_name | VARCHAR(255) | NULL | - | 自定义名称 |
| sdp_ip | VARCHAR(50) | NULL | - | SDP IP 地址 |
| local_ip | VARCHAR(50) | NULL | - | 本地 IP 地址 |
| password | VARCHAR(255) | NULL | - | 设备密码 |
| as_message_channel | TINYINT | NULL | 0 | 作为消息通道 (0:否, 1:是) |
| keepalive_interval_time | INT | NULL | - | 心跳间隔时间 (秒) |
| switch_primary_sub_stream | TINYINT | NULL | 0 | 切换主子码流 (0:否, 1:是) |
| broadcast_push_after_ack | TINYINT | NULL | 0 | ACK 后广播推送 (0:否, 1:是) |

**索引设计**:
```sql
PRIMARY KEY (id)
UNIQUE KEY uk_device_id (device_id)
KEY idx_online (online)
KEY idx_keepalive_time (keepalive_time)
```

---

### 2.2 设备通道表 (docking_device_channel)

**表描述**: 存储设备的通道信息

| 字段名 | 类型 | 是否为空 | 默认值 | 说明 |
|-------|------|---------|-------|------|
| id | BIGINT | NOT NULL | AUTO_INCREMENT | 主键 ID |
| gb_device_id | VARCHAR(20) | NOT NULL | - | 国标设备 ID |
| gb_device_channel_id | VARCHAR(20) | NOT NULL | - | 国标设备通道 ID |
| name | VARCHAR(255) | NULL | - | 通道名称 |
| address | VARCHAR(255) | NULL | - | 地址 |

**索引设计**:
```sql
PRIMARY KEY (id)
KEY idx_gb_device_id (gb_device_id)
```

---


## 3. 建表脚本

### 3.1 完整建表 SQL

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS gb28181_docking_platform 
    DEFAULT CHARACTER SET utf8mb4 
    DEFAULT COLLATE utf8mb4_general_ci;

USE gb28181_docking_platform;

-- 设备表
CREATE TABLE `docking_device` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `device_id` VARCHAR(20) NOT NULL COMMENT '设备国标编码',
    `name` VARCHAR(255) NULL COMMENT '设备名称',
    `manufacturer` VARCHAR(255) NULL COMMENT '设备厂商',
    `model` VARCHAR(255) NULL COMMENT '设备型号',
    `firmware` VARCHAR(255) NULL COMMENT '固件版本',
    `transport` VARCHAR(10) NULL DEFAULT 'UDP' COMMENT '传输协议',
    `stream_mode` VARCHAR(10) NULL DEFAULT 'UDP' COMMENT '流传输模式',
    `online` TINYINT NULL DEFAULT 0 COMMENT '在线状态',
    `register_time` DATETIME NULL COMMENT '注册时间',
    `keepalive_time` DATETIME NULL COMMENT '最后心跳时间',
    `ip` VARCHAR(50) NULL COMMENT '设备IP地址',
    `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `port` INT NULL COMMENT '设备端口',
    `expires` INT NULL DEFAULT 3600 COMMENT '注册有效期(秒)',
    `subscribe_cycle_for_catalog` INT NULL COMMENT '目录订阅周期',
    `subscribe_cycle_for_mobile_position` INT NULL COMMENT '移动位置订阅周期',
    `mobile_position_submission_interval` INT NULL COMMENT '移动位置提交间隔',
    `subscribe_cycle_for_alarm` INT NULL COMMENT '报警订阅周期',
    `host_address` VARCHAR(255) NULL COMMENT '主机地址',
    `charset` VARCHAR(20) NULL DEFAULT 'GB2312' COMMENT '字符编码',
    `ssrc_check` TINYINT NULL DEFAULT 0 COMMENT 'SSRC校验',
    `geo_coord_sys` VARCHAR(20) NULL COMMENT '地理坐标系',
    `media_server_id` VARCHAR(50) NULL COMMENT '媒体服务器ID',
    `custom_name` VARCHAR(255) NULL COMMENT '自定义名称',
    `sdp_ip` VARCHAR(50) NULL COMMENT 'SDP IP地址',
    `local_ip` VARCHAR(50) NULL COMMENT '本地IP地址',
    `password` VARCHAR(255) NULL COMMENT '设备密码',
    `as_message_channel` TINYINT NULL DEFAULT 0 COMMENT '作为消息通道',
    `keepalive_interval_time` INT NULL COMMENT '心跳间隔时间(秒)',
    `switch_primary_sub_stream` TINYINT NULL DEFAULT 0 COMMENT '切换主子码流',
    `broadcast_push_after_ack` TINYINT NULL DEFAULT 0 COMMENT 'ACK后广播推送',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_device_id` (`device_id`),
    KEY `idx_online` (`online`),
    KEY `idx_keepalive_time` (`keepalive_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备信息表';

-- 设备通道表
CREATE TABLE `docking_device_channel` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `gb_device_id` VARCHAR(20) NOT NULL COMMENT '国标设备ID',
    `gb_device_channel_id` VARCHAR(20) NOT NULL COMMENT '国标设备通道ID',
    `name` VARCHAR(255) NULL COMMENT '通道名称',
    `address` VARCHAR(255) NULL COMMENT '地址',
    PRIMARY KEY (`id`),
    KEY `idx_gb_device_id` (`gb_device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备通道表';

```

---

## 4. MyBatis 映射配置

### 4.1 实体类位置

根据 MyBatis Generator 配置，生成的实体类位于以下包:

- **基础 Mapper**: `cn.skcks.docking.gb28181.orm.mybatis.basic.model`
- **动态 SQL Mapper**: `cn.skcks.docking.gb28181.orm.mybatis.dynamic.model`

### 4.2 Mapper 接口位置

- **基础 Mapper**: `cn.skcks.docking.gb28181.orm.mybatis.basic.mapper`
- **动态 SQL Mapper**: `cn.skcks.docking.gb28181.orm.mybatis.dynamic.mapper`

### 4.3 生成命令

```bash
# 在 orm 模块目录下执行
mvn mybatis-generator:generate
```

---

## 5. 数据字典

### 5.1 在线状态 (online/status)

| 值 | 说明 |
|----|------|
| 0 | 离线 |
| 1 | 在线 |

### 5.2 传输协议 (transport)

| 值 | 说明 |
|----|------|
| UDP | UDP 传输 |
| TCP | TCP 传输 |

### 5.3 流传输模式 (stream_mode)

| 值 | 说明 |
|----|------|
| UDP | UDP 传输 |
| TCP-ACTIVE | TCP 主动 |
| TCP-PASSIVE | TCP 被动 |

### 5.4 云台类型 (ptz_type)

| 值 | 说明 |
|----|------|
| 0 | 未知 |
| 1 | 球机 |
| 2 | 半球 |
| 3 | 固定摄像机 |
| 4 | 遥控摄像机 |

### 5.5 注册方式 (register_way)

| 值 | 说明 |
|----|------|
| 1 | 主动注册 |
| 2 | 被动注册 |

---

## 6. 数据库优化建议

### 6.1 索引优化

1. **设备表**:
   - 按 `device_id` 查询频繁，建立唯一索引
   - 按 `online` 和 `keepalive_time` 查询心跳超时设备，建立组合索引

2. **通道表**:
   - 按 `gb_device_id` 查询通道列表，建立普通索引
### 6.2 分表策略

当设备数量达到百万级别时，建议按以下策略分表:

1. **按地域分表**: 根据行政区划代码前缀分表
2. **按时间分表**: 历史数据按月/年归档

### 6.3 缓存策略

1. **设备信息缓存**: 使用 Redis 缓存设备基本信息
2. **通道列表缓存**: 缓存设备的通道列表，设置合理的过期时间
3. **缓存更新策略**: 设备心跳更新时刷新缓存

---

## 7. 数据库连接配置

### 7.1 application.yml 配置

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/gb28181_docking_platform?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    
mybatis:
  mapper-locations: classpath:**/mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl

pagehelper:
  helper-dialect: mysql
  reasonable: true
  support-methods-arguments: true
  params: count=countSql
```

### 7.2 连接池配置

建议使用 HikariCP 连接池 (Spring Boot 默认):

```yaml
spring:
  datasource:
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20
      idle-timeout: 30000
      pool-name: GB28181HikariCP
      max-lifetime: 1800000
      connection-timeout: 30000
```

---

## 8. 数据迁移与备份

### 8.1 数据备份

```bash
# 全库备份
mysqldump -u root -p gb28181_docking_platform > backup_$(date +%Y%m%d).sql

# 仅备份结构
mysqldump -u root -p --no-data gb28181_docking_platform > schema.sql
```

### 8.2 数据恢复

```bash
mysql -u root -p gb28181_docking_platform < backup_20240101.sql
```

---

## 9. 注意事项

1. **字符集**: 统一使用 `utf8mb4` 以支持完整 Unicode 字符
2. **时区**: 数据库和 JVM 时区需保持一致
3. **事务隔离**: 默认使用 REPEATABLE-READ，可根据需求调整
4. **连接超时**: 根据实际网络环境调整连接超时参数
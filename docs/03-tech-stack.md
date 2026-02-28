# 技术栈详解

## 1. 技术栈概览

本项目基于现代 Java 技术栈构建，采用 Spring Boot 3.x 作为核心框架，结合多种成熟的开源组件，实现高性能、可扩展的视频监控对接平台。

### 1.1 技术栈全景图

```
┌─────────────────────────────────────────────────────────────────────────┐
│                            应用层 (Application)                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐│
│  │ Spring Boot │  │ Spring MVC  │  │ Spring Cloud│  │  Spring Doc     ││
│  │   3.1.2     │  │   6.0.x     │  │  OpenFeign  │  │  OpenAPI 3.0    ││
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────────┘│
├─────────────────────────────────────────────────────────────────────────┤
│                            业务层 (Business)                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐│
│  │  Lombok     │  │  MapStruct  │  │   Hutool    │  │  Commons-Lang3  ││
│  │  1.18.24    │  │  1.5.3.Final│  │   5.8.22    │  │     3.x         ││
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────────┘│
├─────────────────────────────────────────────────────────────────────────┤
│                           协议层 (Protocol)                              │
│  ┌─────────────────────────────┐  ┌─────────────────────────────────────┐│
│  │      JAIN-SIP RI 1.3.0-91   │  │     Jackson (JSON/XML)              ││
│  │      (SIP 协议栈)           │  │     (序列化框架)                    ││
│  └─────────────────────────────┘  └─────────────────────────────────────┘│
├─────────────────────────────────────────────────────────────────────────┤
│                           数据层 (Data)                                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐│
│  │  MyBatis    │  │ MyBatis     │  │  PageHelper │  │  MySQL 8.0      ││
│  │  3.0.2      │  │ Dynamic SQL │  │   1.4.7     │  │                 ││
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────────┘│
├─────────────────────────────────────────────────────────────────────────┤
│                          缓存层 (Cache)                                  │
│  ┌─────────────────────────────┐  ┌─────────────────────────────────────┐│
│  │     Spring Data Redis       │  │            Jedis                   ││
│  │                             │  │         (Redis 客户端)              ││
│  └─────────────────────────────┘  └─────────────────────────────────────┘│
├─────────────────────────────────────────────────────────────────────────┤
│                          基础设施 (Infrastructure)                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐│
│  │   Java 17   │  │   Maven     │  │  Undertow   │  │    Docker       ││
│  │   (LTS)     │  │   3.x       │  │  (Web容器)  │  │                 ││
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────────┘│
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 2. 核心框架

### 2.1 Java 17 (LTS)

**选择理由**:
- 长期支持版本 (Long Term Support)，稳定性有保障
- 引入 Records、Sealed Classes、Pattern Matching 等新特性
- 性能优化，包括 G1GC 改进、ZGC 增强
- 更好的空指针异常信息

**主要使用特性**:
- Records: 用于不可变数据载体 (DTO)
- Text Blocks: 多行字符串处理
- Pattern Matching: 类型匹配简化
- Switch Expressions: 增强的 switch 语法

### 2.2 Spring Boot 3.1.2

**核心特性**:
- 基于 Spring Framework 6.0.x
- 支持 Jakarta EE 10 (从 javax 迁移到 jakarta)
- 原生编译支持 (GraalVM)
- 可观测性增强 (Micrometer Tracing)

**项目使用**:
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.1.2</version>
</parent>
```

**主要依赖**:
- spring-boot-starter-web: Web 开发
- spring-boot-starter-undertow: Web 容器 (替代 Tomcat)
- spring-boot-configuration-processor: 配置元数据处理
- spring-boot-devtools: 开发工具

### 2.3 Spring Cloud 2022.0.3

**使用组件**:
- **Spring Cloud OpenFeign**: 声明式 HTTP 客户端

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

**使用场景**: 
- 调用 ZLMediaKit 流媒体服务器 HTTP API

---

## 3. Web 层技术

### 3.1 Spring MVC 6.0.x

**特点**:
- 基于函数式端点的路由
- 响应式编程支持
- CORS 配置增强

### 3.2 Undertow (Web 容器)

**选择理由**:
- 轻量级高性能 Web 容器
- 非阻塞 I/O (NIO)
- 内存占用更低
- 适合高并发场景

**配置**:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```

**排除 Tomcat**:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### 3.3 SpringDoc OpenAPI 2.2.0

**功能**:
- 自动生成 OpenAPI 3.0 规范文档
- 提供 Swagger UI 界面
- 支持注解驱动的 API 文档

**依赖**:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

**访问地址**:
- Swagger UI: `http://localhost:28181/swagger-ui.html`
- OpenAPI JSON: `http://localhost:28181/v3/api-docs`

---

## 4. 数据层技术

### 4.1 MyBatis 3.0.2

**特点**:
- Spring Boot 3.x 完全兼容
- 支持 Kotlin 协程
- 批量操作性能优化

**依赖**:
```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>3.0.2</version>
</dependency>
```

### 4.2 MyBatis Dynamic SQL 1.5.0

**功能**:
- 类型安全的动态 SQL 构建
- 编译时检查
- 流式 API 风格

**示例**:
```java
SelectStatementProvider selectStatement = SqlBuilder.select(id, name, email)
    .from(userTable)
    .where(id, isEqualTo(1))
    .build()
    .render(RenderingStrategies.MYBATIS3);
```

### 4.3 PageHelper 1.4.7

**功能**:
- 物理分页插件
- 支持多种数据库
- 自动计算分页参数

**使用方式**:
```java
PageHelper.startPage(pageNum, pageSize);
List<User> users = userMapper.selectAll();
PageInfo<User> pageInfo = new PageInfo<>(users);
```

### 4.4 MySQL 8.0

**特点**:
- 性能大幅提升
- 支持 JSON 数据类型
- 窗口函数支持
- CTE (Common Table Expressions)

**驱动**:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## 5. 缓存层技术

### 5.1 Spring Data Redis

**特点**:
- 统一的 Redis 操作抽象
- 支持多种序列化方式
- 与 Spring Cache 无缝集成

### 5.2 Jedis

**选择理由**:
- 同步阻塞式客户端，使用简单
- 社区活跃，文档丰富
- 性能满足大多数场景

**配置**:
```xml
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
    <exclusions>
        <exclusion>
            <artifactId>lettuce-core</artifactId>
            <groupId>io.lettuce</groupId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
```

---

## 6. SIP 协议栈

### 6.1 JAIN-SIP RI 1.3.0-91

**简介**:
JAIN-SIP (Java API for Integrated Networks - SIP) 是 Java 平台的 SIP 协议标准实现。

**特点**:
- 完整的 SIP 协议实现
- 支持 TCP/UDP 传输
- 支持 SIP 摘要认证
- 支持 SDP 协商

**依赖**:
```xml
<dependency>
    <groupId>javax.sip</groupId>
    <artifactId>jain-sip-ri</artifactId>
    <version>1.3.0-91</version>
</dependency>
```

**GB28181 使用**:
- 设备注册
- 目录查询
- 实时视频邀请
- 云台控制
- 录像查询

---

## 7. 工具库

### 7.1 Hutool 5.8.22

**简介**: 一个小而全的 Java 工具类库

**使用模块**:
| 模块 | 功能 |
|------|------|
| hutool-core | 核心工具，字符串、日期、随机数等 |
| hutool-crypto | 加密解密，MD5、AES、RSA 等 |
| hutool-json | JSON 解析 |

**使用示例**:
```java
// 随机 ID
NanoId.randomNanoId(null, "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray(), 32);

// JSON 转换
JSONUtil.toJsonStr(object);
```

### 7.2 Lombok 1.18.24

**功能**: 通过注解简化 Java 代码

**常用注解**:
| 注解 | 功能 |
|------|------|
| @Data | 生成 getter/setter/toString/equals/hashCode |
| @Builder | 构建器模式 |
| @AllArgsConstructor | 全参构造 |
| @NoArgsConstructor | 无参构造 |
| @Slf4j | 日志对象 |
| @SneakyThrows | 异常包装 |
| @RequiredArgsConstructor | 必需参数构造 (final 字段) |

### 7.3 MapStruct 1.5.3.Final

**功能**: 类型安全的对象映射框架

**特点**:
- 编译时生成代码，无运行时开销
- 类型安全
- 支持嵌套映射

**配置**:
```xml
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.3.Final</version>
</dependency>
```

**注解处理器配置**:
```xml
<annotationProcessorPaths>
    <path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>1.5.3.Final</version>
    </path>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.24</version>
    </path>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok-mapstruct-binding</artifactId>
        <version>0.2.0</version>
    </path>
</annotationProcessorPaths>
```

### 7.4 Apache Commons Lang3

**功能**: 通用工具类库

**主要使用**:
- `StringUtils`: 字符串处理
- `ObjectUtils`: 对象处理
- `RandomStringUtils`: 随机字符串

---

## 8. 序列化框架

### 8.1 Jackson

**特点**:
- Spring Boot 默认 JSON 序列化框架
- 高性能
- 丰富的注解支持

**使用模块**:
| 模块 | 功能 |
|------|------|
| jackson-core | 核心模块 |
| jackson-databind | 数据绑定 |
| jackson-annotations | 注解支持 |
| jackson-dataformat-xml | XML 序列化 |

**XML 序列化配置**:
```java
XmlMapper mapper = new XmlMapper();
mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
mapper.setPropertyNamingStrategy(new PropertyNamingStrategies.UpperCamelCaseStrategy());
```

---

## 9. 构建与部署

### 9.1 Maven

**版本要求**: Maven 3.6+

**主要插件**:

| 插件 | 功能 |
|------|------|
| maven-compiler-plugin | Java 编译 |
| spring-boot-maven-plugin | Spring Boot 打包 |
| maven-surefire-plugin | 测试执行 |
| maven-resources-plugin | 资源处理 |
| dockerfile-maven-plugin | Docker 镜像构建 |
| central-publishing-maven-plugin | 发布到中央仓库 |

**编译配置**:
```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <maven.compiler.encoding>UTF-8</maven.compiler.encoding>
</properties>
```

### 9.2 Docker 支持

**构建镜像**:
```bash
mvn clean package -Pdocker
```

**运行容器**:
```bash
docker run --name gb28181 --rm \
  -v /path/to/application.yml:/opt/gb28181-docking-platform/application.yml \
  skcks.cn/gb28181-docking-platform:0.1.0
```

---

## 10. 技术选型总结

### 10.1 选型原则

| 原则 | 说明 |
|------|------|
| 成熟稳定 | 选择经过大规模生产验证的技术 |
| 社区活跃 | 社区活跃，问题易解决 |
| 性能优先 | 在稳定前提下选择高性能方案 |
| 易于维护 | 技术栈统一，降低维护成本 |

### 10.2 技术栈版本一览

| 技术 | 版本 | 类型 |
|------|------|------|
| Java | 17 | LTS |
| Spring Boot | 3.1.2 | 核心框架 |
| Spring Cloud | 2022.0.3 | 微服务 |
| MyBatis | 3.0.2 | ORM |
| MySQL | 8.0 | 数据库 |
| Redis | - | 缓存 |
| JAIN-SIP | 1.3.0-91 | SIP 协议 |
| Hutool | 5.8.22 | 工具库 |
| Lombok | 1.18.24 | 代码简化 |
| MapStruct | 1.5.3.Final | 对象映射 |
| SpringDoc | 2.2.0 | API 文档 |

### 10.3 替代方案参考

| 当前技术 | 可替代方案 | 替代场景 |
|----------|-----------|---------|
| Undertow | Tomcat | 需要更广泛兼容性时 |
| Jedis | Lettuce | 需要异步 Redis 操作时 |
| MyBatis | JPA/Hibernate | 需要更抽象的 ORM 时 |
| MySQL | PostgreSQL | 需要更强的事务特性时 |
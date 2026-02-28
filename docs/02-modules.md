# 模块架构详解

## 1. 模块总览

本项目采用 Maven 多模块架构设计，遵循"高内聚、低耦合"的设计原则。整个项目由 8 个模块组成，每个模块职责明确、边界清晰。

### 1.1 模块清单

| 模块名称 | ArtifactId | 职责描述 |
|---------|-----------|---------|
| 根模块 | gb28181 | 父 POM，管理依赖版本和公共配置 |
| 启动模块 | starter | Spring Boot 应用入口，整合所有模块 |
| 注解模块 | annotation | 自定义 Web 注解，简化 Controller 开发 |
| 公共模块 | common | 通用工具类、响应封装、Redis 工具等 |
| 数据层模块 | orm | MyBatis 数据持久层，数据库访问 |
| SIP 协议模块 | gb28181-sip | GB28181 SIP 信令处理 |
| 流媒体模块 | zlmediakit-service | ZLMediaKit 流媒体服务封装 |
| 业务服务模块 | gb28181-service | 核心业务逻辑实现 |
| API 模块 | api | RESTful API 控制器层 |

### 1.2 模块依赖关系图

```
                              ┌─────────────┐
                              │   starter   │
                              │  (启动模块)  │
                              └──────┬──────┘
                                     │
                      ┌──────────────┼──────────────┐
                      │              │              │
                      ▼              ▼              ▼
               ┌──────────┐  ┌─────────────┐  ┌─────────┐
               │   api    │  │gb28181-svc  │  │  common │
               │ (接口层)  │  │  (业务层)   │  │ (公共)  │
               └────┬─────┘  └──────┬──────┘  └────┬────┘
                    │               │              │
                    │      ┌────────┼────────┐     │
                    │      │        │        │     │
                    │      ▼        ▼        ▼     │
                    │  ┌───────┐ ┌─────┐ ┌────────┐│
                    │  │  orm  │ │ sip │ │zlm-svc ││
                    │  │(数据) │ │(SIP)│ │(流媒体)││
                    │  └───┬───┘ └──┬──┘ └───┬────┘│
                    │      │        │        │     │
                    └──────┴────────┴────────┴─────┘
                                     │
                              ┌──────┴──────┐
                              │ annotation  │
                              │  (注解层)   │
                              └─────────────┘
```

## 2. 核心模块详解

### 2.1 starter (启动模块)

**职责**: 作为 Spring Boot 应用的入口模块，负责整合所有业务模块并启动应用。

**特点**:
- 包含 `Gb28181DockingPlatformApplication` 主启动类
- 打包为可执行 JAR 文件
- 包含应用配置文件 `application.yml`
- 默认端口 28181

**依赖模块**:
- api (API 层)
- gb28181-service (业务服务层)
- common (公共工具层)

**关键文件**:
```
starter/
├── pom.xml                          # Maven 配置
└── src/main/
    ├── java/
    │   └── cn/skcks/docking/gb28181/
    │       └── Gb28181DockingPlatformApplication.java  # 启动类
    └── resources/
        ├── application.yml          # 应用配置
        └── logback.xml              # 日志配置
```

**启动类代码**:
```java
@EnableFeignClients
@SpringBootApplication
public class Gb28181DockingPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(Gb28181DockingPlatformApplication.class, args);
    }
}
```

---

### 2.2 annotation (注解模块)

**职责**: 提供自定义 Web 注解，简化 RESTful API 开发。

**特点**:
- 轻量级模块，无业务逻辑
- 提供组合注解，减少 Controller 层样板代码
- 基于 Spring MVC 注解扩展

**主要注解**:

| 注解 | 说明 | 示例 |
|------|------|------|
| `@JsonMapping` | 组合注解，自动设置 JSON 响应类型 | `@JsonMapping("/api/device")` |
| `@GetJson` | GET 请求 + JSON 响应 | `@GetJson("/list")` |
| `@PostJson` | POST 请求 + JSON 响应 | `@PostJson("/save")` |
| `@PutJson` | PUT 请求 + JSON 响应 | `@PutJson("/update")` |
| `@DeleteJson` | DELETE 请求 + JSON 响应 | `@DeleteJson("/{id}")` |
| `@PatchJson` | PATCH 请求 + JSON 响应 | `@PatchJson("/status")` |

**依赖**:
- spring-beans
- spring-context
- spring-web
- jackson-annotations

**使用示例**:
```java
@JsonMapping("/api/device/play")
@RestController
@RequiredArgsConstructor
public class PlayController {
    
    @GetJson("/realTimePlay")
    public DeferredResult<JsonResponse<String>> realTimePlay(RealTimePlayDTO dto) {
        return playService.realTimePlay(dto.getDeviceId(), dto.getChannelId(), dto.getTimeout());
    }
}
```

---

### 2.3 common (公共模块)

**职责**: 提供通用工具类、响应封装、分页处理、Redis 工具等基础设施。

**核心组件**:

#### 2.3.1 JSON 响应封装

```java
// JsonResponse - 统一响应结构
public class JsonResponse<T> {
    private int code;       // 状态码
    private T data;         // 响应数据
    private String msg;     // 响应消息
    
    // 静态工厂方法
    public static <T> JsonResponse<T> success(T data);
    public static <T> JsonResponse<T> error(String message);
}
```

#### 2.3.2 Redis 工具类

提供完整的 Redis 操作封装:
- **KeyOps**: Key 操作 (删除、过期、重命名等)
- **StringOps**: String 类型操作
- **HashOps**: Hash 类型操作
- **ListOps**: List 类型操作
- **SetOps**: Set 类型操作
- **ZSetOps**: 有序集合操作

#### 2.3.3 分页工具

```java
// PageWrapper - 分页结果封装
public class PageWrapper<T> {
    private List<T> list;      // 数据列表
    private long total;        // 总记录数
    private int pageNum;       // 当前页
    private int pageSize;      // 每页大小
    private int pages;         // 总页数
}
```

#### 2.3.4 其他工具类

| 工具类 | 功能 |
|--------|------|
| `JsonUtils` | JSON 序列化/反序列化工具 |
| `XmlUtils` | XML 处理工具 |
| `ProjectConfig` | 项目配置信息 |

**依赖**:
- hutool-all (工具库)
- jackson (JSON/XML 处理)
- pagehelper-spring-boot-starter (分页插件)
- spring-data-redis + jedis (Redis 操作)
- springdoc-openapi (API 文档)

---

### 2.4 orm (数据持久层模块)

**职责**: 负责数据库访问，使用 MyBatis 作为 ORM 框架。

**特点**:
- 使用 MyBatis 3.0.2 + MyBatis Dynamic SQL
- 集成 PageHelper 分页插件
- 支持代码生成器自动生成 Mapper

**包结构**:
```
orm/
├── pom.xml
└── src/main/
    ├── java/
    │   └── cn/skcks/docking/gb28181/orm/mybatis/
    │       ├── basic/          # 基础 Mapper (简单 CRUD)
    │       │   ├── mapper/
    │       │   └── model/
    │       └── dynamic/        # 动态 SQL Mapper
    │           ├── mapper/
    │           └── model/
    └── resources/
        └── config/
            └── generatorConfig.xml  # MyBatis Generator 配置
```

**MyBatis Generator 配置**:

项目配置了两个生成上下文:
1. **mysql**: 生成简单 CRUD 的 Mapper (MyBatis3Simple)
2. **dynamic-mysql**: 生成动态 SQL Mapper (MyBatis3DynamicSql)

**表命名规则**: 所有表以 `docking_` 为前缀，如 `docking_device_channel`

**依赖**:
- mybatis-spring-boot-starter
- mybatis-dynamic-sql
- pagehelper-spring-boot-starter
- mysql-connector-j

---

### 2.5 gb28181-sip (SIP 协议模块)

**职责**: 处理 GB28181 SIP 信令协议，包括消息解析、构建和工具方法。

**特点**:
- 基于 JAIN-SIP 1.3.0-91 协议栈
- 支持 GB28181 消息编解码
- 提供 SIP 工具方法

**核心工具类**:

#### 2.5.1 SipUtil

提供 SIP 消息处理的通用工具方法:

```java
public class SipUtil {
    // 生成唯一 ID
    public static String nanoId(int size);
    
    // 从 From 头获取设备 ID
    public static String getIdFromFromHeader(FromHeader fromHeader);
    
    // 生成序号
    public static String generateSn();
    
    // 从请求获取通道 ID
    public static String getChannelIdFromRequest(Request request);
    
    // 生成 Via 标签
    public static String generateViaTag();
    
    // 生成 User-Agent 头
    public static UserAgentHeader getUserAgentHeader();
    
    // 云台指令码计算
    public static String cmdString(int leftRight, int upDown, int inOut, 
                                   int moveSpeed, int zoomSpeed);
    
    // 获取远程地址信息
    public static RemoteInfo getRemoteInfoFromRequest(SIPRequest request, 
                                                       boolean sipUseSourceIpAsRemoteAddress);
}
```

#### 2.5.2 MANSCDPUtils

GB28181 MANSCDP (监控报警联网系统控制描述协议) XML 消息处理工具:

```java
public class MANSCDPUtils {
    // 对象转 XML
    public static String toXml(Object obj);
    public static byte[] toByteXml(Object obj, String charset);
    
    // XML 解析为对象
    public static <T> T parse(String xml, Class<T> clazz);
    public static <T> T parse(byte[] xml, Class<T> clazz);
    
    // 对象转换
    public static <T> T convert(Object object, Class<T> clazz);
}
```

#### 2.5.3 DigestAuthenticationHelper

SIP 摘要认证助手，处理 SIP 认证相关逻辑。

**依赖**:
- jain-sip-ri (SIP 协议栈)
- hutool-core (工具方法)
- hutool-crypto (加密工具)
- jackson (JSON/XML 处理)
- commons-lang3 (字符串工具)

---

### 2.6 zlmediakit-service (流媒体服务模块)

**职责**: 封装 ZLMediaKit 流媒体服务器 API，提供视频流转发、录像、截图等功能。

**特点**:
- 使用 Spring Cloud OpenFeign 调用 ZLMediaKit HTTP API
- 支持 HTTPS 忽略 SSL 证书
- 完整封装 ZLMediaKit API

**核心组件**:

#### 2.6.1 ZlmMediaService

流媒体服务门面类，提供以下功能:

| 功能分类 | 方法 | 说明 |
|---------|------|------|
| **API 管理** | getApiList() | 获取 API 列表 |
| **配置管理** | getServerConfig() / setServerConfig() | 获取/设置服务器配置 |
| **RTP 服务** | openRtpServer() / closeRtpServer() | 开启/关闭 RTP 服务 |
| **推流** | startSendRtp() / stopSendRtp() | 开始/停止推流 |
| **拉流代理** | addStreamProxy() / delStreamProxy() | 添加/删除拉流代理 |
| **FFmpeg 代理** | addFfmpegSource() / delFfmpegSource() | FFmpeg 拉流代理 |
| **录像** | startRecord() / stopRecord() / isRecording() | 录像控制 |
| **截图** | getSnap() | 获取截图 |
| **版本** | version() | 获取版本信息 |

#### 2.6.2 ZlmMediaHttpClient

HTTP 客户端接口，定义与 ZLMediaKit 交互的所有 API。

#### 2.6.3 DTO 数据传输对象

按功能分类的 DTO:

```
dto/
├── config/          # 配置相关 DTO
│   ├── ServerConfig, ApiConfig, HttpConfig, RtpConfig...
├── media/           # 媒体相关 DTO
│   ├── GetMediaList, MediaResp, MediaTrack...
├── proxy/           # 代理相关 DTO
│   ├── AddStreamProxy, AddFFmpegSource...
├── record/          # 录像相关 DTO
│   ├── StartRecord, GetMp4RecordFile...
├── response/        # 响应 DTO
│   └── ZlmResponse
└── rtp/             # RTP 相关 DTO
    ├── OpenRtpServer, StartSendRtp...
```

**依赖**:
- spring-cloud-starter-openfeign
- spring-boot-starter-web
- annotation (自定义注解)
- common (公共工具)

---

### 2.7 gb28181-service (业务服务模块)

**职责**: 实现 GB28181 核心业务逻辑，是整个系统的核心层。

**特点**:
- 整合 orm、sip、zlmediakit-service 模块
- 实现设备管理、视频播放、云台控制等业务
- 支持异步处理

**业务服务**:

| 服务 | 功能 |
|------|------|
| PlayService | 实时视频播放、历史回放控制 |
| CatalogService | 设备目录查询 |
| DeviceChannelService | 设备通道管理 |

**依赖**:
- zlmediakit-service (流媒体服务)
- orm (数据访问)
- gb28181-sip (SIP 协议)
- common (公共工具)
- jain-sip-ri (SIP 协议栈)
- mapstruct (对象映射)
- hutool-all (工具库)

---

### 2.8 api (API 接口模块)

**职责**: 提供 RESTful API 接口，是系统对外暴露的服务层。

**特点**:
- 使用 SpringDoc 生成 OpenAPI 文档
- 支持异步响应 (DeferredResult)
- 统一异常处理

**Controller 列表**:

| Controller | 路径 | 功能 |
|-----------|------|------|
| PlayController | /api/device/play | 视频播放控制 |
| DeviceChannelController | /api/device/channel | 设备通道管理 |
| CatalogController | /api/gb28181/catalog | 设备目录查询 |
| RecordController | /api/gb28181/record | 录像查询 |
| GB28181Controller | /api/gb28181 | GB28181 通用接口 |
| InfoController | /api/info | 系统信息 |

**API 分组**:

通过 SpringDoc GroupedOpenApi 对 API 进行分组:
- Play: 播放相关 API
- DeviceChannel: 设备通道 API
- GB28181: GB28181 协议 API

**全局配置**:
- WebConfig: Web MVC 配置
- SwaggerConfig: API 文档配置
- RequestInterceptor: 请求拦截器
- ExceptionAdvice: 全局异常处理

**依赖**:
- gb28181-service (业务服务)
- annotation (自定义注解)
- springdoc-openapi (API 文档)
- spring-boot-starter-web

---

## 3. 模块职责矩阵

| 模块 | 表示层 | 业务层 | 协议层 | 数据层 | 工具层 |
|------|--------|--------|--------|--------|--------|
| starter | ★ | | | | |
| api | ★ | | | | |
| gb28181-service | | ★ | | | |
| gb28181-sip | | | ★ | | |
| zlmediakit-service | | ★ | | | |
| orm | | | | ★ | |
| common | | | | | ★ |
| annotation | | | | | ★ |

## 4. 模块边界与设计原则

### 4.1 依赖原则

1. **单向依赖**: 下层模块不依赖上层模块
2. **接口隔离**: 模块间通过接口通信
3. **最小依赖**: 每个模块只依赖必要的模块

### 4.2 模块职责边界

```
┌────────────────────────────────────────────────────────────┐
│                        表示层 (api)                         │
│  - 接收 HTTP 请求                                          │
│  - 参数校验                                                │
│  - 响应封装                                                │
└───────────────────────────┬────────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────────┐
│                       业务层 (gb28181-service)              │
│  - 业务逻辑处理                                            │
│  - 流程编排                                                │
│  - 事务管理                                                │
└───────────────────────────┬────────────────────────────────┘
                            │
          ┌─────────────────┼─────────────────┐
          │                 │                 │
          ▼                 ▼                 ▼
┌─────────────────┐ ┌──────────────┐ ┌─────────────────────┐
│  协议层 (sip)   │ │ 数据层 (orm) │ │ 外部服务 (zlm-svc) │
│ - SIP 信令处理  │ │ - 数据库操作 │ │ - 流媒体调用       │
│ - 消息编解码    │ │ - SQL 执行   │ │ - API 封装         │
└─────────────────┘ └──────────────┘ └─────────────────────┘
```

### 4.3 扩展建议

1. **新增协议支持**: 在 gb28181-sip 或新建协议模块
2. **新增流媒体**: 实现新的流媒体服务模块
3. **新增 API**: 在 api 模块添加 Controller
4. **新增数据表**: 在 orm 模块生成 Mapper
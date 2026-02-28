# 开发者指南

## 1. 开发环境搭建

### 1.1 必要工具安装

| 工具 | 版本要求 | 下载地址 |
|------|---------|---------|
| JDK | 17+ | https://adoptium.net/ |
| Maven | 3.6+ | https://maven.apache.org/download.cgi |
| Git | 2.x | https://git-scm.com/downloads |
| IDE | IntelliJ IDEA 推荐 | https://www.jetbrains.com/idea/ |

### 1.2 IDE 配置

#### IntelliJ IDEA 配置

1. **JDK 配置**:
   - File → Project Structure → Project → SDK → 选择 JDK 17

2. **Maven 配置**:
   - File → Settings → Build Tools → Maven
   - Maven home directory: 选择 Maven 安装目录
   - User settings file: 选择 settings.xml

3. **编码配置**:
   - File → Settings → Editor → File Encodings
   - Global Encoding: UTF-8
   - Project Encoding: UTF-8
   - Properties Files: UTF-8

4. **解决控制台乱码**:
   - Help → Edit Custom VM Options
   - 添加: `-Dfile.encoding=GBK`

### 1.3 克隆项目

```bash
git clone https://github.com/shikong-sk/gb28181-docking-platform.git
cd gb28181-docking-platform
```

### 1.4 导入项目

#### IntelliJ IDEA

1. File → Open
2. 选择项目根目录
3. 等待 Maven 自动导入依赖

#### Eclipse

1. File → Import → Maven → Existing Maven Projects
2. 选择项目根目录
3. 点击 Finish

---

## 2. 项目结构

### 2.1 目录结构

```
gb28181-docking-platform/
├── pom.xml                    # 父 POM
├── docs/                      # 文档目录
│   ├── 01-overview.md
│   ├── 02-modules.md
│   ├── 03-tech-stack.md
│   ├── 04-api.md
│   ├── 05-database.md
│   ├── 06-deployment.md
│   └── 07-developer-guide.md
├── starter/                   # 启动模块
│   ├── pom.xml
│   └── src/main/
│       ├── java/
│       └── resources/
│           ├── application.yml
│           └── logback.xml
├── annotation/                # 注解模块
├── common/                    # 公共模块
├── orm/                       # 数据层模块
├── gb28181-sip/               # SIP 协议模块
├── zlmediakit-service/        # 流媒体服务模块
├── gb28181-service/           # 业务服务模块
└── api/                       # API 模块
```

### 2.2 包命名规范

```
cn.skcks.docking.gb28181
├── annotation/           # 自定义注解
├── common/               # 公共工具
│   ├── config/          # 配置类
│   ├── json/            # JSON 工具
│   ├── page/            # 分页工具
│   └── redis/           # Redis 工具
├── orm/                  # 数据层
│   └── mybatis/
│       ├── basic/       # 基础 Mapper
│       └── dynamic/     # 动态 SQL Mapper
├── sip/                  # SIP 协议
│   └── utils/           # SIP 工具类
├── media/                # 流媒体服务
│   ├── config/          # 配置
│   ├── dto/             # 数据传输对象
│   ├── feign/           # Feign 客户端
│   └── proxy/           # 服务代理
├── service/              # 业务服务
│   ├── catalog/         # 目录服务
│   ├── device/          # 设备服务
│   ├── play/            # 播放服务
│   └── record/          # 录像服务
└── api/                  # API 层
    ├── device/          # 设备相关 API
    ├── gb28181/         # GB28181 相关 API
    ├── info/            # 信息 API
    └── play/            # 播放 API
```

---

## 3. 编码规范

### 3.1 命名规范

#### 类命名

| 类型 | 命名规则 | 示例 |
|------|---------|------|
| Controller | XxxController | PlayController |
| Service | XxxService | PlayService |
| Repository/Mapper | XxxMapper | DeviceMapper |
| DTO | XxxDTO | RealTimePlayDTO |
| VO | XxxVO | RecordInfoItemVO |
| Entity | DockingXxx | DockingDevice |
| 工具类 | XxxUtils | SipUtil |
| 配置类 | XxxConfig | ZlmMediaConfig |

#### 方法命名

| 操作 | 前缀 | 示例 |
|------|------|------|
| 查询单个 | get | getDeviceById |
| 查询列表 | list/listAll | listDevices |
| 分页查询 | getWithPage | getWithPage |
| 保存 | save | saveDevice |
| 更新 | update | updateStatus |
| 删除 | delete | deleteById |
| 判断 | is/has | isOnline |
| 统计 | count | countDevices |

#### 变量命名

```java
// 常量：全大写下划线
public static final String DEFAULT_CHARSET = "GB2312";

// 成员变量：驼峰
private String deviceId;

// 局部变量：驼峰
String channelName = channel.getName();
```

### 3.2 注释规范

#### 类注释

```java
/**
 * 视频播放控制器
 * <p>
 * 提供实时视频点播、历史录像回放等功能
 * </p>
 *
 * @author shikong
 * @since 0.1.0
 */
@RestController
public class PlayController {
    // ...
}
```

#### 方法注释

```java
/**
 * 实时视频点播
 *
 * @param deviceId  设备ID，20位国标编码
 * @param channelId 通道ID，20位国标编码
 * @param timeout   超时时间(秒)，默认30
 * @return 异步响应结果，包含流媒体播放地址
 */
public DeferredResult<JsonResponse<String>> realTimePlay(
        String deviceId, String channelId, long timeout) {
    // ...
}
```

### 3.3 代码格式

#### 缩进与空格

- 使用 4 个空格缩进
- 运算符两侧加空格
- 逗号后加空格

#### 大括号风格

```java
// 推荐：同行大括号
if (condition) {
    // do something
} else {
    // do other
}

// 不推荐：换行大括号
if (condition)
{
    // do something
}
```

### 3.4 异常处理

```java
// 推荐：具体异常
try {
    // 可能出错的代码
} catch (IOException e) {
    log.error("读取文件失败: {}", fileName, e);
    throw new RuntimeException("文件读取失败", e);
}

// 不推荐：捕获所有异常
try {
    // ...
} catch (Exception e) {
    // 忽略异常
}
```

---

## 4. 开发流程

### 4.1 创建新功能

1. **创建 DTO**
```java
// api/src/main/java/.../dto/XxxDTO.java
@Data
@Schema(title = "功能描述")
public class XxxDTO {
    @NotBlank
    @Schema(description = "字段说明")
    private String field;
}
```

2. **创建 Service**
```java
// gb28181-service/src/main/java/.../service/XxxService.java
@Service
@RequiredArgsConstructor
public class XxxService {
    // 业务逻辑
}
```

3. **创建 Controller**
```java
// api/src/main/java/.../api/XxxController.java
@Tag(name = "功能名称")
@RestController
@JsonMapping("/api/xxx")
@RequiredArgsConstructor
public class XxxController {
    private final XxxService xxxService;
    
    @GetJson("/list")
    public JsonResponse<List<XxxVO>> list() {
        return JsonResponse.success(xxxService.listAll());
    }
}
```

### 4.2 数据库开发

1. **创建表**
```sql
CREATE TABLE `docking_xxx` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

2. **生成 Mapper**
```bash
cd orm
mvn mybatis-generator:generate
```

3. **使用 Mapper**
```java
@Service
@RequiredArgsConstructor
public class XxxService {
    private final DockingXxxMapper xxxMapper;
    
    public List<DockingXxx> listAll() {
        return xxxMapper.selectAll();
    }
}
```

### 4.3 单元测试

```java
@SpringBootTest
class XxxServiceTest {
    
    @Autowired
    private XxxService xxxService;
    
    @Test
    void testListAll() {
        List<Xxx> result = xxxService.listAll();
        assertNotNull(result);
    }
}
```

---

## 5. Git 工作流

### 5.1 分支管理

```
main        # 主分支，稳定版本
├── develop # 开发分支
│   ├── feature/xxx  # 功能分支
│   ├── bugfix/xxx   # 修复分支
│   └── refactor/xxx # 重构分支
└── release/x.x.x    # 发布分支
```

### 5.2 提交规范

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type 类型**:
- `feat`: 新功能
- `fix`: 修复 bug
- `docs`: 文档更新
- `style`: 代码格式
- `refactor`: 重构
- `test`: 测试
- `chore`: 构建/工具

**示例**:
```
feat(play): 添加实时视频点播功能

- 支持实时视频请求
- 返回流媒体播放地址
- 支持超时配置

Closes #123
```

### 5.3 工作流程

```bash
# 创建功能分支
git checkout -b feature/new-feature

# 开发完成后提交
git add .
git commit -m "feat: 添加新功能"

# 推送到远程
git push origin feature/new-feature

# 创建 Pull Request
# 在 GitHub 上创建 PR 合并到 develop
```

---

## 6. 调试技巧

### 6.1 日志调试

```java
@Slf4j
@Service
public class XxxService {
    
    public void doSomething(String param) {
        log.debug("开始处理，参数: {}", param);
        
        try {
            // 业务逻辑
            log.info("处理成功，结果: {}", result);
        } catch (Exception e) {
            log.error("处理失败: {}", e.getMessage(), e);
        }
    }
}
```

### 6.2 远程调试

```bash
# 启动时添加调试参数
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar starter.jar
```

在 IntelliJ IDEA 中配置 Remote JVM Debug，连接端口 5005。

### 6.3 SQL 调试

在 `application.yml` 中启用 MyBatis SQL 日志:

```yaml
mybatis:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

---

## 7. 性能优化

### 7.1 数据库优化

1. **使用索引**
```sql
-- 为常用查询字段添加索引
CREATE INDEX idx_device_id ON docking_device_channel(device_id);
```

2. **分页查询**
```java
// 使用 PageHelper
PageHelper.startPage(page, size);
List<DockingDevice> list = deviceMapper.selectAll();
PageInfo<DockingDevice> pageInfo = new PageInfo<>(list);
```

3. **批量操作**
```java
// 批量插入
deviceMapper.insertList(deviceList);
```

### 7.2 缓存使用

```java
// 使用 Redis 缓存
public DockingDevice getDevice(String deviceId) {
    String key = "device:" + deviceId;
    String cached = RedisUtil.StringOps.get(key);
    
    if (cached != null) {
        return JSONUtil.toBean(cached, DockingDevice.class);
    }
    
    DockingDevice device = deviceMapper.selectByDeviceId(deviceId);
    RedisUtil.StringOps.setEx(key, JSONUtil.toJsonStr(device), 1, TimeUnit.HOURS);
    
    return device;
}
```

### 7.3 异步处理

```java
// 使用 DeferredResult 异步响应
@GetMapping("/async")
public DeferredResult<JsonResponse<String>> asyncEndpoint() {
    DeferredResult<JsonResponse<String>> result = new DeferredResult<>(30000L);
    
    CompletableFuture.supplyAsync(() -> {
        // 异步处理
        return "result";
    }).thenAccept(r -> {
        result.setResult(JsonResponse.success(r));
    });
    
    return result;
}
```

---

## 8. 常用命令

### 8.1 Maven 命令

```bash
# 编译
mvn compile

# 打包 (跳过测试)
mvn package -DskipTests

# 安装到本地仓库
mvn install -DskipTests

# 清理并打包
mvn clean package

# 查看依赖树
mvn dependency:tree

# 更新依赖
mvn versions:display-dependency-updates
```

### 8.2 Git 命令

```bash
# 查看状态
git status

# 查看提交历史
git log --oneline -10

# 撤销未提交的修改
git checkout -- <file>

# 撤销最后一次提交
git reset --soft HEAD~1

# 查看远程仓库
git remote -v
```

---

## 9. 发布流程

### 9.1 发布到中央仓库

```bash
# GPG 签名
gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>

# 发布
mvn clean deploy -P release
```

### 9.2 发布 Docker 镜像

```bash
# 构建 Docker 镜像
mvn clean package -Pdocker

# 加载镜像
docker load < gb28181-docking-platform.image

# 推送到仓库
docker tag skcks.cn/gb28181-docking-platform:0.1.0 your-registry/gb28181:0.1.0
docker push your-registry/gb28181:0.1.0
```

---

## 10. 常见问题

### 10.1 编译失败

**问题**: Maven 编译时报错找不到依赖

**解决**: 
```bash
# 清理并更新依赖
mvn clean install -U
```

### 10.2 端口占用

**问题**: 启动时端口被占用

**解决**:
```bash
# Windows
netstat -ano | findstr :28181
taskkill /F /PID <PID>

# Linux/Mac
lsof -i :28181
kill -9 <PID>
```

### 10.3 中文乱码

**问题**: 控制台输出中文乱码

**解决**:
- IDE 设置 UTF-8 编码
- 启动参数添加 `-Dfile.encoding=UTF-8`
- Windows 控制台执行 `chcp 65001`

---

## 11. 参考资源

### 11.1 官方文档

- Spring Boot: https://spring.io/projects/spring-boot
- MyBatis: https://mybatis.org/mybatis-3/
- SpringDoc: https://springdoc.org/
- ZLMediaKit: https://github.com/ZLMediaKit/ZLMediaKit

### 11.2 GB28181 相关

- GB/T 28181-2016 标准
- GB28181 协议详解
- SIP 协议 RFC 3261

### 11.3 学习资源

- Spring Boot 实战
- MyBatis 入门到精通
- Netty 实战
# 部署指南

## 1. 部署概述

本文档介绍 GB28181 对接平台的多种部署方式，包括 JAR 包部署、Docker 容器化部署等。

### 1.1 系统架构

```
┌─────────────────────────────────────────────────────────────────────┐
│                           客户端/前端                                 │
└─────────────────────────────────┬───────────────────────────────────┘
                                  │ HTTP/WebSocket
                                  ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      GB28181 对接平台 (28181)                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────────┐  │
│  │  API Server  │  │  SIP Server  │  │   Business Services      │  │
│  │   (HTTP)     │  │   (UDP/TCP)  │  │   (Play, Catalog...)     │  │
│  └──────────────┘  └──────────────┘  └──────────────────────────┘  │
└─────────────────────────────────┬───────────────────────────────────┘
                                  │
          ┌───────────────────────┼───────────────────────┐
          │                       │                       │
          ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────────┐
│     MySQL       │    │     Redis       │    │    ZLMediaKit       │
│   (数据库)      │    │    (缓存)       │    │    (流媒体)         │
│   默认: 3306    │    │   默认: 6379    │    │   HTTP: 80          │
└─────────────────┘    └─────────────────┘    │   RTSP: 554         │
                                              │   RTP: 10000-20000  │
                                              └─────────────────────┘
```

### 1.2 端口说明

| 服务 | 端口 | 协议 | 说明 |
|------|------|------|------|
| HTTP API | 28181 | TCP | RESTful API 接口 |
| SIP | 5060 | UDP/TCP | GB28181 信令端口 |
| MySQL | 3306 | TCP | 数据库端口 |
| Redis | 6379 | TCP | 缓存服务端口 |
| ZLMediaKit HTTP | 80 | TCP | 流媒体 HTTP API |
| ZLMediaKit RTSP | 554 | TCP | RTSP 流端口 |
| ZLMediaKit RTP | 10000-20000 | UDP | RTP 流端口范围 |

---

## 2. 环境要求

### 2.1 硬件要求

| 配置项 | 最低要求 | 推荐配置 |
|-------|---------|---------|
| CPU | 2 核 | 4 核+ |
| 内存 | 4 GB | 8 GB+ |
| 磁盘 | 20 GB | 100 GB+ (SSD) |
| 网络 | 100 Mbps | 1 Gbps |

### 2.2 软件要求

| 软件 | 版本要求 |
|------|---------|
| Java JDK | 17+ |
| Maven | 3.6+ |
| MySQL | 8.0+ |
| Redis | 5.0+ |
| Docker (可选) | 20.0+ |
| ZLMediaKit | 最新稳定版 |

### 2.3 操作系统支持

- Linux: CentOS 7+, Ubuntu 18.04+, Debian 10+
- Windows: Windows 10/Server 2016+
- macOS: 10.15+

---

## 3. JAR 包部署

### 3.1 编译打包

```bash
# 克隆项目
git clone https://github.com/shikong-sk/gb28181-docking-platform.git
cd gb28181-docking-platform

# 编译打包
mvn clean package -DskipTests

# 打包后的 JAR 文件位置
# starter/target/starter.jar
```

### 3.2 配置文件

创建 `application.yml` 配置文件:

```yaml
server:
  port: 28181

project:
  version: 0.1.0

spring:
  application:
    name: gb28181-docking-platform
  
  # 数据库配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/gb28181_docking_platform?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20
      idle-timeout: 30000
      max-lifetime: 1800000
      connection-timeout: 30000
  
  # Redis 配置
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0
      timeout: 10000
  
  # MVC 配置
  mvc:
    async:
      request-timeout: 3m

# MyBatis 配置
mybatis:
  mapper-locations: classpath:**/mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true

# GB28181 SIP 配置 (示例)
gb28181:
  sip:
    id: 44050100002000000001
    domain: 4405010000
    ip: 192.168.1.100
    port: 5060
    password: 123456

# ZLMediaKit 配置
zlm:
  url: http://192.168.1.100:80
  secret: your_secret_key
```

### 3.3 启动服务

```bash
# 前台启动
java -jar starter.jar

# 指定配置文件启动
java -jar starter.jar --spring.config.location=/path/to/application.yml

# 后台启动
nohup java -jar starter.jar > app.log 2>&1 &

# 指定 JVM 参数
java -Xms512m -Xmx2048m -jar starter.jar
```

### 3.4 停止服务

```bash
# 查找进程
ps -ef | grep starter.jar

# 优雅停止
kill -15 <PID>

# 强制停止
kill -9 <PID>
```

### 3.5 Systemd 服务配置

创建服务文件 `/etc/systemd/system/gb28181.service`:

```ini
[Unit]
Description=GB28181 Docking Platform
After=network.target mysql.service redis.service

[Service]
Type=simple
User=gb28181
WorkingDirectory=/opt/gb28181-docking-platform
ExecStart=/usr/bin/java -Xms512m -Xmx2048m -jar /opt/gb28181-docking-platform/starter.jar
ExecStop=/bin/kill -15 $MAINPID
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

管理服务:

```bash
# 重载服务配置
systemctl daemon-reload

# 启动服务
systemctl start gb28181

# 停止服务
systemctl stop gb28181

# 开机自启
systemctl enable gb28181

# 查看状态
systemctl status gb28181
```

---

## 4. Docker 部署

### 4.1 使用 Maven 构建 Docker 镜像

```bash
# 编译并构建 Docker 镜像
mvn clean package -Pdocker

# 镜像文件位于项目根目录
# gb28181-docking-platform.image
```

### 4.2 Dockerfile

项目支持通过 Dockerfile 构建镜像:

```dockerfile
FROM openjdk:17-jdk-slim

LABEL maintainer="shikong <919411476@qq.com>"

WORKDIR /opt/gb28181-docking-platform

COPY starter/target/starter.jar app.jar

EXPOSE 28181 5060/udp

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 4.3 Docker Compose 部署

创建 `docker-compose.yml`:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: gb28181-mysql
    environment:
      MYSQL_ROOT_PASSWORD: your_password
      MYSQL_DATABASE: gb28181_docking_platform
      TZ: Asia/Shanghai
    volumes:
      - mysql_data:/var/lib/mysql
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "3306:3306"
    networks:
      - gb28181-network
    restart: unless-stopped

  redis:
    image: redis:7-alpine
    container_name: gb28181-redis
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - gb28181-network
    restart: unless-stopped

  zlmediakit:
    image: zlmediakit/zlmediakit:master
    container_name: gb28181-zlm
    environment:
      - TZ=Asia/Shanghai
    ports:
      - "80:80"
      - "554:554"
      - "10000:10000"
      - "10000:10000/udp"
      - "8000:8000/udp"
    volumes:
      - zlm_data:/opt/media
    networks:
      - gb28181-network
    restart: unless-stopped

  gb28181-platform:
    image: skcks.cn/gb28181-docking-platform:0.1.0
    container_name: gb28181-platform
    depends_on:
      - mysql
      - redis
      - zlmediakit
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - TZ=Asia/Shanghai
    ports:
      - "28181:28181"
      - "5060:5060/udp"
    volumes:
      - ./application.yml:/opt/gb28181-docking-platform/application.yml
    networks:
      - gb28181-network
    restart: unless-stopped

networks:
  gb28181-network:
    driver: bridge

volumes:
  mysql_data:
  redis_data:
  zlm_data:
```

### 4.4 启动 Docker Compose

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f gb28181-platform

# 停止服务
docker-compose down

# 停止并删除数据卷
docker-compose down -v
```

### 4.5 Docker 运行命令

```bash
# 单独运行 GB28181 平台容器
docker run -d \
  --name gb28181 \
  -p 28181:28181 \
  -p 5060:5060/udp \
  -v /path/to/application.yml:/opt/gb28181-docking-platform/application.yml \
  skcks.cn/gb28181-docking-platform:0.1.0
```

---

## 5. 依赖服务安装

### 5.1 MySQL 安装

**Ubuntu/Debian**:
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

**CentOS/RHEL**:
```bash
sudo yum install mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld
sudo mysql_secure_installation
```

**创建数据库**:
```sql
CREATE DATABASE gb28181_docking_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
CREATE USER 'gb28181'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON gb28181_docking_platform.* TO 'gb28181'@'%';
FLUSH PRIVILEGES;
```

### 5.2 Redis 安装

**Ubuntu/Debian**:
```bash
sudo apt install redis-server
sudo systemctl start redis
sudo systemctl enable redis
```

**CentOS/RHEL**:
```bash
sudo yum install redis
sudo systemctl start redis
sudo systemctl enable redis
```

**配置 Redis** (`/etc/redis/redis.conf`):
```conf
bind 127.0.0.1
protected-mode yes
port 6379
maxmemory 256mb
maxmemory-policy allkeys-lru
```

### 5.3 ZLMediaKit 安装

**Docker 方式 (推荐)**:
```bash
docker run -d \
  --name zlmediakit \
  -p 80:80 \
  -p 554:554 \
  -p 10000:10000 \
  -p 10000:10000/udp \
  zlmediakit/zlmediakit:master
```

**源码编译**:
```bash
# 安装依赖
sudo apt install git cmake build-essential libssl-dev

# 克隆代码
git clone https://github.com/ZLMediaKit/ZLMediaKit.git
cd ZLMediaKit
git submodule update --init

# 编译
mkdir build && cd build
cmake ..
make -j$(nproc)

# 运行
./MediaServer
```

---

## 6. 生产环境配置

### 6.1 JVM 调优

```bash
# 推荐 JVM 参数
java \
  -Xms2g \
  -Xmx4g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/var/log/gb28181/heap_dump.hprof \
  -Dfile.encoding=UTF-8 \
  -Duser.timezone=Asia/Shanghai \
  -jar starter.jar
```

### 6.2 日志配置

创建 `logback-spring.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property name="LOG_PATH" value="/var/log/gb28181"/>
    
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/application.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/application.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
            <totalSizeCap>1GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

### 6.3 Nginx 反向代理

```nginx
upstream gb28181_backend {
    server 127.0.0.1:28181;
    keepalive 32;
}

server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        proxy_pass http://gb28181_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_connect_timeout 60s;
        proxy_read_timeout 300s;
        proxy_send_timeout 300s;
    }
    
    location /swagger-ui {
        proxy_pass http://gb28181_backend;
    }
}
```

---

## 7. 健康检查

### 7.1 接口健康检查

```bash
# 检查服务是否启动
curl http://localhost:28181/actuator/health

# 检查 API 文档
curl http://localhost:28181/swagger-ui.html
```

### 7.2 数据库连接检查

```bash
mysql -u gb28181 -p -e "SELECT 1"
```

### 7.3 Redis 连接检查

```bash
redis-cli ping
```

### 7.4 ZLMediaKit 状态检查

```bash
curl http://localhost/index/api/getServerConfig?secret=your_secret
```

---

## 8. 常见问题排查

### 8.1 服务无法启动

1. 检查端口占用
```bash
netstat -tlnp | grep 28181
netstat -tlnp | grep 5060
```

2. 检查日志文件
```bash
tail -f /var/log/gb28181/application.log
```

### 8.2 数据库连接失败

1. 检查 MySQL 服务状态
2. 验证用户名密码
3. 检查防火墙规则

### 8.3 设备无法注册

1. 检查 SIP 端口是否开放
2. 验证 SIP 配置是否正确
3. 检查设备国标编码格式

### 8.4 视频无法播放

1. 检查 ZLMediaKit 服务状态
2. 验证 RTP 端口范围是否开放
3. 检查设备流传输模式配置

---

## 9. 安全配置

### 9.1 防火墙配置

```bash
# 开放必要端口
firewall-cmd --permanent --add-port=28181/tcp
firewall-cmd --permanent --add-port=5060/udp
firewall-cmd --permanent --add-port=10000-20000/udp
firewall-cmd --reload
```

### 9.2 敏感配置加密

建议使用 Jasypt 加密敏感配置:

```yaml
jasypt:
  encryptor:
    password: your-encryption-key

spring:
  datasource:
    password: ENC(encrypted_password)
```

---

## 10. 监控与告警

### 10.1 Prometheus 监控

添加 Spring Boot Actuator 依赖后，暴露监控端点:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

### 10.2 日志监控

建议使用 ELK Stack (Elasticsearch + Logstash + Kibana) 收集和分析日志。
# API 接口文档

## 1. 接口概述

本项目提供 RESTful API 接口，所有接口均返回 JSON 格式数据。接口文档通过 SpringDoc 自动生成，支持 Swagger UI 在线查看和测试。

### 1.1 接口基础信息

| 配置项 | 值 |
|-------|-----|
| 基础路径 | http://localhost:28181 |
| 接口前缀 | /api |
| 响应格式 | application/json |
| 字符编码 | UTF-8 |

### 1.2 文档访问

- **Swagger UI**: http://localhost:28181/swagger-ui.html
- **OpenAPI JSON**: http://localhost:28181/v3/api-docs

### 1.3 API 分组

| 分组名称 | 路径前缀 | 说明 |
|---------|---------|------|
| Play | /api/device/play | 视频播放控制 |
| DeviceChannel | /api/device/channel | 设备通道管理 |
| Info | /api/device/info | 设备信息查询 |
| GB28181 | /api/gb28181 | GB28181 协议接口 |
| Record | /api/gb28181/record | 录像相关接口 |

---

## 2. 统一响应格式

所有接口均返回统一的 JSON 响应格式：

### 2.1 响应结构

```json
{
    "code": 200,
    "data": {},
    "msg": "操作成功"
}
```

### 2.2 响应字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| code | int | 状态码，200 表示成功 |
| data | T | 响应数据，类型根据接口而定 |
| msg | string | 响应消息 |

### 2.3 常见状态码

| 状态码 | 说明 |
|-------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 500 | 服务器内部错误 |

### 2.4 分页响应格式

```json
{
    "code": 200,
    "data": {
        "list": [],
        "total": 100,
        "pageNum": 1,
        "pageSize": 10,
        "pages": 10
    },
    "msg": "操作成功"
}
```

---

## 3. 视频播放接口

### 3.1 实时视频点播

**接口地址**: `GET /api/device/play/realTimePlay`

**接口描述**: 请求设备实时视频流，返回流媒体播放地址

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| deviceId | string | 是 | 设备 ID，20 位国标编码 |
| channelId | string | 是 | 通道 ID，20 位国标编码 |
| timeout | long | 否 | 超时时间(秒)，默认 30，最小值 30 |

**请求示例**:
```
GET /api/device/play/realTimePlay?deviceId=44050100001180000001&channelId=44050100001180000001&timeout=30
```

**响应示例**:
```json
{
    "code": 200,
    "data": "rtsp://192.168.1.100:554/live/44050100001180000001",
    "msg": "操作成功"
}
```

**说明**:
- 该接口为异步接口，返回 `DeferredResult`
- 返回的流媒体地址可支持 RTSP/RTMP/HLS 等多种协议
- 需要设备在线且支持实时视频

---

### 3.2 停止实时视频

**接口地址**: `GET /api/device/play/realtimeStop`

**接口描述**: 停止设备的实时视频推流

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| deviceId | string | 是 | 设备 ID |
| channelId | string | 是 | 通道 ID |

**请求示例**:
```
GET /api/device/play/realtimeStop?deviceId=44050100001180000001&channelId=44050100001180000001
```

**响应示例**:
```json
{
    "code": 200,
    "data": null,
    "msg": "操作成功"
}
```

---

### 3.3 历史录像回放

**接口地址**: `GET /api/device/play/recordPlay`

**接口描述**: 请求设备历史录像回放

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| deviceId | string | 是 | 设备 ID |
| channelId | string | 是 | 通道 ID |
| startTime | string | 是 | 开始时间，格式：yyyy-MM-dd HH:mm:ss |
| endTime | string | 是 | 结束时间，格式：yyyy-MM-dd HH:mm:ss |
| timeout | long | 否 | 超时时间(秒)，默认 30 |

**请求示例**:
```
GET /api/device/play/recordPlay?deviceId=44050100001180000001&channelId=44050100001180000001&startTime=2024-01-01 00:00:00&endTime=2024-01-01 01:00:00
```

**响应示例**:
```json
{
    "code": 200,
    "data": "rtsp://192.168.1.100:554/playback/44050100001180000001",
    "msg": "操作成功"
}
```

---

### 3.4 停止历史回放

**接口地址**: `GET /api/device/play/recordStop`

**接口描述**: 停止设备的历史录像回放

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| deviceId | string | 是 | 设备 ID |
| channelId | string | 是 | 通道 ID |
| startTime | string | 是 | 开始时间 |
| endTime | string | 是 | 结束时间 |

**响应示例**:
```json
{
    "code": 200,
    "data": null,
    "msg": "操作成功"
}
```

---

## 4. 设备通道接口

### 4.1 分页查询设备通道

**接口地址**: `GET /api/device/channel/page`

**接口描述**: 分页查询设备通道信息

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| page | int | 是 | 页码，从 1 开始 |
| size | int | 是 | 每页数量 |

**请求示例**:
```
GET /api/device/channel/page?page=1&size=10
```

**响应示例**:
```json
{
    "code": 200,
    "data": {
        "list": [
            {
                "id": 1,
                "deviceId": "44050100001180000001",
                "channelId": "44050100001180000001",
                "name": "前门摄像头",
                "manufacturer": "海康威视",
                "model": "DS-2CD2T45D",
                "owner": "测试单位",
                "civilCode": "440501",
                "address": "广东省汕头市",
                "parentDeviceId": "44050100001180000000",
                "safetyWay": null,
                "registerWay": 1,
                "certifiable": 0,
                "errCode": 0,
                "endTime": null,
                "secrecy": 0,
                "ipAddress": "192.168.1.100",
                "port": 554,
                "password": null,
                "ptzType": 1,
                "status": 1,
                "longitude": 116.397428,
                "latitude": 39.90923
            }
        ],
        "total": 100,
        "pageNum": 1,
        "pageSize": 10,
        "pages": 10
    },
    "msg": "操作成功"
}
```

---

## 5. 设备信息接口

### 5.1 获取设备列表

**接口地址**: `GET /api/device/info/list`

**接口描述**: 获取所有已注册设备列表

**请求参数**: 无

**响应示例**:
```json
{
    "code": 200,
    "data": [
        {
            "id": 1,
            "deviceId": "44050100001180000001",
            "name": "测试设备",
            "manufacturer": "海康威视",
            "model": "NVR",
            "firmware": "V5.5.52",
            "transport": "UDP",
            "streamMode": "UDP",
            "charset": "GB2312",
            "online": 1,
            "registerTime": "2024-01-01 10:00:00",
            "keepaliveTime": "2024-01-01 12:00:00",
            "ip": "192.168.1.100",
            "port": 5060,
            "expires": 3600
        }
    ],
    "msg": "操作成功"
}
```

---

## 6. GB28181 协议接口

### 6.1 获取设备目录

**接口地址**: `GET /api/gb28181/catalog`

**接口描述**: 向设备发送目录查询请求，获取设备下的通道列表

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| gbDeviceId | string | 是 | 设备国标编码 |

**请求示例**:
```
GET /api/gb28181/catalog?gbDeviceId=44050100001180000001
```

**响应示例**:
```json
{
    "code": 200,
    "data": [
        {
            "deviceId": "44050100001180000001",
            "name": "通道1",
            "manufacturer": "海康威视",
            "model": "DS-2CD2T45D",
            "owner": "测试单位",
            "civilCode": "440501",
            "block": null,
            "address": "广东省汕头市",
            "parentDeviceId": "44050100001180000000",
            "safetyWay": null,
            "registerWay": 1,
            "certifiable": 0,
            "errCode": 0,
            "endTime": null,
            "secrecy": 0,
            "ipAddress": "192.168.1.100",
            "port": 554,
            "password": null,
            "ptzType": 1,
            "status": 1,
            "longitude": 116.397428,
            "latitude": 39.90923
        }
    ],
    "msg": "操作成功"
}
```

**说明**:
- 该接口为异步接口，需要等待设备响应
- 响应时间取决于设备性能和网络状况

---

### 6.2 查询历史录像列表

**接口地址**: `GET /api/gb28181/record/list`

**接口描述**: 查询设备指定通道的历史录像文件列表

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| gbDeviceId | string | 是 | 设备国标编码 |
| gbDeviceChannelId | string | 是 | 通道国标编码 |
| date | string | 否 | 查询日期，格式：yyyy-MM-dd |
| timeout | long | 否 | 超时时间(秒) |

**请求示例**:
```
GET /api/gb28181/record/list?gbDeviceId=44050100001180000001&gbDeviceChannelId=44050100001180000001&date=2024-01-01
```

**响应示例**:
```json
{
    "code": 200,
    "data": [
        {
            "deviceId": "44050100001180000001",
            "name": "录像文件1",
            "startTime": "2024-01-01 00:00:00",
            "endTime": "2024-01-01 01:00:00",
            "fileSize": 1024000,
            "filePath": "/record/2024/01/01/001.mp4"
        }
    ],
    "msg": "操作成功"
}
```

---

## 7. ZLMediaKit 流媒体接口

以下接口通过 `ZlmMediaService` 服务类调用，不直接暴露 HTTP 接口。

### 7.1 RTP 服务管理

| 方法 | 说明 |
|------|------|
| openRtpServer | 开启 RTP 接收端口 |
| closeRtpServer | 关闭 RTP 接收端口 |
| listRtpServer | 获取 RTP 服务列表 |

### 7.2 推流管理

| 方法 | 说明 |
|------|------|
| startSendRtp | 开始 RTP 推流 |
| startSendRtpPassive | 开始被动 RTP 推流 |
| stopSendRtp | 停止 RTP 推流 |

### 7.3 流代理管理

| 方法 | 说明 |
|------|------|
| addStreamProxy | 添加拉流代理 |
| delStreamProxy | 删除拉流代理 |
| addStreamPusherProxy | 添加推流代理 |
| delStreamPusherProxy | 删除推流代理 |

### 7.4 录像管理

| 方法 | 说明 |
|------|------|
| startRecord | 开始录像 |
| stopRecord | 停止录像 |
| isRecording | 查询录像状态 |
| getMp4RecordFile | 获取 MP4 录像文件列表 |

### 7.5 其他功能

| 方法 | 说明 |
|------|------|
| getMediaList | 获取流列表 |
| getSnap | 获取截图 |
| getServerConfig | 获取服务器配置 |
| setServerConfig | 设置服务器配置 |
| version | 获取版本信息 |

---

## 8. 错误处理

### 8.1 错误响应格式

```json
{
    "code": 500,
    "data": null,
    "msg": "设备离线"
}
```

### 8.2 常见错误码

| 错误码 | 说明 | 处理建议 |
|-------|------|---------|
| 400 | 参数错误 | 检查请求参数格式 |
| 404 | 资源不存在 | 检查设备 ID 或通道 ID |
| 500 | 服务器内部错误 | 查看服务器日志 |
| 503 | 设备离线 | 确认设备在线状态 |
| 504 | 请求超时 | 增加超时时间或检查网络 |

### 8.3 全局异常处理

系统通过 `ExceptionAdvice` 类统一处理异常，返回标准错误响应。

---

## 9. 接口调用示例

### 9.1 cURL 示例

```bash
# 实时视频点播
curl -X GET "http://localhost:28181/api/device/play/realTimePlay?deviceId=44050100001180000001&channelId=44050100001180000001"

# 获取设备列表
curl -X GET "http://localhost:28181/api/device/info/list"

# 分页查询通道
curl -X GET "http://localhost:28181/api/device/channel/page?page=1&size=10"
```

### 9.2 JavaScript 示例

```javascript
// 实时视频点播
async function realTimePlay(deviceId, channelId) {
    const response = await fetch(
        `http://localhost:28181/api/device/play/realTimePlay?deviceId=${deviceId}&channelId=${channelId}`
    );
    const result = await response.json();
    return result;
}

// 获取设备列表
async function getDeviceList() {
    const response = await fetch('http://localhost:28181/api/device/info/list');
    const result = await response.json();
    return result.data;
}
```

### 9.3 Java 示例

```java
// 使用 RestTemplate
RestTemplate restTemplate = new RestTemplate();
String url = "http://localhost:28181/api/device/play/realTimePlay?deviceId={deviceId}&channelId={channelId}";
JsonResponse<String> response = restTemplate.getForObject(url, JsonResponse.class, 
    "44050100001180000001", "44050100001180000001");

// 使用 WebClient (异步)
WebClient webClient = WebClient.create("http://localhost:28181");
Mono<JsonResponse> response = webClient.get()
    .uri(uriBuilder -> uriBuilder
        .path("/api/device/play/realTimePlay")
        .queryParam("deviceId", "44050100001180000001")
        .queryParam("channelId", "44050100001180000001")
        .build())
    .retrieve()
    .bodyToMono(JsonResponse.class);
```

---

## 10. 接口设计规范

### 10.1 URL 命名规范

- 使用小写字母
- 使用连字符分隔单词
- 使用名词表示资源
- 使用动词表示操作

### 10.2 HTTP 方法使用

| 方法 | 用途 |
|------|------|
| GET | 查询操作 |
| POST | 创建操作 |
| PUT | 更新操作 |
| DELETE | 删除操作 |

### 10.3 版本控制

当前版本未实现接口版本控制，后续可通过以下方式实现：
- URL 路径版本：`/api/v1/device`
- 请求头版本：`Accept: application/vnd.api.v1+json`

---

## 11. 附录

### 11.1 国标编码规则

GB/T 28181 规定的设备编码为 20 位数字：
- 第 1-2 位：省级行政区划
- 第 3-4 位：市级行政区划
- 第 5-6 位：区县行政区划
- 第 7-8 位：基层接入单位编码
- 第 9-13 位：设备类型编码
- 第 14-20 位：设备序号

### 11.2 设备类型编码

| 编码 | 类型 |
|------|------|
| 111 | DVR |
| 112 | 视频服务器 |
| 113 | 编码器 |
| 114 | 解码器 |
| 115 | 视频切换矩阵 |
| 116 | 音频切换矩阵 |
| 117 | 报警控制器 |
| 118 | 网络视频录像机 (NVR) |
| 119 | 球机 |
| 120 | 半球 |
| 121 | 固定摄像机 |
| 122 | 遥控摄像机 |
| 200 | 平台 |
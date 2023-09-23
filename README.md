# gb28181-docking-platform

gb28181 协议 对接平台
gb28181-docking-platform


### 项目打包
#### 打包 为 jar
```shell
mvn clean package
```
打包后jar在 starter/target/starter.jar

#### 打包 为 docker 镜像
一键脚本(纯docker环境打包 + 编译)
```
chmod +x ./package-docker.sh
./package-docker.sh
```
打包后的 docker镜像文件位于 项目根目录 gb28181-docking-platform.image

##### 测试运行
```shell
docker run --name gb28181 --rm \
  -v /mnt/sk-w/run/docker/maven/application.yml:/opt/gb28181-docking-platform/application.yml \
  skcks.cn/gb28181-docking-platform:0.0.1-SNAPSHOT
```

### 打包到本地私仓
```shell
mvn deploy -s settings.xml -DaltDeploymentRepository=amleixun-mvn-reop::default::file:H:/Repository/skcks.cn/gb28181-docking-platform-mvn-repo
```
git push 推送即可

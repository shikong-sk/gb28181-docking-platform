# gb28181-docking-platform
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fshikong-sk%2Fgb28181-docking-platform.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fshikong-sk%2Fgb28181-docking-platform?ref=badge_shield)


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

### 清理私仓
切换到私仓目录
例: cd H:/Repository/skcks.cn/gb28181-docking-platform-mvn-repo
```shell
rm -rf ./*/*/*/*/0.1.0-SNAPSHOT
rm -rf ./*/*/*/*/*/0.1.0-SNAPSHOT
```
### 打包到本地私仓
```shell
mvn deploy -s settings.xml -DaltDeploymentRepository=local-repo::default::file:H:/Repository/skcks.cn/gb28181-docking-platform-mvn-repo
```
git push 推送即可

### 公共仓库
https://central.sonatype.com

https://central.sonatype.org/publish/requirements/gpg/#signing-a-file

gpg签名
https://central.sonatype.org/publish/requirements/gpg/
https://www.gpg4win.org/thanks-for-download.html

gpg服务器
- keyserver.ubuntu.com
- keys.openpgp.org
- pgp.mit.edu

settings.xml
```xml
<settings>
	<servers>
		<server>
			<id>ossrh</id>
			<username><!-- your token username --></username>
			<password><!-- your token password --></password>
		</server>
	</servers>
    
	<profiles>
		<profile>
			<id>ossrh</id>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<properties>
				<gpg.executable>gpg</gpg.executable>
				<gpg.keyname>GPG KEY NAME</gpg.keyname>
				<gpg.passphrase>GPG KEY PASSWORD</gpg.passphrase>
			</properties>
		</profile>
	</profiles>
</settings>
```

#### 解决idea 控制台乱码

- 设置 > 构建/运行/部署 > 构建工具 > Maven > 运行程序 > VM options 
- 添加 -Dfile.encoding=GBK


## License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fshikong-sk%2Fgb28181-docking-platform.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Fshikong-sk%2Fgb28181-docking-platform?ref=badge_large)
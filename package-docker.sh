#!/bin/bash
docker run --name maven --rm \
	-v /mnt/sk-w/run/src/gb28181-docking-platform:/usr/src/mymaven \
	-v /mnt/sk-w/run/docker/maven/repository:/root/.m2/repository \
	-v /mnt/sk-w/run/docker/maven/settings.xml:/usr/share/maven/ref/settings.xml \
	-v /etc/docker/daemon.json:/etc/docker/daemon.json -v /var/run/docker.sock:/var/run/docker.sock -v /usr/bin/docker:/usr/bin/docker \
	-w /usr/src/mymaven \
	maven:3.9.3-eclipse-temurin-17 \
	mvn clean package -DskipTests -Pdocker
docker save skcks.cn/gb28181-docking-platform -o gb28181.docking.platform.image
ls -lh *.image

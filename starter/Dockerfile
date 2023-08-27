FROM eclipse-temurin:17-jre

MAINTAINER Shikong <919411476@qq.com>

ENV HOME_PATH /opt/gb28181-docking-platform/
RUN mkdir -p $HOME_PATH
WORKDIR $HOME_PATH

ADD target/starter.jar /opt/gb28181-docking-platform/starter.jar

EXPOSE 28181

ENTRYPOINT ["java", "-jar","/opt/gb28181-docking-platform/starter.jar"]

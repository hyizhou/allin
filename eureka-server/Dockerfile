# JDK 17
FROM openjdk:17
#作者
MAINTAINER hyizhou
# 声明一个挂载点，容器内此路径会对应宿主机的某个文件夹
VOLUME /app
# 应用构建成功后的jar文件被复制到镜像内，名字也改成了app.jar
ADD build/libs/eureka-server-0.0.1-SNAPSHOT.jar app.jar

# 启动容器时的进程
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8088
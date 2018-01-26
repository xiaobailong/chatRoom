# 匿名聊天室

支持使用docker部署

部署方式：

1.构建

docker build . -t chatroom

2.部署

docker run -d -p 80:8080 -p 9090:9090 --name chatroom chatroom

3.访问

本地访问地址：http://localhost/
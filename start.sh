#/bin/bash
nohup redis-server &
java -jar /usr/local/chatRoom-service/chatRoom-service-1.0.0-SNAPSHOT.jar
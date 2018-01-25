#/bin/bash
nohup redis-server &
/opt/tomcat/bin/startup.sh
java -jar /usr/local/chatRoom-service/chatRoom-service-1.0.0-SNAPSHOT.jar
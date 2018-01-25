FROM xiaobailong/oracle-java:centos7_oracleJDK8

RUN wget http://mirrors.shu.edu.cn/apache/tomcat/tomcat-8/v8.5.27/bin/apache-tomcat-8.5.27.tar.gz
RUN mkdir /opt/tomcat && tar xvf apache-tomcat-8*tar.gz -C /opt/tomcat --strip-components=1
RUN yum install -y epel-release maven
RUN yum install -y redis

COPY ./chatRoom-client /opt/tomcat/webapps/ROOT/
COPY ./chatRoom-service /data/chatRoom-service
COPY ./settings.xml /root/.m2/
COPY ./start.sh /root/
RUN cd /data/chatRoom-service && mvn package && mkdir -p /usr/local/chatRoom-service && \
    cp /data/chatRoom-service/target/chatRoom-service-1.0.0-SNAPSHOT.jar /usr/local/chatRoom-service/
RUN rm -rf /data

CMD /root/start.sh && tail -f /opt/tomcat/logs/catalina.out

docker stop chatroom && docker rm chatroom
docker build . -t chatroom
docker run -d -p 80:8080 -p 9090:9090 --name chatroom chatroom
/**
 * Copyright (c) 2017 杭州中恒云能源互联网技术有限公司
 * http://www.powercloud.cn
 * 保留所有权利
 */
package com.gaoqingyue.netty.socketio.server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author yangcz@hzzh.com
 * @since 2018-1-18
 */
public class AlarmSocketIoServer {

    private static Logger logger = LoggerFactory.getLogger(AlarmSocketIoServer.class);

    private static final String CLUSTER_REDIS_ADDRESS = "http://localhost:6379";

    private SocketIOServer server;

    private String serverName;

    private RedissonClient redisson;

    private static final String HOST_NAME = "0.0.0.0";

    private int port;

    public AlarmSocketIoServer(int port) {
        this(port, "Server-" + new Random().nextInt(Integer.MAX_VALUE));
    }

    public AlarmSocketIoServer(int port, String serverName) {
        this.port = port;
        this.serverName = serverName;
    }

    private RedissonClient createRedisson() {
        Config redisConfig = new Config();
        redisConfig.useSingleServer().setAddress(CLUSTER_REDIS_ADDRESS);
        return Redisson.create(redisConfig);
    }

    public Configuration socketIoConfig() {
        Configuration socketIoConfig = new Configuration();
        socketIoConfig.setHostname(HOST_NAME);
        socketIoConfig.setPort(port);
        socketIoConfig.setPingInterval(1000);
        socketIoConfig.setPingTimeout(1000);

        this.redisson = createRedisson();
        socketIoConfig.setStoreFactory(new RedissonStoreFactory(redisson));
        return socketIoConfig;
    }

    public void run() {

        Configuration socketIoConfig = this.socketIoConfig();

        server = new SocketIOServer(socketIoConfig);

        server.addEventListener("notification", SubscribeObject.class, new DataListener<SubscribeObject>() {

            @Override
            public void onData(SocketIOClient client, SubscribeObject subscribeObject, AckRequest ackRequest) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) client.getRemoteAddress();
                String clientIp = inetSocketAddress.getAddress().getHostAddress();
                logger.info("receive notification, channel#{},data#{}", subscribeObject.getChannel(), subscribeObject.getData());
                server.getBroadcastOperations().sendEvent(subscribeObject.getChannel(), clientIp + "@" + serverName + ": " + subscribeObject.getData());
            }
        });

        server.start();
    }

    public void sendServerInfo() {
        Socket socket = null;
        try {
            socket = IO.socket("http://" + HOST_NAME + ":" + port);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... objects) {
                    logger.info("connect...");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... objects) {
                    logger.info("disconnect...");
                }
            });
            socket.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("channel", "service_info");
            while (true) {
                Collection<SocketIOClient> socketIOClients = this.server.getAllClients();
                int size=socketIOClients.size();
                jsonObject.put("data", "Members in " + serverName + " is " + size + " .");
                socket.emit("notification", jsonObject);
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int port = 9090;
        if (args != null) {
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }
        }

        AlarmSocketIoServer launcher = new AlarmSocketIoServer(port);
        launcher.run();
        launcher.sendServerInfo();
        TimeUnit.DAYS.sleep(1);
    }

}
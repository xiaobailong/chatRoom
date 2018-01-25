/**
 * Copyright (c) 2017 杭州中恒云能源互联网技术有限公司
 * http://www.powercloud.cn
 * 保留所有权利
 */
package com.gaoqingyue.netty.socketio.client;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * @author yangcz@hzzh.com
 * @since 2018-1-23
 */
public class AlarmSocketIoClient {

    private static Logger logger = LoggerFactory.getLogger(AlarmSocketIoClient.class);

    public static void main(String[] args) throws InterruptedException {

        String url = "http://127.0.0.1:8080";
        if (args != null) {
            if (args.length >= 1) {
                url = args[0];
            }
        }

        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = false;
        opts.timeout = 10000;

        try {
            Socket socket = IO.socket(url);

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
            jsonObject.put("channel", "hzzh");
            int i = 0;
            while (true) {
                jsonObject.put("data", "中文 " + (i++));
                socket.emit("notification", jsonObject);
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

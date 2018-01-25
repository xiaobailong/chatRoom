/**
 * Copyright (c) 2017 杭州中恒云能源互联网技术有限公司
 * http://www.powercloud.cn
 * 保留所有权利
 */
package com.gaoqingyue.netty.socketio.server;

/**
 * @author yangcz@hzzh.com
 * @since 2018-1-23
 */
public class SubscribeObject {

    public SubscribeObject() {
    }

    public SubscribeObject(String channel) {
        this.channel = channel;
    }

    public SubscribeObject(String channel, String data) {
        this.channel = channel;
        this.data = data;
    }

    public SubscribeObject(String channel, String client, String data) {
        this.channel = channel;
        this.client = client;
        this.data = data;
    }

    private String channel;

    private String client;

    private String data;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" + "#channel='" + channel + '\'' + ", data='" + data + '\'' + '}';
    }
}

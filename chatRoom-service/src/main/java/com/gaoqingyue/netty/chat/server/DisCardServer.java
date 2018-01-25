/**
 * Copyright (c) 2017 杭州中恒云能源互联网技术有限公司
 * http://www.powercloud.cn
 * 保留所有权利
 */
package com.gaoqingyue.netty.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author yangcz@hzzh.com
 * @since 2018-1-15
 */
public class DisCardServer {

    private int port;

    public DisCardServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workEventLoopGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossEventLoopGroup, workEventLoopGroup)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(new ChannelInitializer<SocketChannel>() {

                         @Override
                         public void initChannel(SocketChannel ch) throws Exception {
                             ch.pipeline().addLast(new DiscardServerHandler());
                         }
                     }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收进来的连接
            ChannelFuture f = bootstrap.bind(port).sync(); // (7)

            // 等待服务器  socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();
        } finally {
            workEventLoopGroup.shutdownGracefully();
            bossEventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try {
            new DisCardServer(8080).run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

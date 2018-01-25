/**
 * Copyright (c) 2017 杭州中恒云能源互联网技术有限公司
 * http://www.powercloud.cn
 * 保留所有权利
 */
package com.gaoqingyue.netty.chat.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yangcz@hzzh.com
 * @since 2018-1-15
 */
public class SimpleChatClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        System.out.println(s);
    }
}
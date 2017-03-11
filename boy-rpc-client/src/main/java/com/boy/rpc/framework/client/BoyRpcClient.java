package com.boy.rpc.framework.client;

import com.boy.rpc.framework.common.codec.BoyRpcDecoder;
import com.boy.rpc.framework.common.codec.BoyRpcEncoder;
import com.boy.rpc.framework.common.bean.BoyRpcRequest;
import com.boy.rpc.framework.common.bean.BoyRpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wesley on 2017-03-10.
 * rpc调用客户端
 */
public class BoyRpcClient extends SimpleChannelInboundHandler<BoyRpcResponse> {

    private static final Logger logger = LoggerFactory.getLogger(BoyRpcClient.class);

    private final String host;
    private final int port;

    private BoyRpcResponse response;

    public BoyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, BoyRpcResponse response) throws Exception {
        this.response = response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("api caught exception", cause);
        ctx.close();
    }

    public BoyRpcResponse send(BoyRpcRequest request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建并初始化 Netty 客户端 Bootstrap 对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new BoyRpcEncoder(BoyRpcRequest.class)); // 编码 RPC 请求
                    pipeline.addLast(new BoyRpcDecoder(BoyRpcResponse.class)); // 解码 RPC 响应
                    pipeline.addLast(BoyRpcClient.this); // 处理 RPC 响应
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // 连接 RPC 服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();
            // 写入 RPC 请求数据并关闭连接
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
            // 返回 RPC 响应对象
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }
}
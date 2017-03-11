package com.boy.rpc.framework.server;


import com.boy.rpc.framework.registry.ServiceRegistry;
import com.boy.rpc.framework.common.bean.BoyRpcRequest;
import com.boy.rpc.framework.common.bean.BoyRpcResponse;
import com.boy.rpc.framework.common.codec.BoyRpcDecoder;
import com.boy.rpc.framework.common.codec.BoyRpcEncoder;
import com.boy.rpc.framework.common.utils.StringUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wesley on 2017-03-09.
 * rpc 服务器（用于发布 RPC 服务）
 */
public class BoyRpcServer implements ApplicationContextAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(BoyRpcServer.class);

    private String serviceAddress;

    private ServiceRegistry serviceRegistry;

    /**
     * 存放 服务名 与 服务对象 之间的映射关系
     */
    private Map<String, Object> handlerMap = new HashMap<>();

    public BoyRpcServer(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public BoyRpcServer(String serviceAddress, ServiceRegistry serviceRegistry) {
        this.serviceAddress = serviceAddress;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        // 扫描带有 RpcService 注解的类并初始化 handlerMap 对象
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(BoyRpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                BoyRpcService rpcService = serviceBean.getClass().getAnnotation(BoyRpcService.class);
                String serviceName = rpcService.value().getName();
                String serviceVersion = rpcService.version();
                if (StringUtil.isNotEmpty(serviceVersion)) {
                    serviceName += "-" + serviceVersion;
                }
                handlerMap.put(serviceName, serviceBean);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建并初始化 Netty 服务端 Bootstrap 对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new BoyRpcDecoder(BoyRpcRequest.class)); // 解码 RPC 请求
                    pipeline.addLast(new BoyRpcEncoder(BoyRpcResponse.class)); // 编码 RPC 响应
                    pipeline.addLast(new BoyRpcServerHandler(handlerMap)); // 处理 RPC 请求
                }
            });
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 获取 RPC 服务器的 IP 地址与端口号
            String[] addressArray = StringUtil.split(serviceAddress, ":");
            String ip = addressArray[0];
            int port = Integer.parseInt(addressArray[1]);
            // 启动 RPC 服务器
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            // 注册 RPC 服务地址
            if (serviceRegistry != null) {
                for (String interfaceName : handlerMap.keySet()) {
                    serviceRegistry.register(interfaceName, serviceAddress);
                    logger.debug("register service: {} => {}", interfaceName, serviceAddress);
                }
            }
            logger.debug("server started on port {}", port);
            // 关闭 RPC 服务器
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

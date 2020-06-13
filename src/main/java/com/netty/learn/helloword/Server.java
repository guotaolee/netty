package com.netty.learn.helloword;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: gtli
 * Date: 2020-05-17
 * Time: 16:00
 * Description: 服务端
 */
public class Server {

    public static void main(String[] args) throws Exception{
        EventLoopGroup pGroup = new NioEventLoopGroup();//用于处理服务器接收客户端连接的
        EventLoopGroup cGroup = new NioEventLoopGroup();//用于进行网络通信
        ServerBootstrap bootstrap = new ServerBootstrap();//创建辅助工具类，用于服务器通道的一系列配置
        bootstrap.group(pGroup,cGroup)//绑定两个线程组
                .channel(NioServerSocketChannel.class)//指定NIO的模式
                .option(ChannelOption.SO_BACKLOG, 1024)//设置TCP缓冲区,1k
                .option(ChannelOption.SO_SNDBUF, 32*1024)//设置发送缓冲大小,32k
                .option(ChannelOption.SO_RCVBUF,32*1024)//设置接收缓冲大小,32k
                .option(ChannelOption.SO_KEEPALIVE,true)//保持连接
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ServerHandler());//配置具体的接收方法的处理
                    }
                });

        ChannelFuture cf1 = bootstrap.bind(8765).sync();//端口绑定
        cf1.channel().closeFuture().sync();//等待关闭
        pGroup.shutdownGracefully();
        cGroup.shutdownGracefully();
    }
}

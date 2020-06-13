package com.netty.learn.helloword;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: gtli
 * Date: 2020-05-17
 * Time: 17:04
 * Description: 客户端
 */
public class Client {

    public static void main(String[] args) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientHandler());//处理具体逻辑的handler
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8765).sync();//连接端口8765

        channelFuture.channel().write(Unpooled.copiedBuffer("hello netty~".getBytes()));//写数据"hello netty~"

        channelFuture.channel().flush();//把数据刷新到同道中

        channelFuture.channel().closeFuture().sync();

        group.shutdownGracefully();
    }
}

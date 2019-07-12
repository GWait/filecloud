package ru.geekbrains.filecloud;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.geekbrains.filecloud.payload.AuthenticationPayload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Client {

    private EventLoopGroup workerGroup;
    private Channel channel;

    public boolean connect(String host, int port) {
        workerGroup = new NioEventLoopGroup(4);
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addFirst(new ClientHandler());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            channel = f.sync().channel();
        } catch (InterruptedException e) {
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }
        return true;
    }

    public boolean send(Packet packet) {
        try {
            channel.writeAndFlush(packet).await(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean disconnect() {
        channel.close();
        workerGroup.shutdownGracefully();
        return true;
    }

    public Packet prepareAuthPacket(String login, String password) {
        Packet packet = new Packet();
        AuthenticationPayload payload = new AuthenticationPayload(login, password);
        packet.setType(PacketType.AUTH);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(payload);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte [] payloadBuff = bos.toByteArray();
        packet.setPayload(payloadBuff);
        return packet;
    }
}

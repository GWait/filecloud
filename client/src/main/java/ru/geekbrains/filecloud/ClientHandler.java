package ru.geekbrains.filecloud;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class ClientHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Packet packet = (Packet) msg;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(packet);
        oos.flush();
        byte [] data = bos.toByteArray();
        ByteBuf payload = ctx.alloc().buffer(data.length); // (2)
        System.out.println(data.length);
        payload.writeBytes(data);
        System.out.println("Send packet : " + payload);
        ctx.writeAndFlush(payload);
    }
}

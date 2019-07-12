package ru.geekbrains.filecloud.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.geekbrains.filecloud.Packet;
import ru.geekbrains.filecloud.PacketType;
import ru.geekbrains.filecloud.payload.AuthenticationPayload;

import java.io.*;

public class PacketInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("unregistered");
        System.out.println(ctx.channel().isOpen());
        System.out.println(ctx.channel().id().asLongText());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("registered");
        System.out.println(ctx.channel().isOpen());
        System.out.println(ctx.channel().id().asLongText());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] array = new byte[buf.capacity()];
        buf.getBytes(0, array);
        Object object;
        try(ByteArrayInputStream bis = new ByteArrayInputStream(array);ObjectInput in = new ObjectInputStream(bis)) {
            object = in.readObject();
        }
        Packet packet = (Packet) object;
        System.out.println("Receive packet: " + packet);
        packetAnalyze(packet);
        ((ByteBuf) msg).release();
    }

    private void packetAnalyze(Packet packet) {
        if (packet.getType().equals(PacketType.AUTH)) {
            processAuthPacket(packet);
        }
    }

    private void processAuthPacket(Packet packet) {
        byte[] payload = packet.getPayload();
        Object object = null;
        try(ByteArrayInputStream bis = new ByteArrayInputStream(payload);ObjectInput in = new ObjectInputStream(bis)) {
            object = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        AuthenticationPayload auth = (AuthenticationPayload) object;
        auth.setToken("ABC");
        System.out.println(auth);
        convertPacketToBytes(packet);
    }

    private void convertPacketToBytes(Packet packet) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(packet);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte [] data = bos.toByteArray();
        System.out.println(data.length);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Exception: "+cause.getMessage());
        ctx.channel().close();
    }
}

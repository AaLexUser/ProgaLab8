package com.lapin.network.listener;

import com.lapin.di.context.ApplicationContext;
import com.lapin.network.log.NetworkLogOutputConsole;
import com.lapin.network.log.NetworkLogger;
import com.lapin.network.obj.NetObj;
import com.lapin.network.obj.Serializer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class ClientListener implements Listenerable{
    private final NetworkLogger netLogger;
    private final SocketChannel socketChannel;

    public ClientListener(SocketChannel socketChannel){
        netLogger = ApplicationContext.getInstance().getBean(NetworkLogger.class);
        netLogger.setLogOutput(new NetworkLogOutputConsole());
        this.socketChannel = socketChannel;
    }

    public NetObj handle(NetObj request){
        try {
            write(request);
        } catch (IOException e) {
            netLogger.error("Failed to send a request to the server");
        }
        NetObj response = null;
        try {
            response = read();
        } catch (IOException e){
            netLogger.error("Failed to get a response from the server");
        }
        return response;
    }


    public void write(NetObj request) throws IOException {
        byte[] bytes = Serializer.serialize(request);
        getOutputStream().write(bytes);
    }
    private NetObj read() throws IOException {
        ByteBuffer mainBuffer = ByteBuffer.allocate(0);
        InputStream inputStream = getInputStream();
        while (true) {
            byte[] bytesToDeserialize = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int bytesCount = bis.read(bytesToDeserialize);
            ByteBuffer newBuffer = ByteBuffer.allocate(mainBuffer.capacity() + bytesCount);
            newBuffer.put(mainBuffer);
            newBuffer.put(ByteBuffer.wrap(bytesToDeserialize, 0, bytesCount));
            mainBuffer = ByteBuffer.wrap(newBuffer.array());
            NetObj response = (NetObj) Serializer.deserialize(mainBuffer.array());
            if (response == null) {
                List<ByteBuffer> buffers = new ArrayList<>();
                int bytesLeft = bis.available();
                int len = bytesLeft;
                while (bytesLeft > 0) {
                    byte[] leftBytesToSerialize = new byte[bytesLeft];
                    bis.read(leftBytesToSerialize);
                    buffers.add(ByteBuffer.wrap(leftBytesToSerialize));
                    bytesLeft = bis.available();
                    len += bytesLeft;
                }
                newBuffer = ByteBuffer.allocate(len + mainBuffer.capacity());
                newBuffer.put(mainBuffer);
                buffers.forEach(newBuffer::put);
                mainBuffer = ByteBuffer.wrap(newBuffer.array());
                response = (NetObj) Serializer.deserialize(mainBuffer.array());
            }
            if (response != null) {
                return response;
            }
            else throw new IOException();
        }
    }
    private OutputStream getOutputStream(){
        try {
            return socketChannel.socket().getOutputStream();
        } catch (IOException e) {
            netLogger.error("Failed to open OutputStream");
        }
        return null;
    }
    private InputStream getInputStream(){
        try {
            return socketChannel.socket().getInputStream();
        } catch (IOException e) {
            netLogger.error("Failed to open InputStream");
        }
        return null;
    }
}

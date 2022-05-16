package com.lapin.network.listener;

import com.lapin.network.State;
import com.lapin.network.config.NetworkConfigurator;
import com.lapin.network.log.NetworkLogger;
import com.lapin.network.obj.NetObj;
import com.lapin.network.obj.Serializer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

public class ServerListener implements Listenerable{
    protected ServerSocketChannel ssc;
    protected NetworkLogger netLogger;
    private static final int BUFFER_SIZE = 1024;
    protected NetworkConfigurator config;
    protected final Map<SocketChannel, ByteBuffer> channelBuffer = new HashMap<>();
    protected Selector sel;
    protected SelectionKey key;

    public ServerListener(NetworkConfigurator config, ServerSocketChannel ssc){
        this.config = config;
        this.netLogger=config.getNetLogger();
        this.ssc = ssc;
        try {
            sel = Selector.open();
        } catch (IOException e) {
            netLogger.error("Failed to open selector!");
        }
        try {
            ssc.configureBlocking(false);
        } catch (IOException e) {
            netLogger.error("Non-blocking mode not available!");
        }
        try {
            key = ssc.register(sel, OP_ACCEPT);
            netLogger.info("Server is ready to accept");
        } catch (ClosedChannelException e) {
            netLogger.error("Failed key registration");
        }
        startUp();
    }
    @Override
    public void startUp() {
        while (State.getPS()) {
            try {
                int numOfKeys = sel.select();
                if (numOfKeys == 0){
                    continue;
                }
                Set<SelectionKey> keys = sel.selectedKeys();
                for (var it = keys.iterator(); it.hasNext(); ) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            accept();
                        } else if (key.isReadable()) {
                            read();
                        } else if (key.isWritable()) {
                            write();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void accept() throws IOException {
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        sc.register(sel, SelectionKey.OP_READ);
        channelBuffer.put(sc,ByteBuffer.allocate(0));
        netLogger.info("New session: " + sc.socket().getRemoteSocketAddress());
    }
    protected SocketAddress kill(SocketChannel channel) throws IOException {
        SocketAddress address = channel.getRemoteAddress();
        netLogger.info("Session: " + address + " closed");
        channelBuffer.remove(channel);
        channel.close();
        return address;
    }

    protected void read(){
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                kill(channel);
            }
            ByteBuffer newBuffer = ByteBuffer.allocate(channelBuffer.get(channel).capacity() + bytesRead);
            newBuffer.put(channelBuffer.get(channel).array());
            newBuffer.put(ByteBuffer.wrap(buffer.array(), 0, bytesRead));
            channelBuffer.put(channel, newBuffer);
            NetObj request = (NetObj) Serializer.deserialize(channelBuffer.get(channel).array());
            channelBuffer.put(channel,ByteBuffer.wrap(Serializer.serialize(config.getRequestHandler().handle(request))));
            channel.register(sel, SelectionKey.OP_WRITE);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    protected void write(){
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = channelBuffer.get(channel);
            int responseLen = 0;
            int bytesWritten = channel.write(buffer);
            responseLen += bytesWritten;
            while (buffer.hasRemaining()) {
                bytesWritten = channel.write(buffer);
                responseLen += bytesWritten;
            }
            channelBuffer.put(channel, ByteBuffer.allocate(0));
            channel.register(sel, SelectionKey.OP_READ);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    };
}

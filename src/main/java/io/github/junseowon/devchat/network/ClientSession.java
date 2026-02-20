package io.github.junseowon.devchat.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class ClientSession {
    private final AsynchronousSocketChannel socketChannel;
    private final SessionManager sessionManager;

    public ClientSession(AsynchronousSocketChannel socketChannel, SessionManager sessionManager) {
        this.socketChannel = socketChannel;
        this.sessionManager = sessionManager;
        receive();
    }

    private void receive() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (result == -1) {
                    disconnect();
                    return;
                }
                attachment.flip();
                String data = StandardCharsets.UTF_8.decode(attachment).toString();
                System.out.println("[RECV] " + data);

                sessionManager.broadcast(data);

                ByteBuffer nextBuffer = ByteBuffer.allocate(1024);
                socketChannel.read(nextBuffer, nextBuffer, this);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                disconnect();
            }
        });
    }

    public void send(String data) {
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(data);
        socketChannel.write(byteBuffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer result, Void attachment) {}

            @Override
            public void failed(Throwable exc, Void attachment) {
                disconnect();
            }
        });
    }

    private void disconnect() {
        try {
            sessionManager.removeSession(this);
            if (socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
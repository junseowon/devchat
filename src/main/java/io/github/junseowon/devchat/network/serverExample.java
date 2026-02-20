package io.github.junseowon.devchat.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class serverExample {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    AsynchronousChannelGroup channelGroup;
    AsynchronousServerSocketChannel serverSocketChannel;
    List<Client> connections = new Vector<Client>();

    void startServer() {
        try {
            channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());

            serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
            serverSocketChannel.bind(new InetSocketAddress(5001));
        } catch (Exception e) {
            if (serverSocketChannel.isOpen()) {
                stopServer();
            }
            return;
        }

        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
                try {
                    String message = "[연결 수락: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + "]";

                } catch (IOException e) {
                    logger.error("에러가 발생했습니다: {}", e.getMessage(), e);
                }

                Client client = new Client(socketChannel);
                connections.add(client);

                serverSocketChannel.accept(null, this);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                if (serverSocketChannel.isOpen()) {
                    stopServer();
                }
            }
        });
    }

    void stopServer() {
        try {
            connections.clear();
            if (channelGroup != null && !channelGroup.isShutdown()) {
                channelGroup.shutdownNow();
            }
        } catch (Exception e) {
            logger.error("에러가 발생했습니다: {}", e.getMessage(), e);
        }
    }

    class Client {
        AsynchronousSocketChannel socketChannel;

        Client(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
            receive();
        }

        void receive() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
            socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    try {
                        String message = "[요청 처리: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + "]";

                        attachment.flip();
                        Charset charset = Charset.forName("UTF-8");
                        String data = charset.decode(attachment).toString();

                        for (Client client : connections) {
                            client.send(data);
                        }

                        ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
                        socketChannel.read(byteBuffer, byteBuffer, this);
                    } catch (Exception e) {
                        logger.error("에러가 발생했습니다: {}", e.getMessage(), e);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        String message = "[클라이언트 통신 안 됨: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + "]";
                        connections.remove(Client.this);
                        socketChannel.close();
                    } catch (IOException e) {
                        logger.error("에러가 발생했습니다: {}", e.getMessage(), e);
                    }
                }
            });
        }

        void send(String data) {

            Charset charset = Charset.forName("UTF-8");
            ByteBuffer byteBuffer = charset.encode(data);

            socketChannel.write(byteBuffer, null, new CompletionHandler<Integer, Void>() {

                @Override
                public void completed(Integer result, Void attachment) {

                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    try {
                        String message = "[클라이언트 통신 안 됨: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + "]";
                        connections.remove(Client.this);
                        socketChannel.close();
                    } catch (IOException e) {
                        logger.error("에러가 발생했습니다: {}", e.getMessage(), e);
                    }
                }
            });
        }
    }
}

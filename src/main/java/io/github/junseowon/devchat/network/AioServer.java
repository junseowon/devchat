package io.github.junseowon.devchat.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

public class AioServer {
    private AsynchronousChannelGroup channelGroup;
    private AsynchronousServerSocketChannel serverSocketChannel;
    private final SessionManager sessionManager;

    public AioServer() {
        this.sessionManager = new SessionManager();
    }

    public void start(int port) {
        try {
            channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
                    Runtime.getRuntime().availableProcessors(),
                    Executors.defaultThreadFactory()
            );

            serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("[SERVER] Listening on port " + port + "...");

            acceptClients();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptClients() {
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
                try {
                    System.out.println("[ACCEPT] Client connected from: " + socketChannel.getRemoteAddress());

                    ClientSession newSession = new ClientSession(socketChannel, sessionManager);
                    sessionManager.addSession(newSession);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverSocketChannel.accept(null, this);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.err.println("[ERROR] Server accept failed: " + exc.getMessage());
                stop();
            }
        });
    }

    public void stop() {
        try {
            sessionManager.clearAll();
            if (channelGroup != null && !channelGroup.isShutdown()) {
                channelGroup.shutdownNow();
            }
            System.out.println("[SERVER] Stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
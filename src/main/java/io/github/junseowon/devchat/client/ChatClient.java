package io.github.junseowon.devchat.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ChatClient {

    public static void main(String[] args) {
        try {
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();

            socketChannel.connect(new InetSocketAddress("localhost", 5001)).get();
            System.out.println("[INFO] Connected to server. Start typing your message!");

            receiveMessages(socketChannel);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("/quit")) {
                    break;
                }

                ByteBuffer buffer = StandardCharsets.UTF_8.encode(message);
                socketChannel.write(buffer).get();
            }

            socketChannel.close();
            System.out.println("[INFO] Chat ended.");

        } catch (IOException | InterruptedException | ExecutionException e) {
            System.err.println("[ERROR] Failed to connect to server.");
            e.printStackTrace();
        }
    }

    private static void receiveMessages(AsynchronousSocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (result == -1) {
                    System.out.println("[WARN] Disconnected from server.");
                    return;
                }

                attachment.flip();
                String receivedData = StandardCharsets.UTF_8.decode(attachment).toString();
                System.out.println(">> " + receivedData);

                ByteBuffer nextBuffer = ByteBuffer.allocate(1024);
                socketChannel.read(nextBuffer, nextBuffer, this);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.err.println("[ERROR] Failed to receive message.");
            }
        });
    }
}
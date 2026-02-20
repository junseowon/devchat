package io.github.junseowon.devchat;

import io.github.junseowon.devchat.network.AioServer;
import java.io.IOException;

public class ServerApplication {

    public static void main(String[] args) {
        int port = 5001;
        AioServer server = new AioServer();

        System.out.println("======================================");
        System.out.println("Starting DevChat AIO Server...");
        System.out.println("======================================");

        server.start(port);

        System.out.println("\n[INFO] Press Enter in the console to stop the server gracefully.\n");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.stop();
        System.out.println("[INFO] Server has been shut down successfully.");
    }
}
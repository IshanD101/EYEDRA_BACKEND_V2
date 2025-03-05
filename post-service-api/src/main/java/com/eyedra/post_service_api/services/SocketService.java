package com.eyedra.post_service_api.services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService {
    private ServerSocket serverSocket;

    public SocketService(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() {
        System.out.println("Socket server started...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                // Handle client connection in a separate thread or method
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}

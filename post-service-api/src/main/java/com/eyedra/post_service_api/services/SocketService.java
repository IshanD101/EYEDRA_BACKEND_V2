package com.eyedra.post_service_api.services;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SocketService {

    private ServerSocket serverSocket;
    private final ExecutorService clientPool;
    private volatile boolean running = true;

    public SocketService() throws IOException {
        int socketPort = 5001; // Change if needed
        this.serverSocket = new ServerSocket(socketPort);
        this.clientPool = Executors.newFixedThreadPool(10); // Adjust thread pool size

        System.out.println("Socket server initialized on port: " + socketPort);
    }

    public void start() {
        System.out.println("Socket server started on port: " + serverSocket.getLocalPort());

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientPool.execute(() -> handleClient(clientSocket));
            } catch (IOException e) {
                if (!running) {
                    System.out.println("Server shutting down...");
                } else {
                    e.printStackTrace();
                }
            }
        }

        stop();
    }

    private void handleClient(Socket clientSocket) {
        try {
            System.out.println("New client connected: " + clientSocket.getInetAddress());
            // Implement client request handling logic here
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            clientPool.shutdown();
            System.out.println("Socket server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

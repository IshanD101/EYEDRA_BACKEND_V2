package com.eyedra.post_service_api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class SocketService {

    private ServerSocket serverSocket;
    private final ExecutorService clientPool;
    private volatile boolean running = true;

    @Value("${socket.port:5001}")
    private int socketPort;

    private int threadPoolSize = 10;

    public SocketService() {
        this.clientPool = Executors.newFixedThreadPool(threadPoolSize);  // Initialize the thread pool
    }

    @PostConstruct
    public void init() {
        try {
            this.serverSocket = new ServerSocket(socketPort);  // Initialize the ServerSocket
            System.out.println("Socket server initialized on port: " + socketPort);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing server socket", e);
        }
    }

    public void start() {
        System.out.println("Socket server started on port: " + serverSocket.getLocalPort());

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();  // Accept new client connections
                clientPool.execute(() -> handleClient(clientSocket));  // Handle each client in a separate thread
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
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            System.out.println("New client connected: " + clientSocket.getInetAddress());

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Received from client: " + clientMessage);
                out.println("Message received: " + clientMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();  // Close client connection after handling
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();  // Close the server socket
            }
            clientPool.shutdown();  // Shutdown the client pool
            if (!clientPool.awaitTermination(60, TimeUnit.SECONDS)) {
                clientPool.shutdownNow();  // Force shutdown if tasks don't finish in time
            }
            System.out.println("Socket server stopped.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

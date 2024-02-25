package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private HashMap<String, ChatClient> clients;
    private boolean isRunning = true;


    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            executorService = Executors.newFixedThreadPool(availableProcessors);
            clients = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        addShutdownHook();
        System.out.println("Servidor encendido. Escuchando al puerto " + serverSocket.getLocalPort());

        try {
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                if (isRunning) {
                    handleClientConnection(clientSocket);
                } else {
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            if (isRunning) {
                e.printStackTrace();
            }
        }
    }

    private void handleClientConnection(Socket clientSocket) {
        if (isRunning) {
            ChatClientHandler clientHandler = new ChatClientHandler(clientSocket, this);
            executorService.submit(clientHandler);
        } else {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addClient(String username, ChatClient client) {
        clients.put(username, client);
        System.out.println("Usuario: " + username + ". Conectado! Bienvenido ala Chat ");
    }

    public synchronized void removeClient(String username) {
        clients.remove(username);
        System.out.println("Client disconnected: " + username + ". Total clients: " + clients.size());
    }

    public void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            broadcastMessage();
            closeServer();

            System.out.println("Server shutdown complete.");
        }));
    }

    private void closeServer() {
        try {
            isRunning = false;
            serverSocket.close();
            executorService.shutdown();
            executorService.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized ChatClient getClient(String username) {
        return clients.get(username);
    }

    public Map<String, ChatClient> getClients() {
        return clients;
    }
    private void broadcastMessage() {
        this.getClients().values().forEach(client -> {
            client.sendMessage("El servidor se va a cerrar. Bye Bye!");
        });
    }
}

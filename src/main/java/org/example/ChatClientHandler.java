package org.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientHandler implements Runnable {
    private Socket clientSocket;
    private ChatServer chatServer;
    private PrintWriter wr;
    private BufferedReader br;
    private String username;

    public ChatClientHandler(Socket clientSocket, ChatServer chatServer) {
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;
    }

    @Override
    public void run() {
        try {
            wr = new PrintWriter(clientSocket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            connectClient();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disconnectClient();
        }
    }

    private void connectClient() throws IOException {
        wr.println("Bienvenido al chat de Batoi. introduce tu nombre de usuario: ");
        username = br.readLine();

        ChatClient chatClient = new ChatClient(username, wr);
        chatServer.addClient(username, chatClient);

        ChatClient storedClient = chatServer.getClient(username);
        if (storedClient != null) {
            storedClient.sendQueuedMessages();
        }
        String message;
        while ((message = br.readLine()) != null) {
            if ("bye".equalsIgnoreCase(message)) {
                break;
            }
            sendClientsMessage(username + ": " + message);
        }
    }

    private void disconnectClient() {
        sendClientsMessage(username + ": Nos vemos! Tenog que irme");
        chatServer.removeClient(username);
        try {
            wr.close();
            br.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendClientsMessage(String message) {
        chatServer.getClients().values().forEach(client -> {
            if (!client.getUsername().equals(username)) {
                client.sendMessage(message);
            }
        });
    }
}

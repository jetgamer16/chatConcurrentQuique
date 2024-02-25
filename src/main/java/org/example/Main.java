package org.example;

public class Main {
    public static void main(String[] args) {
        int port = 21010;
        ChatServer chatServer = new ChatServer(port);

        Thread serverThread = new Thread(chatServer::start);
        serverThread.start();
    }

}
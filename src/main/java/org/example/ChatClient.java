package org.example;

import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatClient {
    private String username;
    private PrintWriter writer;
    private Queue<String> messageQueue;

    public ChatClient(String username, PrintWriter writer) {
        this.username = username;
        this.writer = writer;
        this.messageQueue = new LinkedBlockingQueue<>();
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public void sendQueuedMessages() {
        while (!messageQueue.isEmpty()) {
            sendMessage(messageQueue.poll());
        }
    }
}

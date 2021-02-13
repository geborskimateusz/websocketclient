package com.geborskimateusz.websocketclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

import java.util.Scanner;

public class WebsocketclientApplication {

    private static String URL = "ws://localhost:8080/ws/1";

    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        WebSocket ws = HttpClient
                .newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(URL), new WebSocketClient(latch))
                .join();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter message!");
        String question = sc.next();

        while (!question.equals("exit")) {
            ws.sendText(question, true);
            latch.await();
            question = sc.next();
        }

        ws.sendClose(200, "Closing socket");
    }
}




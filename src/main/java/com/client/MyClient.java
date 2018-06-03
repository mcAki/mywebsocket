package com.client;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2018/5/31.
 */
public class MyClient {

    public final static Logger LOGGER = LoggerFactory.getLogger(MyClient.class.getName());

    public static WebSocketClient webSocketClient;

    public static void main(String[] args) {
        try {
            webSocketClient = new WebSocketClient(new URI("ws://localhost:9090/websocket"), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    LOGGER.info("Connection opened.");
                }

                @Override
                public void onMessage(String message) {
                    LOGGER.info("Message received: {}", message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    LOGGER.info("Connection closed.");
                }

                @Override
                public void onError(Exception ex) {
                    LOGGER.error("Encounter an issue, caused by: {}", ex.getMessage());
                }
            };
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }

        webSocketClient.connect();
        LOGGER.info(String.valueOf(webSocketClient.getDraft()));

        while(!webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
//            LOGGER.error("WebSocket doesn't open");
        }
        try{
            send("hello world".getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
            webSocketClient.close();
        }

    }

    public static void send(byte[] bytes) {
        webSocketClient.send(bytes);
    }
}

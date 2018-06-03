package com.client;

import org.java_websocket.WebSocket;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2018/6/2.
 */
public class TestNewClient {

    public static void main(String[] args) {
        try {
            NewClient client = new NewClient(new URI("ws://localhost:9090/websocket"));
            client.connect();
            while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
//            LOGGER.error("WebSocket doesn't open");
            }
            client.send("test2".getBytes("utf-8"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

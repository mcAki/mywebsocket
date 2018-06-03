package com.client;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * Created by Administrator on 2018/6/1.
 */
public class NewClient extends WebSocketClient{

    public final static Logger LOGGER = LoggerFactory.getLogger(NewClient.class.getName());

    public NewClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.info("Connection opened.");
        LOGGER.info(handshakedata.getHttpStatusMessage());
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

}

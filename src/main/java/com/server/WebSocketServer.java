package com.server;

import com.config.GetHttpSessionConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2018/5/30.
 */
@ServerEndpoint(value = "/websocket", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class.getName());

    /**
     * number of connected client
     */
    private static int onlineCouot = 0;

    /**
     * concurrent set, store websocket clients
     */
    private static CopyOnWriteArrayList<WebSocketServer> webSocketSet = new CopyOnWriteArrayList<>();

    /**
     * Session
     */
    private Session session;

    public WebSocketServer() {
        LOGGER.info("Initialize Web Socket Server!");
    }

    @OnOpen
    public void onOpen(Session session,EndpointConfig config) {
        this.session = session;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        LOGGER.info("name is: {}", httpSession.getAttribute("name"));
        LOGGER.info("session is: {}", session.getId());
        webSocketSet.add(this);
        addOnlineCount();
        try{
            sendMessage("welcome ");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void sendMessage(String msg) throws IOException {
        this.session.getAsyncRemote().sendText(msg);
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        LOGGER.info("A msg from client: {}", msg);
        try {
            broadcastInfo(msg);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @OnError
    public void onError(Session session, Throwable e) {
        LOGGER.error(e.getMessage());
    }

    /**
     * broadcast
     * @param msg content
     * @throws IOException
     */
    public static void broadcastInfo(String msg) throws  IOException {
        for (WebSocketServer server: webSocketSet) {
            try {
                server.sendMessage(msg);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCouot() {
        return onlineCouot;
    }

    public static synchronized void addOnlineCount() {
        onlineCouot++;
        LOGGER.info("A new connection joined, number of onlineCount is {}", getOnlineCouot());
    }

    public static synchronized  void subOnlineCount() {
        onlineCouot--;
        LOGGER.info("A new connection joined, number of onlineCount is {}", getOnlineCouot());
        if (onlineCouot < 0) {
            LOGGER.info("A new connection joined, number of onlineCount is less than 0, please check");
            onlineCouot = 0;
        }
    }
}

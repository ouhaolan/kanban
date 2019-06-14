package com.xinhua.kanban.websocket.kanbanqrcode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 看板二维码 socket
 */
@Slf4j
@Component
@EnableWebSocket
@ServerEndpoint(value = "/KanbanQrcodeSocket")
public class KanbanQrcodeSocket {

    /*连接对象*/
    private static CopyOnWriteArraySet<KanbanQrcodeSocket> kanbanContrSocket = new CopyOnWriteArraySet<>();

    /*session 会话*/
    private Session session;

    /**
     * 连接成功
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        kanbanContrSocket.add(this);     //加入set中
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        kanbanContrSocket.remove(this);   //连接关闭后，将此websocket从set中删除
    }

    /**
     * 接收客户端推送的消息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //log.debug("来自客户端的消息:" + message);
    }

    /**
     * 发生错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 向客户端发送消息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);

    }

    /**
     * 获取连接对象
     *
     * @return
     */
    public static synchronized CopyOnWriteArraySet<KanbanQrcodeSocket> getWebSocketSet() {
        return kanbanContrSocket;
    }

}

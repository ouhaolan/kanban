package com.xinhua.kanban.kanbancontr.service;

import com.xinhua.kanban.websocket.kanbancontr.KanbanContrSocket;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Service("kanbanService")
public class KanbanService {

    /**
     * 推送给看板
     * @param type
     * @param modular
     * @param url
     */
    public void handoffPage(String type,String modular,String url) {
        CopyOnWriteArraySet<KanbanContrSocket> kanbanContrSocket =
                KanbanContrSocket.getWebSocketSet();
        String str = "{\"type\":\""+type+"\",\"modular\":\""+modular+"\",\"url\":\""+url+"\"}";
        kanbanContrSocket.forEach(c->{
            try {
                c.sendMessage(str);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }
}

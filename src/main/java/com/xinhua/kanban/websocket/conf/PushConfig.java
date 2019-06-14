package com.xinhua.kanban.websocket.conf;

import com.xinhua.kanban.qrcode.service.QRCodeService;
import com.xinhua.kanban.websocket.kanbanqrcode.KanbanQrcodeSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 任务调度
 */
@Component
@EnableScheduling
public class PushConfig {

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Autowired
    private QRCodeService qRCodeService;

    /**
     * 任务调度：每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void test() {
        CopyOnWriteArraySet<KanbanQrcodeSocket> webSocketSet =
                KanbanQrcodeSocket.getWebSocketSet();

        String qrCodeBase64 =
                qRCodeService.kanbanQRCode("http://" + host + ":" + port + "/view/kanbancontr/kanbancontr.html");

        webSocketSet.forEach(c -> {
            try {
                c.sendMessage(qrCodeBase64);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

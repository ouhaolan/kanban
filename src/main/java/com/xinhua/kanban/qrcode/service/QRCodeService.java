package com.xinhua.kanban.qrcode.service;

import com.xinhua.kanban.kanbancontr.controller.KanBanController;
import com.xinhua.kanban.util.QRCodeUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class QRCodeService {

    /**
     * 生成看板控制器二维码
     * @param url
     * @return
     */
    public String kanbanQRCode(String url) {
        String uuid = UUID.randomUUID().toString();
        KanBanController.qrCodeUuid=uuid;
        return QRCodeUtil.strQrcode(url + "?" + uuid);
    }
}

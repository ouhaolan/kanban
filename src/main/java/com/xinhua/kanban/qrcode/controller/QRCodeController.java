package com.xinhua.kanban.qrcode.controller;

import com.xinhua.kanban.qrcode.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * 移动终端二维码
 */
@Controller
@RequestMapping("/kanbancontr/QRCodeController")
public class QRCodeController {

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Autowired
    private QRCodeService qRCodeService;

    /**
     * 生成移动终端二维码
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("kanbanQRCode")
    public HashMap kanbanQRCode() {
        HashMap map = new HashMap();

        String qrCodeBase64 =
                qRCodeService.kanbanQRCode("http://" + host + ":" + port + "/view/kanbancontr/kanbancontr.html");
        map.put("data", qrCodeBase64);
        return map;
    }
}

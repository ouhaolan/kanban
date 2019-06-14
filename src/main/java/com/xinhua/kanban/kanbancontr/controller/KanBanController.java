package com.xinhua.kanban.kanbancontr.controller;

import com.xinhua.kanban.kanbancontr.service.KanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * 看板移动终端控制器
 */
@Controller
@RequestMapping("/kanbancontr/KanBanController")
public class KanBanController {

    /*二维码上的uuid*/
    public static String qrCodeUuid;

    /*已登录的用户*/
    public static String loginUuid;


    @Autowired
    private KanbanService kanbanService;

    /**
     * 遥控器控制看板页面跳转
     *
     * @param url
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping("handoffPage")
    public HashMap handoffPage(@RequestParam String type, @RequestParam String modular, @RequestParam String url, @RequestParam String uuid) {
        HashMap map = new HashMap();
        if (!uuid.equals(KanBanController.loginUuid) || uuid == null) {
            map.put("code", 404);
            return map;
        }

        try {
            kanbanService.handoffPage(type, modular, url);
            map.put("code", 200);
        } catch (Exception e) {
            map.put("code", 500);
            map.put("mes", "系统内部异常，请联系开发人员进行处理！");
        }
        return map;
    }

    /**
     * 遥控器扫码登录
     *
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping("loginKanban")
    public HashMap loginKanban(@RequestParam String uuid) {
        HashMap map = new HashMap();
        if (uuid.equals(KanBanController.qrCodeUuid)) {
            KanBanController.loginUuid = uuid;
            kanbanService.handoffPage("refresh", null, null);
            map.put("code", 200);
        } else if (uuid.equals(KanBanController.loginUuid)) {
            kanbanService.handoffPage("refresh", null, null);
            map.put("code", 200);
        } else {
            map.put("code", 404);
        }
        return map;
    }

}

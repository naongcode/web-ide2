package com.example.myapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    // 루트 경로에 대한 요청을 처리하여 외부 URL로 리다이렉트
    @RequestMapping("/")
    public String redirectToExternal() {
        return "redirect:https://front-web-ide.vercel.app";
    }
}

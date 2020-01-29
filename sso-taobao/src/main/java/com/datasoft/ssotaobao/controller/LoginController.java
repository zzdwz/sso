package com.datasoft.ssotaobao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/taobao")
    public String index(){
      return "taobao";
    }
}

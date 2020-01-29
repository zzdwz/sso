package com.datasoft.ssotmall.controller;

import com.datasoft.ssotmall.utils.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping("/tmall")
    public String index(HttpServletRequest request, Model model){
        model.addAttribute("logoutUrl", SSOClientUtil.getServerLogoutUrl(request));
      return "tmall";
    }

    @RequestMapping("/logout")
    @ResponseBody
    public void logout(HttpSession session){
        System.out.println("局部注销,jessionid:" + session.getId());
        session.invalidate();
    }
}

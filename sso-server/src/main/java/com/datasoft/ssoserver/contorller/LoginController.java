package com.datasoft.ssoserver.contorller;

import com.datasoft.ssoserver.db.MemDB;
import com.datasoft.ssoserver.vo.VoClientInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class LoginController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
        public String login(String userName, String pwd, String redirectUrl, HttpSession session, Model model, HttpServletResponse response, RedirectAttributes ra ) throws IOException {
            if ("admin".equals(userName) && "123456".equals(pwd)){
                String token = UUID.randomUUID().toString();
                MemDB.T_TOKEN.add(token);
                session.setAttribute("token", token);
                System.out.println("login SessionId:" + redirectUrl + "=>" + session.getId());
                ra.addAttribute("token", token);
                return "redirect:" + redirectUrl;
            }

            model.addAttribute("redirectUrl", redirectUrl);
            return "index";

    }

    @RequestMapping("/checkLogin")
    public String checkLogin(String redirectUrl, HttpSession session, Model model, RedirectAttributes ra ){
        System.out.println("checkLogin SessionId:" + redirectUrl + "=>" + session.getId());
        String token = (String) session.getAttribute("token");
        if (StringUtils.isEmpty(token)){
            model.addAttribute("redirectUrl", redirectUrl);
            return "index";
        }else {
            ra.addAttribute("token", token);
            return "redirect:" + redirectUrl;
        }
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token, String logoutUrl, String jsessionid){
        if (MemDB.T_TOKEN.contains(token)){
            List<VoClientInfo> clientInfos = MemDB.T_CLIENTINFO.get(token);
            if (clientInfos == null){
                clientInfos = new ArrayList<VoClientInfo>();
                MemDB.T_CLIENTINFO.put(token, clientInfos);
             }
            VoClientInfo clientInfo = new VoClientInfo();
            clientInfo.setLogoutUrl(logoutUrl);
            clientInfo.setJsessionid(jsessionid);
            clientInfos.add(clientInfo);
            return "true";
        }else{
            return "false";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session, String redirectUrl, Model model){
        session.invalidate();
        model.addAttribute("redirectUrl", redirectUrl);
        return "index";
    }
}

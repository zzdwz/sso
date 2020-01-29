package com.datasoft.ssoserver.config;



import com.datasoft.ssoserver.db.MemDB;
import com.datasoft.ssoserver.util.HttpUtil;
import com.datasoft.ssoserver.vo.VoClientInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

@Component
public class LogoutSessionListener implements HttpSessionListener {
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("认证中心全局注销");
        HttpSession session = se.getSession();
        String token = (String) session.getAttribute("token");
        if (!StringUtils.isEmpty(token)){
            MemDB.T_TOKEN.remove(token);
            List<VoClientInfo> clientInfos = MemDB.T_CLIENTINFO.remove(token);
            if (null != clientInfos && clientInfos.size() > 0){
                for(VoClientInfo clientInfo: clientInfos){
                    try {
                        System.out.println("通知客户端注销,注销地址:" + clientInfo.getLogoutUrl() + " 注销的jsessionid:" + clientInfo.getJsessionid());
                        HttpUtil.sendDestorySessionRequest(clientInfo.getLogoutUrl(), clientInfo.getJsessionid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }


    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("有新用户连接");
    }
}

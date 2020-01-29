package com.datasoft.ssotmall.interceptor;


import com.datasoft.ssotmall.utils.HttpUtil;
import com.datasoft.ssotmall.utils.SSOClientUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");
        if (isLogin != null && isLogin){
            return true;
        }


        String token = request.getParameter("token");
        if (!StringUtils.isEmpty(token)){
            String httpUrl = SSOClientUtil.SERVER_URL_PREFIX + "/verify";
            Map<String, String> params = new HashMap<String, String>();
            params.put("token", token);
            params.put("logoutUrl", SSOClientUtil.getClientLogoutUrl());
            params.put("jsessionid", session.getId());
            String result = HttpUtil.sendHttpRequest(httpUrl, params);
            if ("true".equals(result)){
                session.setAttribute("isLogin", true);
                return true;
            }
        }

        SSOClientUtil.redirectSSOUrl(request, response);
        return false;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

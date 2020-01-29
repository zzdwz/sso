package com.datasoft.ssotaobao.utils;

import com.sun.deploy.net.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class SSOClientUtil {
    private static Properties ssoProperties = new Properties();
    public static String SERVER_URL_PREFIX;
    public static String CLIENT_HOST_URL;
    static {
        try {
            ssoProperties.load(SSOClientUtil.class.getClassLoader().getResourceAsStream("sso.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SERVER_URL_PREFIX = ssoProperties.getProperty("server_url_prefix");
        CLIENT_HOST_URL = ssoProperties.getProperty("client_host_url");
    }

    public static String getRedirectUrl(HttpServletRequest request) {
        return CLIENT_HOST_URL + request.getServletPath();
    }

    public static void redirectSSOUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUrl = getRedirectUrl(request);
        StringBuffer sb = new StringBuffer();
        sb.append(SERVER_URL_PREFIX)
                .append("/checkLogin?redirectUrl=")
                    .append(redirectUrl);
        System.out.println("taobao redirectSSOUrl:" + sb.toString());
        response.sendRedirect(sb.toString());
    }

    public static String getServerLogoutUrl(HttpServletRequest request){
        return SERVER_URL_PREFIX + "/logout?redirectUrl=" + CLIENT_HOST_URL + request.getServletPath();
    }

    public static String getClientLogoutUrl(){
        return CLIENT_HOST_URL + "/logout";
    }

}

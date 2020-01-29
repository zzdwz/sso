package com.datasoft.ssoserver.util;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static void sendDestorySessionRequest(String logoutUrl, String jsessionid) throws Exception {
        URL url = new URL(logoutUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("Cookie", "JSESSIONID=" + jsessionid);
        conn.connect();
        conn.getInputStream();
        conn.disconnect();
    }
}

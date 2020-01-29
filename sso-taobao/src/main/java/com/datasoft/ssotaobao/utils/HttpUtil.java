package com.datasoft.ssotaobao.utils;



import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpUtil {
    public static String sendHttpRequest(String httpUrl, Map<String, String> map) throws Exception {
        URL url = new URL(httpUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        StringBuffer sb = new StringBuffer();
        if (map != null && map.size() > 0){
            Set<String> keys = map.keySet();
            for (String key: keys){
                sb.append("&").append(key).append("=").append(map.get(key));
            }
            httpURLConnection.getOutputStream().write(sb.substring(1).getBytes("UTF-8"));
        }
        httpURLConnection.connect();
        String reponse = StreamUtils.copyToString(httpURLConnection.getInputStream(), Charset.forName("UTF-8"));
        return reponse;
    }

    public static void main(String[] args) throws Exception {
        Map map = new HashMap();
        map.put("tel", "18013881356");
        String req = HttpUtil.sendHttpRequest("http://tcc.taobao.com/cc/json/mobile_tel_segment.htm", map);
        System.out.println("数据返回:"+req);

    }
}

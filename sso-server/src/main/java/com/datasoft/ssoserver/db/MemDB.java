package com.datasoft.ssoserver.db;

import com.datasoft.ssoserver.vo.VoClientInfo;

import java.util.*;

public class MemDB {
    public static Set<String> T_TOKEN = new HashSet<>();
    public static Map<String, List<VoClientInfo>> T_CLIENTINFO = new HashMap();
}

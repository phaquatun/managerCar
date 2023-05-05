package com.mycompany.managercar;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HandlerCookie {

    private String userName, tokenRandom, domain;
    private RoutingContext ctx;
    private String cookieMedium;
    private int limitCookie;

    private Map<String, String> mapCookieCreate = new HashMap<>();

    public HandlerCookie(RoutingContext ctx, String userName, String domain) {
        this.userName = userName;
        this.ctx = ctx;
        this.domain = domain;
    }

    public HandlerCookie(String userName, String domain) {
        this.userName = userName;
        this.domain = domain;
    }

    public HandlerCookie limitCookie(int limit) {
        this.limitCookie = limit;

        return this;
    }

    public HandlerCookie cookieMedium() {

        return this;
    }

    public String getCookie(String key) {
        long time = System.currentTimeMillis();
        String creteCodeC = encode(new JsonObject().put("domain", domain).put("user", userName).put("time", time).toString());

        StringBuilder sb = new StringBuilder();
        StringBuilder sbEn = new StringBuilder();
        for (int i = 0; i < creteCodeC.length(); i++) {
            var c = creteCodeC.charAt(i);
            if (i == 3) {
                sbEn.append(c);
            }
            if (i == 5) {
                sbEn.append(c);
            }
            if (i != 3 & i != 5) {
                sb.append(c);
            }
        }

        return sbEn.toString() + sb.toString();
    }
    
    

    public boolean checkCookie(String cookie, String username, String domain) {

        char c3 = cookie.charAt(0), c5 = cookie.charAt(1);

        String valCodeC = cookie.substring(2, 5) + c3 + cookie.substring(5, 6) + c5 + cookie.substring(6, cookie.length());
        System.out.println("valCodeC " + valCodeC);
        String decodeCookie = new String(Base64.getDecoder().decode(valCodeC));

        System.out.println(">> decodeCookie " + decodeCookie);
        var json = new JsonObject(decodeCookie);
        if (json.getString("domain").equals(domain) & json.getString("user").equals(username)) {
            return true;
        }
        return false;
    }

    private String encode(String key) {

        return Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
    }
}

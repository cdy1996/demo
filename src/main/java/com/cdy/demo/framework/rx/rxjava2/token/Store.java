package com.cdy.demo.framework.rx.rxjava2.token;

import java.util.HashMap;
import java.util.Map;

public class Store {

    private static final String SP_RX = "sp_rx";
    private static final String TOKEN = "token";

    private Map<String, String> mStore;

    private Store() {
        mStore = new HashMap<>();
    }

    public static Store getInstance() {
        return Holder.INSTANCE;
    }

    public String getToken() {
        return mStore.get(TOKEN);
    }

    public void setToken(String token) {
        mStore.put(TOKEN, token);
    }

    private static final class Holder {
        private static final Store INSTANCE = new Store();
    }
}
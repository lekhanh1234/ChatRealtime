package com.example.messenger.operationServer;

import java.net.HttpURLConnection;
import java.net.URL;

public class PrepareConnectServer {
    public HttpURLConnection getHttpUrlConnect(String urlServer, String method){
        HttpURLConnection httpURLConnection=null;
        try {
        URL connectSever= null;
        connectSever = new URL(urlServer);
        httpURLConnection=(HttpURLConnection)connectSever.openConnection();
        httpURLConnection.setRequestMethod(method);
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpURLConnection.setDoOutput(true);
        }
         catch (Exception e) {
        }
        return httpURLConnection;
    }
}

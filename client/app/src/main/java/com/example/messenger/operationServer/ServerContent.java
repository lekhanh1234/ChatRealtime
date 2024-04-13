package com.example.messenger.operationServer;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ServerContent {
    public String getServerJsonContent(InputStream is){
        BufferedReader br = null;
        String s = "";
        String jsonData = "";
        try {
            br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((s = br.readLine()) != null) {
                jsonData = jsonData + s+"\n";
            }
            if(jsonData.length() == 0) return "";
            return jsonData.substring(0,jsonData.length()-1);
        } catch (Exception e) {
            Log.d("ngoai le trong getServerContent", jsonData);
            return null;
        }
    }
}

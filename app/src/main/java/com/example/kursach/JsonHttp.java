package com.example.kursach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * класс отвечающий за обращение к серверу
 * */
public class JsonHttp {

    /**
     * метод создания запроса
     * @param url - url адрес для обращения к серверу
     * */
    public static String makeHttpRequest(String url){
        String strResult = "";

        try{
            URL u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setConnectTimeout(2000);
            strResult = readStream(con.getInputStream());
            if(strResult.isEmpty()) return strResult;
        }catch (
                Exception e){
            e.printStackTrace();
        }

        return strResult;
    }
    /**
     * метод чтения из потока
     * @param in - поток для чтения
     * */
    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try{
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

}

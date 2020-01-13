package com.example.kursach;

import android.app.Activity;
import android.os.AsyncTask;



public class StatusTask extends AsyncTask<Void, Void, String> {

    private String mUrl;

    /**интерфейс для переноса информации на activity*/
    OnDataSendToActivity dataSendToActivity;
    /**
     * метод создания запроса
     * @param url - url адрес для обращения к серверу
     * @param activity - активити на которой выполняется метод
     * */
    public StatusTask(String url, Activity activity){
        dataSendToActivity = (OnDataSendToActivity)activity;
        mUrl = url;
    }
    /**
     * метод выполняемый на "заднем плане"
     * */
    @Override
    protected String doInBackground(Void... params) {
        String jsonString = JsonHttp.makeHttpRequest(mUrl);

        return jsonString;
    }
    /**
     * метод выполнеяемый после завершения
     * @param result - результат выпонения
     * */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dataSendToActivity.sendData(result);
    }
}
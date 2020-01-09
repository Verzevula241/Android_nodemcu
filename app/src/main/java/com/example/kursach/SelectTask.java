package com.example.kursach;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SelectTask extends AsyncTask<Void, Void, String> {

    private String mUrl;

    public SelectTask(String url){
        super();
        mUrl = url;
    }

    @Override
    protected String doInBackground(Void... params) {
       return JsonHttp.makeHttpRequest(mUrl);
    }

    @Override
    protected void onPostExecute(String result) {

    }
}

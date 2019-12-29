package com.example.kursach;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Task extends AsyncTask<String, Void,Void> {

        private String ip = MainActivity.ip;
        private String mess = MainActivity.mess;
        private Socket ch;
        private PrintWriter wr;

        @Override
        protected Void doInBackground(String... params) {
            try {
                ch = new Socket(ip,23);
                wr = new PrintWriter(ch.getOutputStream());
                wr.println(mess);
                wr.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {
            ch.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

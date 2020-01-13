package com.example.kursach;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * класс для отправки на плату сообщений
 * */
public class Task extends AsyncTask<String, Void,Void> {
    /**Ip адрес*/
        private String ip = MainActivity.ip;
    /**сообщение*/
        private String mess = MainActivity.mess;
    /**канал для связи*/
        private Socket ch;
    /**принтер для печати в поток*/
        private PrintWriter wr;

    /**
     * метод выполнения задачи на "заднем плане"
     * */
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
    /**
     * метод выполняемый после закрытия задачи
     * */
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

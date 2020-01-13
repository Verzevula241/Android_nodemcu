package com.example.kursach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


/**
 * Класс для работы с Activity , основной класс через который проходит вся программа и работа с платой
 * */
public class MainActivity extends AppCompatActivity implements OnDataSendToActivity,ExampleDialog.ExampleDialogListener, Frag2.FragmentAListener {


    /**лист для хранения температур*/
    static LinkedList<Integer> temp_list = new LinkedList<>();
    ImageView bg_state;
    ImageView status;
    /**положение кнопки*/
    static public String room_light = "0";
    /**положение кнопки*/
    static public String lock = "0";
    TextView txt_network;
    TextView head_txt;
    /**ip адрес*/
    static String ip = "192.168.1.66";
    /**сообщение*/
    static String mess = "";
    /**URL*/
    static String url ;
    Toast toast ;
    /**значение температуры*/
    static String temp;
    /**значение влажности*/
    static String hum;
    /**состояние платы*/
    String net = "1";
    /**состояние свитча*/
    static  boolean  stateLay = false;

    private Frag2 frag2;
    private Frag1 frag1;

    /**метод отображения уведомления
     * @param message - сообщение для отображения
     * */
    public void showAToast (String message){
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Метод выполняемый при создании активити, внутри Runnable который проверяет подключение к сети и выполняет основные запросы к плате
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = findViewById(R.id.status);
        bg_state = findViewById(R.id.bg_status);
        head_txt = findViewById(R.id.head_txt);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                url = String.format("http://%s/",ip);
                if(isNetworkAvailable()){
                    //bg_state.setImageResource(R.drawable.background_on);
                    set_network();
                    if(net.equals("0")) {
                        status.setImageResource(R.drawable.server_on);
                        updateTemp();
                        updateStatus();
                        net ="1";
                    }else{
                        status.setImageResource(R.drawable.server_off);
                    }


                }else{

                    showAToast(getString(R.string.server_offline));
                }


                handler.postDelayed(this, 1000);
            }
        }, 1000);//the time is in miliseconds
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Frag1()).commit();

    }
    /**метод меняющий задний фон*/
    public void back(){
        bg_state.setImageResource(R.drawable.weather);
    }
    /**метод меняющий задний фон*/
    public void back_org(){
        bg_state.setImageResource(R.drawable.background_on);
    }

    /**Листнер привязаный к нижнему меню, реагирует на нажатия пунктов меню и позволяет перемещаться по фрагментам*/
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {

                        case R.id.nav_home:
                            selectedFragment = new Frag1();

                            break;
                        case R.id.nav_favorites:
                            selectedFragment = new Frag2();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new Frag3();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                    return true;
                }
            };

    /**открывает диалог ввода Ip*/
    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    /**
     * применение переменной после ее передачи от openDialog
     * @param username - текст принятый от openDialog
     * */
    @Override
    public void applyTexts(String username) {
        ip = username;
        url = String.format("http://%s/",ip);
    }


    /**
     * метод срабатывает при создании меню
     * @param menu - меню
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    /**
     * метод срабатывает при нажатии пункта меню
     * @param item - пункт меню
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item2) {
            openDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * метод проверки доступа к интернету
     * */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * метод передачи сообщения с сервера методам
     * @param str - сообщение
     * */
    @Override
    public void sendData(String str) {
        updateButtonStatus(str);
        updateTempStatus(str);
        updatenetwork(str);

    }

    /**
     * метод отправляет на сервер запрос
     * */
    public void updateStatus(){
        String url_rl = url+"status";
        StatusTask task = new StatusTask(url_rl, this);
        task.execute();
    }
    /**
     * метод отправляет на сервер запрос
     * */
    public void updateTemp(){
        String url_rl = url+"temp";
        StatusTask task = new StatusTask(url_rl, this);
        task.execute();
    }
    /**
     * метод отправляет на сервер запрос
     * */
    public void set_network(){
        String url_rl = url+"network";
        StatusTask task = new StatusTask(url_rl, this);
        task.execute();
    }

    /**
     * метод обработки данных с сервера о его работе
     * @param jsonStrings -сообщение с сервера
     * */
    private void updatenetwork(String jsonStrings){
        try {
            JSONObject json = new JSONObject(jsonStrings);
            String nety = json.getString("net");

            if(!nety.isEmpty()){
                net = nety;
            }
            else showAToast("Нет подключения к серверу");
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    /**
     * метод обработки данных с датчиков на плате
     * @param jsonStrings -сообщение с сервера
     * */
    private void updateTempStatus(String jsonStrings){

        try {
            JSONObject json = new JSONObject(jsonStrings);
            temp = json.getString("temp");
            temp_list.add(Integer.valueOf(temp));
            hum = json.getString("hum");
            String far = json.getString("far");


        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    /**
     * метод обработки данных о состоянии кнопок на плате
     * @param jsonStrings -сообщение с сервера
     * */
    private void updateButtonStatus(String jsonStrings){
        try {
            JSONObject json = new JSONObject(jsonStrings);

             room_light = json.getString("rl");
             lock = json.getString("ml");

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onInputASent(CharSequence input) {

    }
}

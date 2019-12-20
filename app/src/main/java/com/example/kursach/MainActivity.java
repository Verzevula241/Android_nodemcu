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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnDataSendToActivity,ExampleDialog.ExampleDialogListener, FragmentA.FragmentAListener {



    ImageView bg_state;
    Button btn_rl, btn_mr, btn_bed, btn_fan;
    TextView txt_network;
    TextView head_txt;
    String ip = "192.168.0.33";
    //String url = "http://"+ip+"/"; //Define your NodeMCU IP Address here Ex: http://192.168.1.4/
    String url ;
    Toast toast ;
    static String temp;
    private FragmentA fragmentA;

    public void showAToast (String message){
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bg_state = findViewById(R.id.bg_status);
        txt_network = findViewById(R.id.txt_network);
        head_txt = findViewById(R.id.head_txt);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isNetworkAvailable()){
                    bg_state.setImageResource(R.drawable.background_on);
                    url = String.format("http://%s/",ip);

                }else{
                    bg_state.setImageResource(R.drawable.background);
                    //txt_network.setText(getString(R.string.server_offline));
                    showAToast(getString(R.string.server_offline));
                }
                updateTemp();
                updateStatus();
                handler.postDelayed(this, 2000);
            }
        }, 5000);  //the time is in miliseconds


        btn_rl = findViewById(R.id.room);
        btn_mr = findViewById(R.id.mirror);
        btn_bed = findViewById(R.id.bed);
        btn_fan = findViewById(R.id.fan);

        btn_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url_rl = url+"room_light";
                SelectTask task = new SelectTask(url_rl);
                task.execute();
                updateStatus();
            }
        });

        btn_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url_rl = url+"mirror_light";
                SelectTask task = new SelectTask(url_rl);
                task.execute();
                updateStatus();
            }
        });
        btn_bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url_rl = url+"bed_light";
                SelectTask task = new SelectTask(url_rl);
                task.execute();
                updateStatus();
            }
        });
        btn_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url_rl = url+"fan";
                SelectTask task = new SelectTask(url_rl);
                task.execute();
                updateStatus();
            }
        });

    }


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
                            selectedFragment = new FragmentA();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new Frag3();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                    return true;
                }
            };


    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(String username) {
        ip = username;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item2) {
            openDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void sendData(String str) {
        updateButtonStatus(str);
        updateTempStatus(str);

    }

    private void updateStatus(){
        String url_rl = url+"status";
        StatusTask task = new StatusTask(url_rl, this);
        task.execute();
    }
    private void updateTemp(){
        String url_rl = url+"temp";
        StatusTask task = new StatusTask(url_rl, this);
        task.execute();
    }


    private void updateTempStatus(String jsonStrings){

        try {
            JSONObject json = new JSONObject(jsonStrings);
            temp = json.getString("temp");
            String humi = json.getString("hum");
            String far = json.getString("far");

            if(!temp.isEmpty()){
                head_txt.setText(String.format("%s/%s/%s", temp, humi,far));
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    //Function for updating Button Status


    @Override
    public void onInputASent(CharSequence input) {
        fragmentA.updateEditText(input);
    }

    private void updateButtonStatus(String jsonStrings){
        try {
            JSONObject json = new JSONObject(jsonStrings);

            String room_light = json.getString("rl");
            String mirror_light = json.getString("ml");
            String bed_light = json.getString("bl");
            String fan = json.getString("fan");


            if(room_light.equals("1")){
                btn_rl.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_on);
            }else{
                btn_rl.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_off);
            }
            if(mirror_light.equals("1")){
                btn_mr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_on);
            }else{
                btn_mr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_off);
            }
            if(bed_light.equals("1")){
                btn_bed.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_on);
            }else{
                btn_bed.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_off);
            }
            if(fan.equals("1")){
                btn_fan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_on);
            }else{
                btn_fan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_off);
            }
            /*if(!temp.isEmpty()){
                btn_stat.setText(temp);
            }*/

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}

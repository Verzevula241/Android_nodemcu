package com.example.kursach;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;


public class Frag1 extends Fragment {

    Button btn_rl,btn_mr,btn_bed,btn_fan;


    private MainActivity main;

    private String url = main.url;
    String room_light ;
    String lock;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag1_layout, container, false);


        btn_rl =  v.findViewById(R.id.room);
        btn_rl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url_rl = url+"room_light";
                SelectTask task = new SelectTask(url_rl);
                task.execute();
            }
        });
        btn_mr =  v.findViewById(R.id.mirror);
        btn_mr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url_rl = url+"lock";
                SelectTask task = new SelectTask(url_rl);
                task.execute();
            }
        });

         return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                room_light = MainActivity.room_light;
                lock = MainActivity.lock;
                updateButtonStatus();
                handler.postDelayed(this, 200);
            }
        }, 200);  //the time is in miliseconds
    }




    protected void updateButtonStatus(){

            if(room_light.equals("1")){
                btn_rl.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_on);
            }else{
                btn_rl.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_off);
               }
            if(lock.equals("1")){
                btn_mr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.key_on);
            }else{
                btn_mr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.key);
            }
//            if(bed_light.equals("1")){
//                btn_bed.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_on);
//            }else{
//                btn_bed.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_off);
//            }
//            if(fan.equals("1")){
//                btn_fan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_on);
//            }else{
//                btn_fan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.power_off);
//            }
            /*if(!temp.isEmpty()){
                btn_stat.setText(temp);
            }*/

//        }catch (JSONException e){
//            e.printStackTrace();
//        }

    }




}
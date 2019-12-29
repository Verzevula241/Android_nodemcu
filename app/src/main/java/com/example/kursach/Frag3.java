package com.example.kursach;

import android.content.Context;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Frag3 extends Fragment {

    Switch sw;
    EditText porog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag3_layout, container, false);
        sw = v.findViewById(R.id.switch1);
        porog = v.findViewById(R.id.porog);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String pog = porog.getText().toString();
                if(!pog.equals("")){
                if(isChecked){
                    MainActivity.mess = pog;
                    Task task = new Task();
                    task.execute();
                }else{
                    porog.setText("");
                }
            }else {
                    Toast.makeText(getActivity(),"Введите цифры в поле",Toast.LENGTH_SHORT).show();
                    sw.setChecked(false);
                }
            }

        });

        return v;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }
}
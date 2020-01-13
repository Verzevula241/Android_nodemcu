package com.example.kursach;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Frag2 extends Fragment {
    private FragmentAListener listener;
    private TextView editText;
    private TextView editText1;
    private  TextView min;
    private  TextView max;
    public MainActivity main = new MainActivity();

    public interface FragmentAListener {
        void onInputASent(CharSequence input);
    }

    /**
     * Метод срабатывает при создании вида автоматически,подгружает XML файл для отображения температуры и влажности
     * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag2_layout, container, false);

        editText = v.findViewById(R.id.temp1);
        editText1 = v.findViewById(R.id.hum);
        min = v.findViewById(R.id.min);
        max = v.findViewById(R.id.max);
        min.setText("Min ");
        max.setText("Max ");
        editText.setText(String.format("%s °C",MainActivity.temp));
        editText1.setText(MainActivity.hum+" %");
        return v;
    }

    /**
     * Метод для обновления EditText
     * @param temp - значение температуры
     * @param hum - значение влажности
     * */
    public void updateEditText(CharSequence temp,CharSequence hum) {
        editText.setText(String.format("%s °C",temp));
        editText1.setText(hum+" %");
    }


    /**
     * метод срабатывает при работе фрагмента
     * @param context - контекст
     * */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentAListener) {
            listener = (FragmentAListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateEditText(MainActivity.temp,MainActivity.hum);
                if(MainActivity.temp_list.size()!=0) {
                    min.setText("Min "+min(Integer.valueOf(MainActivity.temp)));
                    max.setText("Max "+max(Integer.valueOf(MainActivity.temp)));
                }
                handler.postDelayed(this, 2000);
            }
        }, 1000);  //the time is in miliseconds
    }

    /**
     * метод для нахождения минимального значения
     * @param min - значение для сравнения
     * */
    public String min(int min){
        int minValue = min;
        for (int i = 0; i < MainActivity.temp_list.size(); i++) {
            if (MainActivity.temp_list.get(i) < minValue) {
                minValue = MainActivity.temp_list.get(i);
            }
        }
        return String.valueOf(minValue);
    }
    /**
     * метод для нахождения максимального значения
     * @param max - значение для сравнения
     * */
    public String max(int max){
        int maxValue = max;
        for (int i = 0; i < MainActivity.temp_list.size(); i++) {
            if (MainActivity.temp_list.get(i) > maxValue) {
                maxValue = MainActivity.temp_list.get(i);
            }
        }
        return String.valueOf(maxValue);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
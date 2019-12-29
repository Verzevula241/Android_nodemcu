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


public class FragmentA extends Fragment {
    private FragmentAListener listener;
    private TextView editText;
    private TextView editText1;

    public interface FragmentAListener {
        void onInputASent(CharSequence input);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag2_layout, container, false);

        editText = v.findViewById(R.id.temp1);
        editText1 = v.findViewById(R.id.hum);
        editText.setText(MainActivity.temp);
        editText1.setText(MainActivity.hum);
        return v;
    }

    public void updateEditText(CharSequence temp,CharSequence hum) {
        editText.setText(temp);
        editText1.setText(hum);
    }

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
                handler.postDelayed(this, 2000);
            }
        }, 1000);  //the time is in miliseconds
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
package com.example.kursach;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
/**
 * Класс для работы с диалоговым окном
 * */
public class ExampleDialog extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private ExampleDialogListener listener;


    /**метод срабатывающий при создании диалоговвго окна*/
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        final MainActivity main = new MainActivity();
        builder.setView(view)
                .setTitle("ip address")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = editTextUsername.getText().toString();
                        listener.applyTexts(username);
                    }
                });

        editTextUsername = view.findViewById(R.id.edit_username);

        return builder.create();
    }

    /**
     * метод срабатывает при обращении к окну
     * @param context - контекст
     * */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    /**
     * интерфейс для передачи данных на основную Activity
     * */
    public interface ExampleDialogListener {
        void applyTexts(String username);
    }
}

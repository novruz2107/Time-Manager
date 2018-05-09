package com.antech.timemanagement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import static android.R.attr.y;
import static android.R.string.no;
import static android.R.string.yes;

/**
 * Created by Novruz Engineer on 4/2/2018.
 */

public class CustomDialogClass extends Dialog {

    public Activity c;
    public static boolean quit=false;
    public Dialog d;
    public ImageButton az, en;

    DialogListener listener;

    interface DialogListener {
        void onCompleted();

        void onCanceled();
    }


    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        final SharedPreferences prefs = getContext().getSharedPreferences("Files", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        az = (ImageButton) findViewById(R.id.az);
        en = (ImageButton) findViewById(R.id.en);
//        az.setOnClickListener(this);
//        en.setOnClickListener(this);

        az.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCompleted();
                CustomDialogClass.this.dismiss();
                editor.putString("number7", "az");
            }
        });

        setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (listener != null)
                    listener.onCanceled();
            }
        });

        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCompleted();
                CustomDialogClass.this.dismiss();
                editor.putString("number7", "en");
            }
        });

        setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (listener != null)
                    listener.onCanceled();
            }
        });
    }


    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.az:
//                Utils.writeToFile("25 5 12 true 1 false az false", getContext());
//                notify();
//                break;
//            case R.id.en:
//                Utils.writeToFile("25 5 12 true 1 false en false", getContext());
//                notify();
//                break;
//            default:
//                break;
//        }
//    }
}
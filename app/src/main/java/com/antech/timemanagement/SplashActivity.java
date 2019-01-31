package com.antech.timemanagement;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Novruz Engineer on 4/1/2018.
 */

public class SplashActivity extends Activity {

    private static String TAG = SplashActivity.class.getName();
    private static long SLEEP_TIME = 1;    // Time in seconds to show the picture
    private boolean isFirst;
    public CustomDialogClass cdd;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar

        setContentView(R.layout.splash_screen);
        cdd = new CustomDialogClass(SplashActivity.this);

        //your layout with the picture
        intent = new Intent(SplashActivity.this, FirstActivity.class);

        // Start timer and launch main activity
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class IntentLauncher extends Thread {
        @Override
        /**
         * Sleep for some time and than start new activity.
         */
        public void run() {
            try {
                // Sleeping
                Thread.sleep(SLEEP_TIME*1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

//             Start main activity
            final Intent intent = new Intent(SplashActivity.this, FirstActivity.class);
            final SharedPreferences prefs = getSharedPreferences("Files", MODE_PRIVATE);
            final SharedPreferences.Editor editor = prefs.edit();
            isFirst=prefs.getBoolean("number8", true);
//            if(isFirst){
//
//                new Thread()
//                {
//                    public void run()
//                    {
//                        SplashActivity.this.runOnUiThread(new Runnable()
//                        {
////
//                            public void run()
//                            {
//                                cdd.setDialogListener(new CustomDialogClass.DialogListener() {
//                                    @Override
//                                    public void onCompleted() {
//                                        startActivity(intent);
//                                        editor.putBoolean("number8", false);
//                                        editor.putString("number7", CustomDialogClass.lang_code);
//                                        editor.apply();
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void onCanceled() {
//
//                                    }
//                                });
//                                cdd.show();
//
//                            }
//                        });
//
//                    }
//                }.start();
//
//
//            }
//            if(!isFirst){
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
//            }


        }
    }

}



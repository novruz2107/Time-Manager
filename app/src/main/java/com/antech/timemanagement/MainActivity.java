package com.antech.timemanagement;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

import static com.antech.timemanagement.SettingsActivity.pomodoroTime;

public class MainActivity extends AppCompatActivity {
    static TextView textView;
    String[] sentencesForWork;
    String[] sentencesForBreak;
    Button Pause;
    FloatingActionButton restartButton;
    long targetTimeInMilliSeconds;
    static CountDownTimer pomodoroTimer;
    CountDownTimer shortBreakTimer;
    CountDownTimer longBreakTimer;
    CountDownTimer backupTimer;
    int flag=0;
    static int flagExtra=0;
    static long backupTime;
    public static boolean isWorking=true;
    public boolean blank=false;
    public static boolean isWork=true;
    static NotificationManager mNotifyMgr;
    MediaPlayer alarm1;
    MediaPlayer alarm2;
    static MediaPlayer tick1;
    static MediaPlayer tick2;
    String lang_code;
    static int counter=-1;
    static int counterForBreak=-1;
    Random random;
    TextSwitcher forWork;
//    private AdView myAdView;
    private NotificationCompat.Builder notificationBuilder;
    private Notification notification;
//    private InterstitialAd mInterstitialAd;
    Typeface typeface;


    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("Files", MODE_PRIVATE);
        lang_code = prefs.getString("number7", "en");
        Context context = Utils.changeLang(newBase, lang_code);
        super.attachBaseContext(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        final SharedPreferences prefs = getSharedPreferences(SettingsActivity.DATAS, MODE_PRIVATE);
        if(prefs.getBoolean("number6", true)){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        targetTimeInMilliSeconds = prefs.getInt("number1", 25)*60*1000;

        //Notification Builder:
        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getText(R.string.app_name))
                .setContentText("")
                .setSmallIcon(R.drawable.ic_notif)
                .setWhen(System.currentTimeMillis()).setOngoing(true).setAutoCancel(false);
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = notificationBuilder.build();
        startService(new Intent(getBaseContext(), AppService.class));
        //////////////////////

//        //ADs section
//        MobileAds.initialize(this, "ca-app-pub-5977142818330521~1943995775");
//        myAdView = (AdView) findViewById(R.id.adView);
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-5977142818330521/9216164252");
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                AdRequest adRequest = new AdRequest.Builder().build();
//                myAdView.loadAd(adRequest);
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//        }, 5000);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/sofiapro-light.otf");

        ////////////////
        textView = (TextView) findViewById(R.id.textView);
        textView.setBackgroundColor(Color.GRAY);
//        textView.setTypeface(typeface);
        Pause = (Button) findViewById(R.id.pause);
        restartButton = (FloatingActionButton) findViewById(R.id.restart);
        alarm1 = MediaPlayer.create(this, R.raw.alarm1);
        alarm2 = MediaPlayer.create(this, R.raw.alarm2);
        final int selectedTick = prefs.getInt("number9", 0);
        playTicks(selectedTick);
        random = new Random();
        forWork = (TextSwitcher) findViewById(R.id.forWork);
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);

        sentencesForWork = new String[] {getString(R.string.sent1),getString(R.string.sent2),getString(R.string.sent3),
                getString(R.string.sent4),getString(R.string.sent5),getString(R.string.sent6),getString(R.string.sent7),
                getString(R.string.sent8),getString(R.string.sent9), getString(R.string.sent10)};
        sentencesForBreak = new String[] {getString(R.string.sen1),getString(R.string.sen2),getString(R.string.sen3)};

        forWork.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView myText = new TextView(MainActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(25);
                myText.setTypeface(typeface);
                myText.setTextColor(Color.argb(255, 208, 208, 208));
                return myText;
            }
        });

        forWork.setInAnimation(in);
        forWork.setOutAnimation(out);


        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setText(Integer.toString((int) targetTimeInMilliSeconds/60000) +":"+ Integer.toString((int) ((targetTimeInMilliSeconds/1000 - targetTimeInMilliSeconds/60000))));

        //whenever intent begins, timer should start
        pomodoroTimer = new CountDownTimer(targetTimeInMilliSeconds, 1000){

            public void onTick (long milliSecondsUntilDone){
                    int minutes = (int) milliSecondsUntilDone / 60000;
                    int seconds = (int) (milliSecondsUntilDone / 1000 - minutes * 60);

                    String secondString=Integer.toString(seconds);
                    if(seconds<=9 && seconds>=0){
                        secondString="0"+seconds;
                    }
                    if(prefs.getBoolean("number4", true)) {
                        notificationBuilder.setContentText("Work time: " + Integer.toString(minutes) + ":" + secondString);
                        mNotifyMgr.notify(11, notificationBuilder.build());
                    }


                textView.setText(Integer.toString(minutes) + ":" + secondString);
                    backupTime = milliSecondsUntilDone;
                    counter++;
                    showSentences();

            }

            public void onFinish(){
                playSound(prefs.getInt("number5", 0));
                vibrate();
                Pause.setActivated(false);
                flag++;
                textView.setText("00:00");
                Pause.setEnabled(false);
                isWork=false;

                if(flag % 3 !=0) {
                    shortBreakTimer.start();
                }else{
                    longBreakTimer.start();
                }
                stopTick(selectedTick);

            }
        }.start();

        shortBreakTimer = new CountDownTimer(prefs.getInt("number2", 5)*60*1000, 1000) {
            @Override
            public void onTick(long milliSecondsUntilDone) {
                int minutes = (int) milliSecondsUntilDone / 60000;
                int seconds = (int) (milliSecondsUntilDone / 1000 - minutes * 60);
                String secondString=Integer.toString(seconds);
                if(seconds<=9 && seconds>=0){
                    secondString="0"+seconds;
                }

                notificationBuilder.setContentText("Short break: "+ Integer.toString(minutes) + ":" + secondString);
                mNotifyMgr.notify(11, notificationBuilder.build());
                textView.setText(Integer.toString(minutes) + ":" + secondString);
                counterForBreak++;
                showSentences();
            }

            @Override
            public void onFinish() {
                textView.setBackgroundColor(Color.TRANSPARENT);
                vibrate();
                Pause.setEnabled(true);
                isWork=true;
                playTicks(selectedTick);
                //if something goes wrong about breaks, change it to pomodoroTimer://////
                pomodoroTimer.start();


            }
        };

        longBreakTimer = new CountDownTimer((prefs.getInt("number3", 12))*60*1000, 1000) {
            @Override
            public void onTick(long milliSecondsUntilDone) {
                int minutes = (int) milliSecondsUntilDone / 60000;
                int seconds = (int) (milliSecondsUntilDone / 1000 - minutes * 60);
                String secondString=Integer.toString(seconds);
                if(seconds<=9 && seconds>=0){
                    secondString="0"+seconds;
                }
                notificationBuilder.setContentText("Long break: "+ Integer.toString(minutes) + ":" + secondString);
                mNotifyMgr.notify(11, notificationBuilder.build());
                textView.setText(Integer.toString(minutes) + ":" + secondString);
                counterForBreak++;
                showSentences();
            }

            @Override
            public void onFinish() {
                vibrate();
                textView.setBackgroundColor(Color.TRANSPARENT);
                Pause.setEnabled(true);
                isWork=true;
                //if something goes wrong about breaks, change it to pomodoroTimer://////
                pomodoroTimer.start();
                playTicks(selectedTick);
            }
        };

        restartButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                counter=-1; counterForBreak=-1;
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
                    finish();
                    mNotifyMgr.cancelAll();
                    stopTick(selectedTick);
                    cancelAllTimers();
                    startActivity(new Intent(MainActivity.this, FirstActivity.class));
//                }

                return true;
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), getString(R.string.long_click_hint),
                        Toast.LENGTH_SHORT).show();
            }
        });


//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                finish();
//                mNotifyMgr.cancelAll();
//                stopTick(selectedTick);
//                cancelAllTimers();
//                startActivity(new Intent(MainActivity.this, FirstActivity.class));
//            }
//        });


    }

    public void showSentences(){
        if(isWork) {
            if (counter % 15 == 0) {
                int i = random.nextInt(9);
                forWork.setText(String.valueOf(sentencesForWork[i]));
            }
        }else{
            if(counterForBreak%15==0){
                int i=random.nextInt(2);
                forWork.setText(String.valueOf(sentencesForBreak[i]));
            }
        }
    }


    public void cancelAllTimers(){
        pomodoroTimer.cancel();
        shortBreakTimer.cancel();
        longBreakTimer.cancel();
        if(blank){
            backupTimer.cancel();
        }
    }


    //Code that handles minimizes function when back button pressed:////////////////////////
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /////////////////////////////////////////////////////////////////////////////////////////


    public void onResume(){
        super.onResume();
        targetTimeInMilliSeconds = pomodoroTime * 60 * 1000;

    }

    public void onPause(){
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.DATAS, MODE_PRIVATE);
        if(prefs.getBoolean("number4", true)) {
            //Showing of icon in notification bar:

            Intent nIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
        }
    }

    public void onDestroy(){
        super.onDestroy();
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.DATAS, MODE_PRIVATE);
        if(prefs.getBoolean("number4", true)) {
            mNotifyMgr.cancel(11);
        }

    }



//    public void handlePause(View view){
//        blank=true;
//        if(isWorking) {
//            pomodoroTimer.cancel();
//            if(flagExtra>=1){
//                backupTimer.cancel();
//            }
//            isWorking=false;
//            Pause.setText("Start");
//        }else if(!isWorking){
//            flagExtra++;
//            isWorking=true;
//            Pause.setText("Pause");
//            backupTimer = new CountDownTimer(backupTime, 1000){
//
//
//                public void onTick (long milliSecondsUntilDone){
//                    int minutes = (int) milliSecondsUntilDone / 60000;
//                    int seconds = (int) (milliSecondsUntilDone / 1000 - minutes * 60);
//                    String secondString=Integer.toString(seconds);
//                    if(seconds<=9 && seconds>=0){
//                        secondString="0"+seconds;
//                    }
//                    textView.setText(Integer.toString(minutes) + ":" + secondString);
//                    backupTime = milliSecondsUntilDone;
//                    counter++;
//                    showSentences();
//                }
//
//                public void onFinish(){
//                    playSound(Integer.parseInt(Utils.readFromFile(getApplicationContext()).split(" ")[4]));
//                    vibrate();
//                    Pause.setEnabled(false);
//                    isWork=false;
//                    flag++;
//                    textView.setText("00:00");
//                    textView.setBackgroundColor(Color.GREEN);
//
//                    if(flag % 3 !=0) {
//                        shortBreakTimer.start();
//                    }else{
//                        longBreakTimer.start();
//                    }
//
//                }
//            }.start();
//        }
//
//    }

    public void playTicks(int i){
        if(i==0){
            tick1 = MediaPlayer.create(this, R.raw.tick1);
            tick1.setLooping(true);
            tick1.start();
        }else if(i==1){
            tick2 = MediaPlayer.create(this, R.raw.tick2);
            tick2.setLooping(true);
            tick2.start();
        }else if(i==2){
            //no sound
        }
    }

    public static void stopTick(int i){
        if(i==0){
            tick1.stop();
        }else if(i==1){
            tick2.stop();
        }else if(i==2) {
            //nothing
        }
    }

    public void playSound(int i){
        if(i==0){
            alarm1.start();
        }else if(i==1){
            alarm2.start();
        }else if(i==2){
            //no sound
        }
    }

    public void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            v.vibrate(1000);
        }
    }

    public static void cancellNotification(){
        mNotifyMgr.cancelAll();
    }


}
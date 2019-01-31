package com.antech.timemanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Field;

import static com.antech.timemanagement.R.raw.tick;

/**
 * Created by Novruz Engineer on 3/22/2018.
 */

public class FirstActivity extends AppCompatActivity {

    Boolean detailFragmentActive = false;
    SoundPool sp;
    Button button;
    String lang_code;
    View layout;
    NumberPicker np_pomodoro;
    NumberPicker np_short;
    NumberPicker np_long;
    int soundID;
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle toolbarDrawerToggle;
    SettingsActivity frag;
    Typeface typeface;
    TextView toolbar_title;
    TextView welcome, t6, t7, t8;
    Context c;

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("Files", MODE_PRIVATE);
        lang_code = prefs.getString("number7", "en");
        Context context = Utils.changeLang(newBase, lang_code);
        c=this;
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        frag = new SettingsActivity();
        myToolbar.setTitleTextColor(Color.BLACK);
        myToolbar.setBackgroundColor(Color.parseColor("#64ACC4"));
        toolbarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.open, R.string.close);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("Time Manager");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/sofiapro-light.otf");
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(typeface);

        //
        welcome = (TextView) findViewById(R.id.welcomeTextView);
        t6 = (TextView) findViewById(R.id.textView6);
        t7 =(TextView) findViewById(R.id.textView7);
        t8 = (TextView) findViewById(R.id.textView8);
        welcome.setTypeface(typeface);t6.setTypeface(typeface);t7.setTypeface(typeface);t8.setTypeface(typeface);

        SharedPreferences prefs = getSharedPreferences(SettingsActivity.DATAS, MODE_PRIVATE);
        //Navigation listener:
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(false);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        switch (menuItem.getItemId()){
                            case R.id.Settings:{
                                FragmentManager fm = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                SettingsActivity fragment = new SettingsActivity();
                                fragmentTransaction.replace(R.id.frame_layout, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }break;
                            case R.id.About:{
                                FragmentManager fm = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                AboutFragment fragment = new AboutFragment();
                                fragmentTransaction.replace(R.id.frame_layout, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }break;
                            case R.id.Rate:{
                                final AlertDialog builder = new AlertDialog.Builder(FirstActivity.this).create();
                                builder.setTitle(R.string.dialog_title);
                                builder.setMessage(c.getString(R.string.dialog_message));
                                builder.setButton(AlertDialog.BUTTON_POSITIVE , c.getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.antech.timemanagement"));
                                                startActivity(intent);
                                                builder.cancel();
                                            }
                                        });
                                builder.setButton(AlertDialog.BUTTON_NEGATIVE, c.getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                builder.cancel();
                                            }
                                        });
                                builder.setIcon(R.drawable.rate_icon);
                                builder.show();
                            }
                        }


                        return true;
                    }
                });

        ////////////////////////////////

        layout = findViewById(R.id.frame_layout);
        button = (Button) findViewById(R.id.button);
        button.setTypeface(typeface);
        np_pomodoro = (NumberPicker) findViewById(R.id.np_pomodoro);
        np_pomodoro.setMinValue(1);
        np_pomodoro.setMaxValue(60);
        np_pomodoro.setValue(prefs.getInt("number1", 25));
        np_short = (NumberPicker) findViewById(R.id.np_short);
        np_long = (NumberPicker) findViewById(R.id.np_long);
        np_short.setMinValue(1);
        np_short.setMaxValue(20);
        np_long.setMinValue(5);
        np_long.setMaxValue(40);
        np_short.setValue(prefs.getInt("number2", 5));
        np_long.setValue(prefs.getInt("number3", 12));

        setNumberPickerTextColor(np_pomodoro, Color.WHITE);
        setNumberPickerTextColor(np_short, Color.WHITE);
        setNumberPickerTextColor(np_long, Color.WHITE);

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sp = new SoundPool.Builder().setAudioAttributes(attributes).build();
        soundID = sp.load(this, tick, 1);

        np_pomodoro.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sp.play(soundID, 1, 1, 0, 0, 1);
            }
        });np_short.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sp.play(soundID, 1, 1, 0, 0, 1);
            }
        });np_long.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sp.play(soundID, 1, 1, 0, 0, 1);
            }
        });

    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

    public void startToCount(View view){
        Intent mainIntent = new Intent(FirstActivity.this, MainActivity.class);
        startActivity(mainIntent);
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.DATAS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("number1", np_pomodoro.getValue());
        editor.putInt("number2", np_short.getValue());
        editor.putInt("number3", np_long.getValue());
        editor.apply();

        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("TextColor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("TextColor", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("TextColor", e);
                }
            }
        }
        return false;
    }


}
package com.antech.timemanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Novruz Engineer on 3/22/2018.
 */

public class SettingsActivity extends Fragment {

    CheckBox checkNotificationBox;
    Spinner alarmList;
    Spinner alarmList2;
    static long pomodoroTime;
    List<String> alarms;
    List<String> ticks;
    String selectedAlarm;
    String selectedTick;
    Switch analogOrDigital;
    ImageButton flag_en;
    ImageButton flag_az;
    ImageButton flag_ru;
    String lang_code;
    public static final String DATAS = "Files";

    @Override
    public void onResume() {
        super.onResume();
        this.onCreate(null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = v.findViewById(R.id.settings_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setTitle("Settings");

        SharedPreferences prefs = getActivity().getSharedPreferences(DATAS, MODE_PRIVATE);

        //Initiations of widgets
        checkNotificationBox = (CheckBox) v.findViewById(R.id.checkNotificationBox);
        alarmList = (Spinner) v.findViewById(R.id.alarm_list);
        alarmList2 = v.findViewById(R.id.alarm_list2);
        analogOrDigital = (Switch) v.findViewById(R.id.switch1);
        flag_az = (ImageButton) v.findViewById(R.id.flag_az);
        flag_en = (ImageButton) v.findViewById(R.id.flag_en);
        flag_ru = (ImageButton) v.findViewById(R.id.flag_ru);
        alarms = new ArrayList<>();
        ticks = new ArrayList<>();
        alarms.add(0, "Alarm 1");
        alarms.add(1, "Alarm 2");
        alarms.add(2, "None");
        ticks.add(0, "Tick 1");
        ticks.add(1, "Tick 2");
        ticks.add(2, "None");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, ticks);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, alarms);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alarmList2.setAdapter(adapter2);
        alarmList2.setSelection(prefs.getInt("number9", 0));
        alarmList.setAdapter(adapter);
        alarmList.setSelection(prefs.getInt("number5", 0));



        analogOrDigital.setChecked(prefs.getBoolean("number6", true));

        checkNotificationBox.setChecked(prefs.getBoolean("number4", true));

        alarmList2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                Object item = parent.getItemAtPosition(position);

                if (item.toString().equals("Tick 1")) {
                    selectedTick = "0";
                } else if (item.toString().equals("Tick 2")) {
                    selectedTick = "1";
                } else if (item.toString().equals("None")) {
                    selectedTick = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        alarmList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                Object item = parent.getItemAtPosition(position);

                if (item.toString().equals("Alarm 1")) {
                    selectedAlarm = "0";
                } else if (item.toString().equals("Alarm 2")) {
                    selectedAlarm = "1";
                } else if (item.toString().equals("None")) {
                    selectedAlarm = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        flag_az.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_en.setAlpha(0.3f);
                flag_ru.setAlpha(0.3f);
                flag_az.setAlpha(1f);
                flag_az.setScaleX(1.2f);
                flag_az.setScaleY(1.2f);
                flag_en.setScaleX(1f);
                flag_en.setScaleY(1f);
                flag_ru.setScaleX(1f);
                flag_ru.setScaleY(1f);
                lang_code="az";
            }
        });

        flag_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_az.setAlpha(0.3f);
                flag_ru.setAlpha(0.3f);
                flag_en.setAlpha(1f);
                flag_en.setScaleX(1.2f);
                flag_en.setScaleY(1.2f);
                flag_az.setScaleX(1f);
                flag_az.setScaleY(1f);
                flag_ru.setScaleX(1f);
                flag_ru.setScaleY(1f);
                lang_code="en";
            }
        });

        flag_ru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_az.setAlpha(0.3f);
                flag_en.setAlpha(0.3f);
                flag_ru.setAlpha(1f);
                flag_ru.setScaleX(1.2f);
                flag_ru.setScaleY(1.2f);
                flag_az.setScaleX(1f);
                flag_az.setScaleY(1f);
                flag_en.setScaleX(1f);
                flag_en.setScaleY(1f);
                lang_code="ru";
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(DATAS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("number9", Integer.parseInt(selectedTick));
                editor.putBoolean("number4", checkNotificationBox.isChecked());
                editor.putBoolean("number6", analogOrDigital.isChecked());
                editor.putInt("number5", Integer.parseInt(selectedAlarm));
                editor.putString("number7", lang_code);
                editor.putBoolean("number8", false);
                editor.apply();

                getActivity().onBackPressed();
            }
        });

        return v;
    }


}
package com.chang.project_yak_ver2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chang.project_yak_ver2.Api.inputData;
import com.chang.project_yak_ver2.Dinner.Dinner;
import com.chang.project_yak_ver2.Lunch.Lunch;
import com.chang.project_yak_ver2.Model.DrugModel;
import com.chang.project_yak_ver2.Morning.Morning;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class mainScreen extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rl_Morning;
    private RelativeLayout rl_Lunch;
    private RelativeLayout rl_DInner;

    public ArrayList<DrugModel> morningDrug = new ArrayList<>();
    public ArrayList<DrugModel> lunchDrug = new ArrayList<>();
    public ArrayList<DrugModel> dinnerDrug = new ArrayList<>();

    private TextView tvMorningTime;
    private TextView tvLunchTime;
    private TextView tvDinnerTime;

    public static Context mContext;
    private ArrayList<String> Cautions;
    private List<inputData.Post.Take_med> take_meds;

    private Button BtnCausions;
    private Button BtnLogOut;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        mContext = this;

        setTime();

        rl_Morning = findViewById(R.id.rl_morning);
        rl_Morning.setOnClickListener(this);
        rl_Lunch = findViewById(R.id.rl_lunch);
        rl_Lunch.setOnClickListener(this);
        rl_DInner = findViewById(R.id.rl_dinner);
        rl_DInner.setOnClickListener(this);

        BtnCausions = findViewById(R.id.btn_cautions);
        BtnCausions.setOnClickListener(this);
        BtnLogOut = findViewById(R.id.btn_logOut);
        BtnLogOut.setOnClickListener(this);

        Intent StartIntent = getIntent();
        Cautions = (ArrayList<String>) StartIntent.getSerializableExtra("userDataCautions");
        take_meds = (List<inputData.Post.Take_med>) StartIntent.getSerializableExtra("userDataTake_med");
    }

    public void setTime() {
        setMorningTime();
        setLunchTime();
        setDinnerTime();
    }

    public void deleteMed(String medName) {
        int i;
        for (i = 0; i < take_meds.size(); i++) {
            String tmp = take_meds.get(i).getProd_name();
            if (tmp.equals(medName)) {
                Log.d("FetchPostTask", take_meds.get(i).getProd_name() + " delete");
                take_meds.remove(i);
                break;
            }
        }

    }


    private void setMorningTime() { //메인화면 아침 시간을 설정
        tvMorningTime = findViewById(R.id.tv_morningTime);

        Calendar defaultMorningTime = Calendar.getInstance();
        defaultMorningTime.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, 8, 0);
        SharedPreferences sharedPreferences = getSharedPreferences("mDailyAlarm", MODE_PRIVATE);
        long millis = sharedPreferences.getLong("mNextNotifyTime", defaultMorningTime.getTimeInMillis());
        Calendar morningNotifyTime = new GregorianCalendar();
        morningNotifyTime.setTimeInMillis(millis);
        Date currentDateTime = morningNotifyTime.getTime();

        String moningTime = new SimpleDateFormat
                ("a hh:mm ",
                        Locale.getDefault()).format(currentDateTime);


        tvMorningTime.setText(moningTime);
    }

    private void setLunchTime() {
        tvLunchTime = findViewById(R.id.tv_lunchTime);

        Calendar defaultLunchTime = Calendar.getInstance();
        defaultLunchTime.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, 13, 0);
        SharedPreferences sharedPreferences = getSharedPreferences("lDailyAlarm", MODE_PRIVATE);
        long millis = sharedPreferences.getLong("lNextNotifyTime", defaultLunchTime.getTimeInMillis());
        Calendar lunchNotifyTime = new GregorianCalendar();
        lunchNotifyTime.setTimeInMillis(millis);
        Date currentDateTime = lunchNotifyTime.getTime();

        String lunchTime = new SimpleDateFormat
                ("a hh:mm ",
                        Locale.getDefault()).format(currentDateTime);


        tvLunchTime.setText(lunchTime);
    }

    private void setDinnerTime() {
        tvDinnerTime = findViewById(R.id.tv_dinnerTime);

        Calendar defaultDinnerTime = Calendar.getInstance();
        defaultDinnerTime.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, 19, 0);
        SharedPreferences sharedPreferences = getSharedPreferences("dDailyAlarm", MODE_PRIVATE);
        long millis = sharedPreferences.getLong("dNextNotifyTime", defaultDinnerTime.getTimeInMillis());
        Calendar dinnerNotifyTime = new GregorianCalendar();
        dinnerNotifyTime.setTimeInMillis(millis);
        Date currentDateTime = dinnerNotifyTime.getTime();

        String dinnerTime = new SimpleDateFormat
                ("a hh:mm ",
                        Locale.getDefault()).format(currentDateTime);
        tvDinnerTime.setText(dinnerTime);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_morning:
                intent = new Intent(this, Morning.class);
                setDrug();
                intent.putExtra("array", morningDrug);
                break;
            case R.id.rl_lunch:
                intent = new Intent(this, Lunch.class);
                setDrug();
                intent.putExtra("array", lunchDrug);
                break;
            case R.id.rl_dinner:
                intent = new Intent(this, Dinner.class);
                setDrug();
                intent.putExtra("array", dinnerDrug);
                break;

            case R.id.btn_cautions:
                intent = new Intent(this, Cautions.class);
                intent.putExtra("array", Cautions);
                break;

            case R.id.btn_logOut:
                SharedPreferences sharedPref = getSharedPreferences("firstLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("IsSSN");
                editor.commit();

                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        startActivity(intent);

    }

    private void setDrug() {
        int takeMedsLength = take_meds.size();
        ArrayList<DrugModel> Mtmp = new ArrayList<>();
        ArrayList<DrugModel> Ltmp = new ArrayList<>();
        ArrayList<DrugModel> Dtmp = new ArrayList<>();
        for (int i = 0; i < takeMedsLength; i++) {
            boolean tmp[] = take_meds.get(i).getTake_session();

            if (tmp[0]) {
                DrugModel item = new DrugModel(take_meds.get(i).getProd_name());
                Mtmp.add(item);
            }
            if (tmp[1]) {
                DrugModel item = new DrugModel(take_meds.get(i).getProd_name());
                Ltmp.add(item);
            }
            if (tmp[2]) {
                DrugModel item = new DrugModel(take_meds.get(i).getProd_name());
                Dtmp.add(item);
            }
        }
        dinnerDrug = Dtmp;
        lunchDrug = Ltmp;
        morningDrug = Mtmp;
    }
}

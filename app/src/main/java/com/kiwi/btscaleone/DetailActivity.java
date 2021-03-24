package com.kiwi.btscaleone;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private Menu menu;
    private Toolbar toolbar;

    // objects
    private TextView tv_weight;
    private TextView tv_bmi;
    private TextView tv_bfr;
    private TextView tv_bmr;
    private TextView tv_slm;
    private TextView tv_thr;

    private TextView t_weight;
    private TextView t_bmi;
    private TextView t_bfr;
    private TextView t_bmr;
    private TextView t_slm;
    private TextView t_thr;

    private LinearLayout ll_d;

    private String date = "";
    private String time = "";
    private String weight = "";
    private String bmi = "";
    private String bfr = "";
    private String bmr = "";
    private String slm = "";
    private String thr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.detail);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        tv_weight = findViewById(R.id.dvWeight);
        tv_bmi = findViewById(R.id.dvBmi);
        tv_bfr = findViewById(R.id.dvBfr);
        tv_bmr = findViewById(R.id.dvBmr);
        tv_slm = findViewById(R.id.dvSlm);
        tv_thr = findViewById(R.id.dvThr);

        t_weight = findViewById(R.id.dWeight);
        t_bmi = findViewById(R.id.dBmi);
        t_bfr = findViewById(R.id.dBfr);
        t_bmr = findViewById(R.id.dBmr);
        t_slm = findViewById(R.id.dSlm);
        t_thr = findViewById(R.id.dThr);

        ll_d = findViewById(R.id.ll_d);


        if(Build.VERSION.SDK_INT >= 29) {
            int currentNightMode = this.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO: {
                    Log.i("MODE4", " LIGHT");

                    ll_d.setBackgroundResource(R.drawable.shadow_light);
                    //ll_t2.setBackgroundResource(R.drawable.shadow_light);
                    tv_weight.setTextColor(Color.BLACK);
                    tv_bmi.setTextColor(Color.BLACK);
                    tv_bfr.setTextColor(Color.BLACK);
                    tv_bmr.setTextColor(Color.BLACK);
                    tv_slm.setTextColor(Color.BLACK);
                    tv_thr.setTextColor(Color.BLACK);
                    t_weight.setTextColor(Color.BLACK);
                    t_bmi.setTextColor(Color.BLACK);
                    t_bfr.setTextColor(Color.BLACK);
                    t_bmr.setTextColor(Color.BLACK);
                    t_slm.setTextColor(Color.BLACK);
                    t_thr.setTextColor(Color.BLACK);


                    break;
                }
                case Configuration.UI_MODE_NIGHT_YES:
                    //return true;
                {
                    Log.i("MODE4", " DARK");
                    setTheme(R.style.AppTheme2);
                    ll_d.setBackgroundResource(R.drawable.shadow_dark);
                    //ll_t2.setBackgroundResource(R.drawable.shadow_dark);
                    tv_weight.setTextColor(Color.WHITE);
                    tv_bmi.setTextColor(Color.WHITE);
                    tv_bfr.setTextColor(Color.WHITE);
                    tv_bmr.setTextColor(Color.WHITE);
                    tv_slm.setTextColor(Color.WHITE);
                    tv_thr.setTextColor(Color.WHITE);
                    t_weight.setTextColor(Color.WHITE);
                    t_bmi.setTextColor(Color.WHITE);
                    t_bfr.setTextColor(Color.WHITE);
                    t_bmr.setTextColor(Color.WHITE);
                    t_slm.setTextColor(Color.WHITE);
                    t_thr.setTextColor(Color.WHITE);






                    break;
                }
            }
        }

        getData();

    }

    private void getData() {

        date = getIntent().getStringExtra("DATE");
        time = getIntent().getStringExtra("TIME");

        weight = getIntent().getStringExtra("WEIG");
        bmi = getIntent().getStringExtra("BMI");
        bfr = getIntent().getStringExtra("BFR");
        bmr = getIntent().getStringExtra("BMR");
        slm = getIntent().getStringExtra("SLM");
        thr = getIntent().getStringExtra("THR");

        tv_weight.setText(String.format("%s Kg", weight));
        tv_bmi.setText(bmi);
        tv_bfr.setText(String.format("%s %%", bfr));
        tv_bmr.setText(String.format("%s Kcal", bmr));
        tv_slm.setText(String.format("%s Kg", slm));
        tv_thr.setText(String.format("%s %%", thr));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

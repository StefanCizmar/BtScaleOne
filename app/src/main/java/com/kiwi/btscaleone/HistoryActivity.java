package com.kiwi.btscaleone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener{

    private Menu menu;
    private Toolbar toolbar;
    private static ListView lv_history;

    private final static String TAG = "HISTORY";

    private ArrayAdapter listAdapterH;
    private  List<String> dataList2 = new ArrayList<>();
    private AlertDialog.Builder build;

    // navigation button
    private  ImageView btn_main;
    private  ImageView btn_user;
    private  ImageView btn_trend;

    private LinearLayout ll_h1;



    private String userId = "";
    private String nickname = "";
    private String gender = "";
    private String height = "";
    private String birth = "";

    private String date = "";
    private String time = "";
    private String weight = "";
    private String bmi = "";
    private String bfr = "";
    private String bmr = "";
    private String slm = "";
    private String thr = "";

    public static Boolean EnableDel= false;

    private SQLiteDatabase dataBase;
    protected Context context;

    private ArrayList<String> sId = new ArrayList<String>();            // id

    private ArrayList<String> sUser = new ArrayList<String>();          // user
    private ArrayList<String> sNick = new ArrayList<String>();          // nick
    private ArrayList<String> sGender = new ArrayList<String>();        // gender
    private ArrayList<String> sBirth = new ArrayList<String>();         // birth
    private ArrayList<String> sHeight = new ArrayList<String>();        // height
    private ArrayList<String> sFigure = new ArrayList<String>();        // figure

    private ArrayList<String> sDate = new ArrayList<String>();          // date
    private ArrayList<String> sTime = new ArrayList<String>();          // time

    private ArrayList<String> sWeight = new ArrayList<String>();        // weight
    private ArrayList<String> sBMI = new ArrayList<String>();           // bmi
    private ArrayList<String> sBFR = new ArrayList<String>();           // fat
    private ArrayList<String> sBMR = new ArrayList<String>();           // bmr
    private ArrayList<String> sSLM = new ArrayList<String>();           // muscle
    private ArrayList<String> sTHR = new ArrayList<String>();           // water

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.history);
            //supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        lv_history = findViewById(R.id.lvHistory);

        /** load shift from preferences */
        loadPreference();

        // navigation buttons
        btn_main = findViewById(R.id.ibMain);
        btn_main.setOnClickListener(this);

        btn_user = findViewById(R.id.ibUsers);
        btn_user.setOnClickListener(this);

        btn_trend = findViewById(R.id.ibTrends);
        btn_trend.setOnClickListener(this);

        ll_h1 = findViewById(R.id.ll_h1);

        if(Build.VERSION.SDK_INT >= 29) {
            int currentNightMode = this.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO: {
                    Log.i("MODE4", " LIGHT");

                    ll_h1.setBackgroundResource(R.drawable.shadow_light);

                    break;
                }
                case Configuration.UI_MODE_NIGHT_YES:
                    //return true;
                {
                    Log.i("MODE4", " DARK");
                    setTheme(R.style.AppTheme2);

                    ll_h1.setBackgroundResource(R.drawable.shadow_dark);

                    break;
                }
            }
        }



        /// add data list from main activity
        displayData();

        // goto detail activity
        lv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.i(TAG, "Click to id " + arg2);

                Intent detailIntent = new Intent(HistoryActivity.this, DetailActivity.class);

                detailIntent.putExtra("DATE", sDate.get(arg2));
                detailIntent.putExtra("TIME", sTime.get(arg2));

                detailIntent.putExtra("WEIG", sWeight.get(arg2));
                detailIntent.putExtra("BMI", sBMI.get(arg2));
                detailIntent.putExtra("BFR", sBFR.get(arg2));
                detailIntent.putExtra("BMR", sBMR.get(arg2));
                detailIntent.putExtra("SLM", sSLM.get(arg2));
                detailIntent.putExtra("THR", sTHR.get(arg2));

                startActivity(detailIntent);
            }
        });


        /** Deleting item **/

            lv_history.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                    build = new AlertDialog.Builder(HistoryActivity.this);
                    build.setTitle(R.string.delete_data);
                    build.setMessage(R.string.realy_delete);
                    build.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            Log.i("DELETE", sId.get(arg2) + " is deleted.");

                            dataBase.delete(DbHelper.TABLE_NAME, DbHelper.KEY_ID + " = " + sId.get(arg2), null);
                            displayData();
                            dialog.cancel();
                        }
                    });

                    build.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = build.create();
                    alert.show();

                    return true;
                }
            });


    }

    private void loadPreference() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Zygaria", 0);

        userId = pref.getString("user", "0");  //"" is the default value.
        nickname = pref.getString("nickname", "");  //"" is the default value.
        gender = pref.getString("gender", "0");  //"" is the default value.
        birth = pref.getString("birth", "0");  //"" is the default value.
        height = pref.getString("height", "0");  //"" is the default value.

    }

    private void displayData() {

        dataBase = (new DbHelper(this)).getWritableDatabase();

        Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where " + "nick  LIKE ? AND weight != ''"+" ORDER BY id DESC" , new String[]{nickname});

        //Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where " + "nick  LIKE ?" , new String[]{nickname});

        //Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where nick = 'Michal'"    , null);
        //Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME, null);

        sId.clear();

        sUser.clear();
        sNick.clear();
        sGender.clear();
        sHeight.clear();
        sBirth.clear();

        sDate.clear();
        sTime.clear();

        sWeight.clear();
        sBMI.clear();
        sBFR.clear();
        sBMR.clear();
        sSLM.clear();
        sTHR.clear();

        if (mCursor.moveToFirst()) {
            do {
                sId.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_ID)));

                sUser.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_USER)));
                sNick.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_NICK)));
                sGender.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_GENDER)));
                sHeight.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_HEIGHT)));
                sBirth.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_BIRTH)));

                sDate.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_DATE)));
                sTime.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_TIME)));

                sWeight.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_WEIGHT)));
                sBMI.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_BMI)));
                sBFR.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_BFR)));
                sBMR.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_BMR)));
                sSLM.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_SLM)));
                sTHR.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_THR)));


            } while (mCursor.moveToNext());
        }
        Log.e("HISTORY ACTIVITY" , "" + sDate);

        HistoryAdapter disadpt = new HistoryAdapter(HistoryActivity.this, sId, sGender, sDate, sTime, sWeight, sBMI, sBFR, sBMR, sSLM, sTHR);
        lv_history.setAdapter( disadpt);

        mCursor.close();
    }

    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }

/**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.history_del, menu);
        return true;
    }
**/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_del:
                if(!EnableDel) {
                    EnableDel = true;
                    item.setTitle("Complete");
                }
                else {
                    EnableDel = false;
                    item.setTitle("Delete");

                }
                lv_history.invalidateViews();
                return true;
            case android.R.id.home:
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {

        // navigation buttons
        if (v.getId() == R.id.ibMain) {
            Log.i(TAG, "Click to Main");
            Intent userIntent = new
                    Intent(HistoryActivity.this, MainActivity.class);
            startActivity(userIntent);


        }
        if (v.getId() == R.id.ibUsers) {
            Log.i(TAG, "Click to Users");
            Intent userIntent = new
                    Intent(HistoryActivity.this, UserActivity.class);
            startActivity(userIntent);


        }
        if (v.getId() == R.id.ibTrends) {
            Log.i(TAG, "Click to Trends");
            Intent userIntent = new
                    Intent(HistoryActivity.this, CharActivity.class);
            startActivity(userIntent);


        }
    }
}

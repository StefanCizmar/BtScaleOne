package com.kiwi.btscaleone;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import static java.util.Collections.min;

public class CharActivity extends AppCompatActivity implements View.OnClickListener{

    private Menu menu;
    private Toolbar toolbar;
    private DatePickerDialog StartTime;
    private final static String TAG = "TREND";

    // navigation button
    private  ImageView btn_main;
    private  ImageView btn_user;
    private  ImageView btn_history;

    // data button
    private  ImageView btn_wei;
    private  ImageView btn_bmi;
    private  ImageView btn_bfr;
    private  ImageView btn_thr;
    private  ImageView btn_slm;
    private  ImageView btn_bmr;

    private LinearLayout ll_t1;
    private LinearLayout ll_t2;

    private  ImageView vChart;

    private String userId = "";
    private String nickname = "";
    private String gender = "";
    private String height = "";
    private String birth = "";

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

    private ArrayList<String> sD = new ArrayList<String>();           // current


    private int selD = 0;
    private int sCol;

    private DecimalFormat df = new DecimalFormat("#.#");
    private DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();

    private String first_date;
    private String end_date;
    private String date;

    private boolean full = false;

    private boolean night = false;

    /**  For draw**/

    private ImageView ivChar;

    int Width;       // Width trend
    int Height;      // height trend
    int stepX;
    //int stepY;

    int minStep;

    float mlt;    // multiplier
    float c1;
    float c2;

    float max;
    float min;
    float avg;

    int dot_size = 15;
    int line_size = 10;

    //int button = 0;

    private Bitmap bitmap;
    private Canvas canvas;

    Paint pFrame = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint scaleXY = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint pDot = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint pText = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint pLine = new Paint(Paint.ANTI_ALIAS_FLAG);

    //String part[] = new String[] {  };
    String part1;
    String part2;
    String part3;
    String part4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_char);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.trend);
            //supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        loadPreferences();
        displayData();
        sD = sWeight;
        sCol = getResources().getColor(R.color.colorWeight);


        ivChar = findViewById(R.id.viewChar);

        ivChar.post(new Runnable() {
            @Override
            public void run() {
                Width = ivChar.getWidth();
                Height = ivChar.getHeight();



                drawChar();
            }
        });

        // navigation buttons
        btn_main = findViewById(R.id.ibMain);
        btn_main.setOnClickListener(this);

        btn_user = findViewById(R.id.ibUsers);
        btn_user.setOnClickListener(this);

        btn_history = findViewById(R.id.ibHistory);
        btn_history.setOnClickListener(this);

        // data buttons
        btn_wei =findViewById(R.id.charWei);
        btn_wei.setOnClickListener(this);

        btn_bmi =findViewById(R.id.charBmi);
        btn_bmi.setOnClickListener(this);

        btn_bfr =findViewById(R.id.charBfr);
        btn_bfr.setOnClickListener(this);

        btn_bmr =findViewById(R.id.charCalorie);
        btn_bmr.setOnClickListener(this);

        btn_slm =findViewById(R.id.charMuscle);
        btn_slm.setOnClickListener(this);

        btn_thr =findViewById(R.id.charWater);
        btn_thr.setOnClickListener(this);

        btn_wei.setImageResource(R.drawable.reo_weight);

        ll_t1 = findViewById(R.id.ll_t1);
        ll_t2 = findViewById(R.id.ll_t2);
        vChart = findViewById(R.id.viewChar);

        if(Build.VERSION.SDK_INT >= 29) {
            int currentNightMode = this.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO: {
                    Log.i("MODE4", " LIGHT");
                    night = false;
                    ll_t1.setBackgroundResource(R.drawable.shadow_light);
                    ll_t2.setBackgroundResource(R.drawable.shadow_light);
                    vChart.setBackgroundColor(Color.WHITE);

                    break;
                }
                case Configuration.UI_MODE_NIGHT_YES:
                    //return true;
                {
                    Log.i("MODE4", " DARK");
                    night = true;
                    ll_t1.setBackgroundResource(R.drawable.shadow_dark);
                    ll_t2.setBackgroundResource(R.drawable.shadow_dark);
                    vChart.setBackgroundColor(Color.BLACK);





                    break;
                }
            }
        }


    }

    private void drawChar() {

        Log.i("Width2", String.valueOf(Width));
        Log.i("Height2", String.valueOf(Height));

        float tSize;
        tSize = Width / 30f;

        if((sWeight.size() + 1) > 11) {
            dot_size = 10;
            line_size = 5;
        }


        stepX = Width / (sWeight.size() + 1) ;
        minStep = Width / 11;
        //if(sWeight.size() >  10) Width = sWeight.size() * minStep;


        Log.i("STEP", String.valueOf(stepX + " "+ sWeight.size() * minStep));


        findMax(); // return max, min, avg from data


        bitmap = Bitmap.createBitmap(Width , Height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        /** DRAW FRAME**/
        if(!night) {
            pFrame.setColor(Color.BLACK);
        }
        else pFrame.setColor(Color.WHITE);
        pFrame.setStyle(Paint.Style.STROKE);
        pFrame.setStrokeWidth(2);

        RectF r = new RectF(10, 10, Width - 10 , Height - 10);
        canvas.drawRect(r, pFrame);

        /** DRAW X AND Y AXIS and texts **/

        if(!night) {
            scaleXY.setColor(Color.BLACK);
        }
        else scaleXY.setColor(Color.WHITE);
        scaleXY.setStyle(Paint.Style.STROKE);
        scaleXY.setStrokeWidth(2);

        float[] pts = {20, 20,
                20, Height - 20,
                20 , Height - 20,
                Width -20, Height - 20};
        canvas.drawLines(pts,scaleXY);

        if(!night) {
            pText.setColor(Color.BLACK);
        }
        else pText.setColor(Color.WHITE);
        pText.setStyle(Paint.Style.FILL);
        pText.setStrokeWidth(2);


        pText.setTextSize(tSize);
        int xStart = 0 ;

        if(sWeight.size() >  0 && sWeight.size() <  11) {
            for (int i = 0; i < sWeight.size() ; i++) {
                xStart = xStart + stepX;
                String day = sDate.get(i);
                StringTokenizer st = new StringTokenizer(day, ".");
                part1 = String.valueOf(Integer.parseInt(st.nextToken()));
                part2 = String.valueOf(Integer.parseInt(st.nextToken()));
                part3 = st.nextToken();
                part4 = part1 + "." + part2;

                Log.i("SPLITS", "" + part1);

                canvas.drawText(String.valueOf(part4), xStart, Height - 30, pText);
            }
        }

        /** DRAW DOTS, TEXTS, LINES **/
        // draw if arrays is not empty
        if(sWeight.size() >  0) {

            pDot.setColor(sCol);
            //pText.setColor(Color.BLACK);

            pText.setStyle(Paint.Style.FILL);

            pLine.setColor(sCol);
            pLine.setStyle(Paint.Style.STROKE);
            pLine.setStrokeWidth(line_size);

            // draw avg
            canvas.drawText(getString(R.string.avg)+ " " + String.valueOf(avg), stepX, tSize + 10, pText);


            Path path = new Path();

            xStart = 0;
            for (int i = 0; i < sWeight.size(); i++) {

                float pos1;
                float pos2;

                c1 = Float.parseFloat(sD.get(i));
                if (i < sD.size() - 1) c2 = Float.parseFloat(sD.get(i + 1));

                pos1 = getHPos(c1);
                pos2 = getHPos(c2);


                xStart = xStart + stepX;

                    // original
                //canvas.drawCircle(xStart, Height - 20 - (Float.parseFloat(sD.get(i)) * mlt), 10, pDot);
                // new
                if(sWeight.size() < 16 )canvas.drawCircle(xStart, pos1, dot_size, pDot);

                    // original
                //canvas.drawText(String.valueOf(sD.get(i)), xStart, Height - 40 - (Float.parseFloat(sD.get(i)) * mlt), pText);
                // new
                if(sWeight.size() < 11) canvas.drawText(String.valueOf(sD.get(i)), xStart, pos1 - 20, pText);


                // draw lines
                //if(i < sD.size() - 1) canvas.drawLine(xStart, Height - 20 - (Float.parseFloat(sD.get(i)) * mlt), xStart + stepX, Height - 20 - (Float.parseFloat(sD.get(i + 1))* mlt), pLine);
                //if(i < sD.size() - 1) canvas.drawLine(xStart, Height - 20 - (Float.parseFloat(sD.get(i)) * mlt), xStart + stepX, Height - 20 - (Float.parseFloat(sD.get(i + 1))* mlt), pLine);


                /** draw curved lines **/
                /**
                 if(i < sD.size() - 1) {
                 path.moveTo(xStart, Height - 20 - (Float.parseFloat(sD.get(i)) * mlt));
                 path.cubicTo(xStart + (stepX / 2), Height - 20 - (Float.parseFloat(sD.get(i)) * mlt), xStart + (stepX / 2), Height - 20 - (Float.parseFloat(sD.get(i + 1)) * mlt), xStart + stepX, Height - 20 - (Float.parseFloat(sD.get(i + 1)) * mlt));
                 canvas.drawPath(path, pLine);
                 }
                 **/
                /** draw new curved lines **/
                if (i < sD.size() - 1) {
                    path.moveTo(xStart, pos1);
                    path.cubicTo(xStart + (stepX / 2), pos1, xStart + (stepX / 2), pos2, xStart + stepX, pos2);
                    canvas.drawPath(path, pLine);
                }


            }
        }

        /**  show chart**/
        ivChar.setImageBitmap(bitmap);

    }

    private float getHPos(float c1) {

        float k1;
        float k2;
        float p1;
        float p2;
        float diff;

        diff = max - min;
        if(diff == 0) diff = 1;

        k1 = (Height / 2f) / diff;

        k2 = k1 * min / Height;

        if(k2 < 0) k2 = k2 + (Height);
        if(k2 > Height) k2 = k2 - (Height);

        p1 = (k1 * c1) - (k2 * Height) - (Height / 4f);
        p2 = (Height - 20) - ((k1 * c1) - (k2 * Height)) - (Height / 4f) ;

        Log.i(TAG, "K1 " + k1 + " k2 " + k2 + " p1 " + p1 + " p2 " + p2);
        return p2;
    }

    private void   findMax() {
        // if arrays is not empty

        if(sWeight.size() >  0) {

            // max
            max = Float.parseFloat(sD.get(0));
            for (int i = 0; i < sD.size(); i++) {
                if (Float.parseFloat(sD.get(i)) > max) {
                    max = Float.parseFloat(sD.get(i));
                }
            }

            // min
            min = Float.parseFloat(sD.get(0));
            for (int i = 0; i < sD.size(); i++) {
                if (Float.parseFloat(sD.get(i)) < min) {
                    min = Float.parseFloat(sD.get(i));
                }
            }

            // average

            float sum = 0;
            for (int i = 0; i < sD.size(); i++) {
                sum = sum + Float.parseFloat(sD.get(i));
            }
            avg = sum / sD.size();

            avg = FloatFormat(avg);

            // multiplier
            mlt = (Height / max) * 0.8f;

            Log.i(TAG, "INFO MAX MIN " + max + " " + min + " " + avg + " " + sD.size());
        }
    }

    private float FloatFormat(float value) {
        float data;
        double dataD;
        String dataStr = String.valueOf(value);

        dataD = Double.valueOf(dataStr);
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        dataStr = df.format(dataD);
        data = Float.parseFloat(dataStr);
        return data;
    }

    private void displayData() {

        dataBase = (new DbHelper(this)).getWritableDatabase();
        int rows;
        int offset;

        /** get number rows**/
        Cursor cursor = dataBase.rawQuery("SELECT * FROM weighing  where " + "nick  LIKE ? AND weight != ''" + "  ORDER BY id  ASC ", new String[]{nickname});
        rows = cursor.getCount();
        offset = rows -10;

        Log.i(TAG, "ROWS " + rows);
        cursor.close();


        Cursor mCursor;

        /**
         selected by date no limit
         */
        if(end_date != null && !end_date.isEmpty() &&  first_date != null && !first_date.isEmpty() ) {
             mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where " + "nick  LIKE ? AND weight != '' AND date  BETWEEN ? AND ?" + " ORDER BY id ASC", new String[]{nickname, first_date, end_date});
        }
        else if(full) {
            mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where " + "nick  LIKE ? AND weight != ''"+" ORDER BY id ASC" , new String[]{nickname});

            //String week = "0";
            //String avg_W = "0";
            //String avg_BMI = "0";
            //String avg_BFR = "0";


            //mCursor = dataBase.rawQuery("SELECT " +  nickname,week + " = DATEPART(wk, date  )", avg_W + "  = AVG(weight)",avg_BMI + " = AVG(bmi)",avg_BFR + " = AVG(bfr) FROM weighing GROUP BY " +  nickname, "DATEPART(wk, date)");

        }
        else

        /**
         * limit 10
         */ {

            mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where " + "nick  LIKE ? AND weight != ''" + " ORDER BY id ASC limit "+ offset +", 10", new String[]{nickname});

            // offset and limit
            //mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where " + "nick  LIKE ? AND weight != ''" + "  ORDER BY id  ASC LIMIT 5 OFFSET 3;  ", new String[]{nickname});


        }

        /** for all records **/
        //Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where " + "nick  LIKE ? AND weight != ''"+" ORDER BY id ASC" , new String[]{nickname});

        // old
        //Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where " + "nick  LIKE ?" , new String[]{nickname});

        //Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where nick = 'Michal'"    , null);
        // Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME, null);

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
        Log.e("CHAR ACTIVITY" , "" + sDate);

        mCursor.close();
        dataBase.close();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    private void loadPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Zygaria", 0);

        userId = pref.getString("user", "0");  //"" is the default value.
        nickname = pref.getString("nickname", "");  //"" is the default value.
        gender = pref.getString("gender", "0");  //"" is the default value.
        birth = pref.getString("birth", "0");  //"" is the default value.
        height = pref.getString("height", "0");  //"" is the default value.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.trend_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_start_date) {

            full = false;

            Calendar newCalendar = Calendar.getInstance();
            StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);

                    String s_month = ""+ (monthOfYear + 1) ;
                    String s_day = ""+ (dayOfMonth ) ;

                    if((monthOfYear + 1) < 10){
                        s_month = "0" + (monthOfYear + 1);
                    }

                    if((dayOfMonth) < 10){
                        s_day = "0" + (dayOfMonth);
                    }

                    first_date = s_day + "." + s_month+ "." + year;

                    Log.i("DATE2 ","" + first_date);
                }

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


            StartTime.show();
        }

            if (id == R.id.action_stop_date) {

                full = false;

                Calendar newCalendar = Calendar.getInstance();
                StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        String s_month = ""+ (monthOfYear + 1) ;
                        String s_day = ""+ (dayOfMonth ) ;

                        if((monthOfYear + 1) < 10){
                            s_month = "0" + (monthOfYear + 1);
                        }

                        if((dayOfMonth) < 10){
                            s_day = "0" + (dayOfMonth);
                        }

                        end_date = s_day + "." + s_month+ "." + year;

                        // show trend after date select
                        displayData();
                        drawChar();


                        Log.i("DATE2 ","" + end_date);
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


                StartTime.show();
            }
        if (id == R.id.action_whole) {

            full = true;
            displayData();
            drawChar();

        }
            if (id == android.R.id.home) {
                finish();

            }

                return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {

        //sD.clear();     // clear array before switching

        if (v.getId() == R.id.charWei) {
            selD = 0;
            sD = sWeight;
            sCol = getResources().getColor(R.color.colorWeight);

            btn_wei.setImageResource(R.drawable.reo_weight);
            btn_bmi.setImageResource(R.drawable.re_bmi);
            btn_bfr.setImageResource(R.drawable.re_bfr);
            btn_slm.setImageResource(R.drawable.re_muscle);
            btn_thr.setImageResource(R.drawable.re_water);
            btn_bmr.setImageResource(R.drawable.re_calorie);


            drawChar();

            // for test
            Log.i("START DATE2 ","" + first_date);

            Log.i("END DATE2 ","" + end_date);


            Log.i(TAG,""+ sD);
        }
        if (v.getId() == R.id.charBmi) {
            selD = 1;
            sD = sBMI;
            sCol = getResources().getColor(R.color.colorBMI);

            btn_wei.setImageResource(R.drawable.re_weight);
            btn_bmi.setImageResource(R.drawable.reo_bmi);
            btn_bfr.setImageResource(R.drawable.re_bfr);
            btn_slm.setImageResource(R.drawable.re_muscle);
            btn_thr.setImageResource(R.drawable.re_water);
            btn_bmr.setImageResource(R.drawable.re_calorie);


            drawChar();
            Log.i(TAG,""+ sD);

        }
        if (v.getId() == R.id.charBfr) {
            selD = 2;
            sD = sBFR;
            sCol = getResources().getColor(R.color.colorBFR);

            btn_wei.setImageResource(R.drawable.re_weight);
            btn_bmi.setImageResource(R.drawable.re_bmi);
            btn_bfr.setImageResource(R.drawable.reo_bfr);
            btn_slm.setImageResource(R.drawable.re_muscle);
            btn_thr.setImageResource(R.drawable.re_water);
            btn_bmr.setImageResource(R.drawable.re_calorie);


            drawChar();

            Log.i(TAG,""+ selD);

        }
        if (v.getId() == R.id.charMuscle) {
            selD = 3;
            sD = sSLM;
            sCol = getResources().getColor(R.color.colorSLM);

            btn_wei.setImageResource(R.drawable.re_weight);
            btn_bmi.setImageResource(R.drawable.re_bmi);
            btn_bfr.setImageResource(R.drawable.re_bfr);
            btn_slm.setImageResource(R.drawable.reo_muscle);
            btn_thr.setImageResource(R.drawable.re_water);
            btn_bmr.setImageResource(R.drawable.re_calorie);


            drawChar();

            Log.i(TAG,""+ selD);

        }
        if (v.getId() == R.id.charWater) {
            selD = 4;
            sD = sTHR;
            sCol = getResources().getColor(R.color.colorTHR);

            btn_wei.setImageResource(R.drawable.re_weight);
            btn_bmi.setImageResource(R.drawable.re_bmi);
            btn_bfr.setImageResource(R.drawable.re_bfr);
            btn_slm.setImageResource(R.drawable.re_muscle);
            btn_thr.setImageResource(R.drawable.reo_water);
            btn_bmr.setImageResource(R.drawable.re_calorie);


            drawChar();

            Log.i(TAG,""+ selD);

        }
        if (v.getId() == R.id.charCalorie) {
            selD = 5;
            sD = sBMR;
            sCol = getResources().getColor(R.color.colorBMR);

            btn_wei.setImageResource(R.drawable.re_weight);
            btn_bmi.setImageResource(R.drawable.re_bmi);
            btn_bfr.setImageResource(R.drawable.re_bfr);
            btn_slm.setImageResource(R.drawable.re_muscle);
            btn_thr.setImageResource(R.drawable.re_water);
            btn_bmr.setImageResource(R.drawable.reo_calorie);

            drawChar();

            Log.i(TAG,""+ selD);

        }

        // navigation buttons
        if (v.getId() == R.id.ibMain) {
            Log.i(TAG, "Click to Main");
            Intent userIntent = new
                    Intent(CharActivity.this, MainActivity.class);
            startActivity(userIntent);


        }
        if (v.getId() == R.id.ibUsers) {
            Log.i(TAG, "Click to Users");
            Intent userIntent = new
                    Intent(CharActivity.this, UserActivity.class);
            startActivity(userIntent);


        }
        if (v.getId() == R.id.ibHistory) {
            Log.i(TAG, "Click to History");
            Intent userIntent = new
                    Intent(CharActivity.this, HistoryActivity.class);
            startActivity(userIntent);


        }
    }




}
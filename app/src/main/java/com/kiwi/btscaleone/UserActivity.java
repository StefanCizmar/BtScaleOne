package com.kiwi.btscaleone;

import android.app.DatePickerDialog;
import android.content.ContentValues;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserActivity   extends AppCompatActivity implements  View.OnClickListener {
    private static final String TAG = UserActivity.class.getSimpleName();

    private Menu menu;
    private Toolbar toolbar;

    // database
    protected SQLiteDatabase db;
    protected Context context;

    // objects
    // navigation button
    private ImageView btn_main;
    private ImageView btn_trend;
    private ImageView btn_history;

    private ImageView btn_add;
    private ImageView btn_edit;


    private LinearLayout layoutTable;
    private LinearLayout layoutOk;
    private TextView tv_name1, tv_name2, tv_birth, tv_gender, tv_age, tv_height, et_Height;
    private EditText ed_height;
    private Button btn_ok;
    private RadioGroup rg_sex;
    private RadioGroup rg_unit;

    private RadioButton rb_male;
    private RadioButton rb_female;

    private RadioButton rb_kg;
    private RadioButton rb_lb;

    private LinearLayout ll_u1;
    private LinearLayout ll_u2;
    private LinearLayout ll_u3;


    private ListView lv_users;
    private AlertDialog.Builder build;

    private Date date;
    private Calendar mCalendar;

    private String ID;
    private String user = "0";
    private String nick;
    private String gender;
    private String height;
    private String birth;
    private String unit;

    private int year;
    private int month;
    private int day;
    private int nUsers = 0;

    // db objects
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

    Boolean rowExists;
    Boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.user));
            //supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // calling database
        db = (new DbHelper(this)).getWritableDatabase();

        tv_name2 = findViewById(R.id.etName);
        tv_name2.setOnClickListener(this);
        tv_name2.setText("");
        tv_name2.setHint(R.string.empty_name);

        tv_name1 = findViewById(R.id.tvName);
        tv_name1.setOnClickListener(this);


        tv_gender = findViewById(R.id.tvGender);
        tv_age = findViewById(R.id.tvAge);
        tv_height = findViewById(R.id.tvHeight);

        ed_height = findViewById(R.id.edHeight);
        ed_height.setText("");
        ed_height.setHint(R.string.empty_height);

        btn_ok = findViewById(R.id.btOK);
        btn_ok.setOnClickListener(this);

        tv_birth = findViewById(R.id.tvBirth);
        tv_birth.setOnClickListener(this);
        tv_birth.setText("");
        tv_birth.setHint(R.string.empty_date);

        rg_sex = findViewById(R.id.rgSex);
        rg_sex.check(R.id.rbMale);              // default male

        rg_unit = findViewById(R.id.rgUnit);
        rg_unit.check(R.id.rbKg);               // default kg

        rb_male = findViewById(R.id.rbMale);
        rb_female = findViewById(R.id.rbFemale);

        rb_kg = findViewById(R.id.rbMale);

        lv_users = findViewById(R.id.lv_users);

        // navigation buttons
        btn_main = findViewById(R.id.ibMain);
        btn_main.setOnClickListener(this);

        btn_trend = findViewById(R.id.ibTrends);
        btn_trend.setOnClickListener(this);

        btn_history = findViewById(R.id.ibHistory);
        btn_history.setOnClickListener(this);

        btn_add = findViewById(R.id.ivAdd);
        btn_add.setOnClickListener(this);

        btn_edit = findViewById(R.id.ivEdit);
        btn_edit.setOnClickListener(this);

        layoutTable = findViewById(R.id.llTable);
        layoutTable.setVisibility(View.GONE);
        layoutTable.setOnClickListener(this);

        layoutOk = findViewById(R.id.llOk);
        layoutOk.setOnClickListener(this);
        layoutOk.setVisibility(View.GONE);

        ll_u1 = findViewById(R.id.ll_u1);
        ll_u3 = findViewById(R.id.ll_u3);
        ll_u2 = findViewById(R.id.ll_u2);


        if(Build.VERSION.SDK_INT >= 29) {
            int currentNightMode = this.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO: {
                    Log.i("MODE4", " LIGHT");

                    ll_u1.setBackgroundResource(R.drawable.shadow_light);
                    ll_u3.setBackgroundResource(R.drawable.shadow_light);
                    ll_u2.setBackgroundResource(R.drawable.shadow_light);
                    tv_name1.setTextColor(Color.BLACK);
                    tv_name2.setTextColor(Color.BLACK);
                    tv_gender.setTextColor(Color.BLACK);
                    tv_age.setTextColor(Color.BLACK);
                    tv_height.setTextColor(Color.BLACK);
                    ed_height.setTextColor(Color.BLACK);
                    tv_birth.setTextColor(Color.BLACK);
                    rb_male.setTextColor(Color.BLACK);
                    rb_female.setTextColor(Color.BLACK);

                    tv_name2.setHintTextColor(Color.GRAY);
                    ed_height.setHintTextColor(Color.GRAY);
                    tv_birth.setHintTextColor(Color.GRAY);
                    break;
                }
                case Configuration.UI_MODE_NIGHT_YES:
                    //return true;
                {
                    Log.i("MODE4", " DARK");
                    setTheme(R.style.AppTheme2);

                    ll_u1.setBackgroundResource(R.drawable.shadow_dark);
                    ll_u3.setBackgroundResource(R.drawable.shadow_dark);
                    ll_u2.setBackgroundResource(R.drawable.shadow_dark);
                    tv_name1.setTextColor(Color.WHITE);
                    tv_name2.setTextColor(Color.WHITE);
                    tv_gender.setTextColor(Color.WHITE);
                    tv_age.setTextColor(Color.WHITE);
                    tv_height.setTextColor(Color.WHITE);
                    ed_height.setTextColor(Color.WHITE);
                    tv_birth.setTextColor(Color.WHITE);
                    rb_male.setTextColor(Color.WHITE);
                    rb_female.setTextColor(Color.WHITE);

                    tv_name2.setHintTextColor(Color.GRAY);
                    ed_height.setHintTextColor(Color.GRAY);
                    tv_birth.setHintTextColor(Color.GRAY);


                    break;
                }
            }
        }

        getCurdate();

        getISEmty();

        /** Select users **/
        lv_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                ID =  sId.get(arg2);
                user =  sUser.get(arg2);
                nick =  sNick.get(arg2);
                gender =  sGender.get(arg2);
                birth =  sBirth.get(arg2);
                height =  sHeight.get(arg2);

                Log.i("SELECT USER"," "+ nick);

                savePreferences();

                tv_name2.setText(nick);
                if(gender.equalsIgnoreCase("0"))
                rg_sex.check(R.id.rbFemale);
                else  rg_sex.check(R.id.rbMale);
                tv_birth.setText(birth);
                ed_height.setText(height);

            }
        });

        /** Deleting user **/
        lv_users.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

                build = new AlertDialog.Builder(UserActivity.this);
                build.setTitle(getString(R.string.delete) + " "+ sNick.get(arg2) + "?");
                //build.setMessage(R.string.realy_delete);
                build.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        Log.i( "DELETE", sNick.get(arg2) + " is deleted.");

                        db.delete( DbHelper.TABLE_NAME, DbHelper.KEY_USER +" LIKE ?", new String[]{sUser.get(arg2)});
                        loadData();

                        if(rowExists) {
                            lv_users.setSelection(0);

                            ID = sId.get(0);
                            user = sUser.get(0);
                            nick = sNick.get(0);
                            gender = sGender.get(0);
                            birth = sBirth.get(0);
                            height = sHeight.get(0);
                            savePreferences();
                        }

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

        initPreferences();


    }

    private void initPreferences() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Zygaria", 0);

        user = pref.getString("user", "0");  //"" is the default value.
        nick = pref.getString("nickname", "");  //"" is the default value.
        gender = pref.getString("gender", "0");  //"" is the default value.
        birth = pref.getString("birth", "0");  //"" is the default value.
        height = pref.getString("height", "0");  //"" is the default value.
    }

    private void getCurdate() {

        mCalendar = Calendar.getInstance();
        year = mCalendar.get(java.util.Calendar.YEAR);
        month = mCalendar.get(java.util.Calendar.MONTH);
        day = mCalendar.get(java.util.Calendar.DAY_OF_MONTH);

    }

    private void getISEmty() {

        Cursor mCursor = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME, null);
        String selectQuery = "SELECT max(user) as id FROM weighing";


        if (mCursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            rowExists = true;

            loadData();
            Cursor cu = db.rawQuery(selectQuery, null);

            cu.moveToFirst();

            int maxid = cu.getInt(cu.getColumnIndex("id"));

            nUsers = maxid + 1;
            Log.i("N_USERS", "" + (nUsers ));

        }
        else
        {
            // table is empty
            rowExists = false;
            Log.e(TAG, "TABLE IS EMPTY");

            // show edit dialog for name
            layoutTable.setVisibility(View.VISIBLE);
            layoutOk.setVisibility(View.VISIBLE);
            createEditdialog();
        }
    }

    private void loadData() {


        Cursor mCursor1 = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " where " + "weight IS NULL"+" ORDER BY id DESC" ,null);
        //Cursor mCursor1 = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME, null);

        sId.clear();
        sUser.clear();
        sGender.clear();
        sNick.clear();
        sBirth.clear();
        sHeight.clear();

        if (mCursor1.moveToFirst()) {
            do {
                sId.add(mCursor1.getString(mCursor1.getColumnIndex(DbHelper.KEY_ID)));
                sUser.add(mCursor1.getString(mCursor1.getColumnIndex(DbHelper.KEY_USER)));
                sGender.add(mCursor1.getString(mCursor1.getColumnIndex(DbHelper.KEY_GENDER)));
                sNick.add(mCursor1.getString(mCursor1.getColumnIndex(DbHelper.KEY_NICK)));
                sBirth.add(mCursor1.getString(mCursor1.getColumnIndex(DbHelper.KEY_BIRTH)));
                sHeight.add(mCursor1.getString(mCursor1.getColumnIndex(DbHelper.KEY_HEIGHT)));
            } while (mCursor1.moveToNext());
        }

        UserAdapter disadpt = new UserAdapter(UserActivity.this, sId, sUser, sGender, sNick, sBirth, sHeight);
        lv_users.setAdapter( disadpt);

        Log.i("USERS", ""+ sNick);

        mCursor1.close();

    }

    private void createEditdialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserActivity.this);
        alertDialog.setTitle(R.string.enter_nickname);
        //alertDialog.setMessage("Enter Nick");

        final EditText input = new EditText(UserActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_action_user_m);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        nick = input.getText().toString();
                        tv_name2.setText(nick);
                    }
                });

        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
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

    @Override
    public void onClick(View v) {

        // navigation button
        if (v.getId() == R.id.ibMain) {
            Log.i(TAG, "Click to Main");
            Intent userIntent = new Intent(UserActivity.this, MainActivity.class);
            startActivity(userIntent);

        }
        if (v.getId() == R.id.ibHistory) {
            Log.i(TAG, "Click to History");
            Intent userIntent = new Intent(UserActivity.this, HistoryActivity.class);
            startActivity(userIntent);

        }
        if (v.getId() == R.id.ibTrends) {
            Log.i(TAG, "Click to Trends");
            Intent userIntent = new Intent(UserActivity.this, CharActivity.class);
            startActivity(userIntent);

        }

        if (v.getId() == R.id.ivAdd) {
            layoutTable.setVisibility(View.VISIBLE);
            layoutOk.setVisibility(View.VISIBLE);
            edit = false;
            //tv_name2.setText("");
            //tv_birth.setText("");
            //ed_height.setText("");
            rg_sex.check(R.id.rbMale);

            createEditdialog();
        }

        if (v.getId() == R.id.ivEdit) {

            layoutOk.setVisibility(View.VISIBLE);
            layoutTable.setVisibility(View.VISIBLE);
            edit = true;
            if(tv_name2.getText().toString().equalsIgnoreCase("") ) {
                tv_name2.setText(nick);
                tv_birth.setText(birth);
                ed_height.setText(height);

            }


            //editRecords();
            //loadData();
            //lv_users.invalidateViews();
        }

        // next buttons
        if (v.getId() == R.id.etName) {
            createEditdialog();
        }

        if (v.getId() == R.id.tvName) {
            createEditdialog();
        }

        if (v.getId() == R.id.tvBirth) {
            DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    StringBuffer strBuf = new StringBuffer();

                    if(dayOfMonth <10) strBuf.append("0");
                    strBuf.append(dayOfMonth);
                    strBuf.append(".");

                    if(month <10) strBuf.append("0");
                    strBuf.append(month+1);
                    strBuf.append(".");

                    strBuf.append(year);

                    TextView datePickerValueTextView = findViewById(R.id.tvBirth);
                    datePickerValueTextView.setText(strBuf.toString());
                }
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(UserActivity.this, android.R.style.Theme_Holo_Light_Dialog, onDateSetListener, year, month, day);
            datePickerDialog.show();
        }


        /** button OK  add user
         *
         */
        if (v.getId() == R.id.btOK) {

            if(edit == false) {
                nUsers += 1;
                user = String.valueOf(nUsers - 1);

                nick = tv_name2.getText().toString();
                if (nick.equalsIgnoreCase(""))
                    createEditdialog();

                if (rb_male.isChecked()) gender = "1";
                else gender = "0";

                birth = tv_birth.getText().toString();
                if (birth.equalsIgnoreCase("")) showAlert();

                height = ed_height.getText().toString();
                if (height.equalsIgnoreCase("")) showHeight();

                if (rb_kg.isChecked()) unit = "0";
                else unit = "1";

                // add to db -> record
                if (!nick.equalsIgnoreCase("") && !height.equalsIgnoreCase("") && !birth.equalsIgnoreCase(""))
                    saveRecords();

            }
            if(edit == true) {



                nick = tv_name2.getText().toString();
                if (nick.equalsIgnoreCase(""))
                    createEditdialog();

                if (rb_male.isChecked()) gender = "1";
                else gender = "0";

                birth = tv_birth.getText().toString();
                if (birth.equalsIgnoreCase("")) showAlert();

                height = ed_height.getText().toString();
                if (height.equalsIgnoreCase("")) showHeight();

                if (rb_kg.isChecked()) unit = "0";
                else unit = "1";

                // update - record

                if( !nick.equalsIgnoreCase("")  &&  !height.equalsIgnoreCase("") && !birth.equalsIgnoreCase(""))
                    showEditDialog();


            }

            //
            loadData();
            lv_users.invalidateViews();

            layoutOk.setVisibility(View.GONE);
            layoutTable.setVisibility(View.GONE);

            // info
            Log.i(TAG, ""+ height + " " + nick + " "+ birth +" "+ gender+" "+ unit);

            savePreferences();

        }
    }

    private void showEditDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.rewrite_user);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_alert);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                editRecords();
                savePreferences();
                loadData();
                lv_users.invalidateViews();
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();

    }

    private void savePreferences() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Zygaria", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("user", user);
        editor.putString("nickname", nick);
        editor.putString("gender", gender);
        editor.putString("height", height);
        editor.putString("birth", birth);

        editor.commit(); // commit changes
        Log.e("SAVED user", "" + user + " "+ nick);

    }

    private void editRecords() {

        ContentValues values = new ContentValues();

        values.put(DbHelper.KEY_USER, user); // inserting a string
        values.put(DbHelper.KEY_NICK, nick); // inserting a string
        values.put(DbHelper.KEY_BIRTH, birth); // inserting an string
        values.put(DbHelper.KEY_HEIGHT, height); // inserting an string
        values.put(DbHelper.KEY_GENDER, gender); // inserting an string

        // update user
        db.update(DbHelper.TABLE_NAME, values, "user = ?", new String[]{user});
    }

    private void saveRecords() {

        ContentValues values = new ContentValues();

        values.put(DbHelper.KEY_USER, user); // inserting a string
        values.put(DbHelper.KEY_NICK, nick); // inserting a string
        values.put(DbHelper.KEY_BIRTH, birth); // inserting an string
        values.put(DbHelper.KEY_HEIGHT, height); // inserting an string
        values.put(DbHelper.KEY_GENDER, gender); // inserting an string

        // Inserting Row
        db.insert(DbHelper.TABLE_NAME, null, values);
        //db.close(); // Closing database connection

    }

    private void showHeight() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.enter_height);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_alert);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                // Write your code here to invoke YES event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    private void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.enter_birth);


        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_alert);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                // Write your code here to invoke YES event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}
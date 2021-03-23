package com.kiwi.btscaleone;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chipsea.bleprofile.BleProfileService;
import com.chipsea.bleprofile.BleProfileServiceReadyActivity;
import com.chipsea.entity.BodyFatData;
import com.chipsea.entity.BroadData;
import com.chipsea.entity.CsFatScale;
import com.chipsea.entity.User;
import com.chipsea.utils.BleConfig;
import com.chipsea.utils.L;
import com.chipsea.utils.ParseData;
import com.chipsea.wby.WBYService;
import com.kiwi.btscaleone.scan.DeviceDialog;
import com.kiwi.btscaleone.utils.T;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BleProfileServiceReadyActivity implements DeviceDialog.OnDeviceScanListener, View.OnClickListener {

    private final static String TAG = "ZYGARIA";
    private Menu menu;
    private Toolbar toolbar;
    private DeviceDialog devicesDialog;
    private Vibrator vibrator;

    private Button btn_version;
    private Button btn_scan;

    private ImageView btn_weight;
    private ImageView btn_main;
    private ImageView btn_users;
    private ImageView btn_history;
    private ImageView btn_trends;

    private LinearLayout ll_m1;
    private LinearLayout ll_m2;
    private LinearLayout ll_m3;
    private LinearLayout ll_m4;

    private ImageView iv_Bt;

    private RadioGroup rg_change_unit;

    private TextView  tv_weight,  tv_device, tv_status, tv_unit;

    private ListView lv_results;
    private ArrayAdapter listAdapter;


    static final List<String> dataList = new ArrayList<>();

    private User user = null;
    private final List<User> userList = new ArrayList<>();

    private boolean showListView = false;
    private byte unit = BleConfig.UNIT_KG;

    private BroadData cacheBroadData;
    private CsFatScale cacheWeight;

    private WBYService.WBYBinder binder;
    private boolean isBLEEnabled = false;

    // database
    protected SQLiteDatabase db;
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

    private Boolean rowExists;

    private String userId = "";
    private String nickname = "";
    private String gender = "";
    private String height = "";
    private String birth = "";

    private float NAge;

    private  float different = 0.0f;

    /** fields for results **/
    String[] results ={"BMI","Weight", "Fat","Water", "BMR","SLM" };
    String[] values ={ "v_bmi","v_weight", "v_bfr","v_b_water", "v_bmr", "v_muscle" };

    Integer[] imgid={
            R.drawable.re_bmi,
            R.drawable.re_weight,
            R.drawable.re_bfr,
            R.drawable.re_water,
            R.drawable.re_calorie,
            R.drawable.re_muscle     };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //initPermission(); move to scanning

        // load user from preferences
        initPreferences();

        initUser();

        //initViews();
        initViewsN();
        setDefault();
        //initEvents();

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        // calling database
        db = (new DbHelper(this)).getWritableDatabase();

        getISEmty();




        if (!ensureBLESupported()) {
            T.showShort(this, R.string.not_support_ble);
            L.e(TAG, "Not support BLE");

            //finish();
        }
        if (!isBLEEnabled()) {
            showBLEDialog();
        }

        /** create device dialog at start app */
        devicesDialog = new DeviceDialog(this, this);
        //deviceBind = new DeviceBind(this, (DeviceBind.OnDeviceScanListener) this);

    }

    private void initPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Zygaria", 0);

        userId = pref.getString("user", "0");  //"" is the default value.
        nickname = pref.getString("nickname", "");  //"" is the default value.
        gender = pref.getString("gender", "0");  //"" is the default value.
        birth = pref.getString("birth", "0");  //"" is the default value.
        height = pref.getString("height", "0");  //"" is the default value.

    }

    private void getISEmty() {
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME, null);

        if (mCursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            rowExists = true;

        }
        else
        {
            // table is empty
            rowExists = false;
            Log.e("TABLE", "IS EMPTY");

            // go to user activity for create user
            startActivityForResult(new Intent(this, UserActivity.class), 1);

        }
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            //Location permission is not turned on
            //Enable positioning permission, 200 is the identification code
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            T.showShort(this, "Location permission is turned on ");
            //Toast.makeText(MainActivity.this, "Location permission is turned on ", Toast.LENGTH_LONG).show();
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            //Location permission is not turned on
            //Enable positioning permission, 200 is the identification code
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH}, 201);
        } else {
            T.showShort(this, "Location permission is turned on ");
            //Toast.makeText(MainActivity.this, "Bluetooth permission is turned on ", Toast.LENGTH_LONG).show();
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {

            //Location permission is not turned on
            //Enable positioning permission, 200 is the identification code
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 202);
        } else {
            T.showShort(this, "Bluetooth admin permission is enabled ");
            //Toast.makeText(MainActivity.this, "Bluetooth admin permission is enabled ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.binder == null) {
            bindService(null);

            initPreferences();
            setSupportActionBar(toolbar);
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(nickname);
            }
        }
    }

    private void initUser() {

        // 0x00 WOMAN, 0x01 MAN
        // weight is target

        /** Adc def = 1000, new = 630  **/

        getNAge();
        int ID = Integer.parseInt(userId);

        Byte sex;
        if(gender.equalsIgnoreCase("0")) sex = 0x00;
        else sex = 0x01;
        user = new User(ID, sex, (int) NAge, Float.parseFloat(height), 720, 630  );

        // original
        //user = new User(0, (byte) 0x01, 53, 185, 720, 630  );

    }

    private void getNAge() {
        if (!birth.equalsIgnoreCase("0")) {

            SimpleDateFormat nf = new SimpleDateFormat("dd.MM.yyyy");

            String currDate = nf.format(new Date());

            Date d1 = null;
            Date d2 = null;
            try {
                d2 = nf.parse("01.01.2000");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // first
            try {
                d1 = nf.parse(currDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                d2 = nf.parse(birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference_In_Time = d1.getTime() - d2.getTime();
            long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

            NAge = difference_In_Years;
            Log.i("AGE", "" + difference_In_Years);
        }
        else NAge = 1;
    }


    private void initViewsN() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.app_name));

            if(!nickname.equalsIgnoreCase("")) supportActionBar.setTitle(nickname);
            //supportActionBar.setTitle(getString(R.string.app_name) + " V" + BuildConfig.VERSION_NAME);

        }

        rg_change_unit = findViewById(R.id.rgUnit);
        rg_change_unit.check(R.id.rbKg);

        btn_version = findViewById(R.id.buVersion);
        btn_version.setOnClickListener(this);

        btn_scan = findViewById(R.id.buScaning);
        btn_scan.setOnClickListener(this);

        /** custom Adapter */
        lv_results = findViewById(R.id.lv_results);

        ResultListAdapter adapter = new ResultListAdapter(this, results, values, imgid);

        lv_results.setAdapter( adapter);

        tv_weight = findViewById(R.id.tvWeight);
        setWeightText();
        tv_unit = findViewById(R.id.tv_unit);
        tv_device = findViewById(R.id.tvDevice);
        tv_status = findViewById(R.id.tvStatus);
        tv_status.setText("");
        tv_device.setText("");

        btn_users = findViewById(R.id.ibUsers);
        btn_users.setOnClickListener(this);

        btn_history = findViewById(R.id.ibHistory);
        btn_history.setOnClickListener(this);

        btn_trends = findViewById(R.id.ibTrends);
        btn_trends.setOnClickListener(this);

        btn_main = findViewById(R.id.ibMain);
        btn_main.setOnClickListener(this);

        btn_weight = findViewById(R.id.ibWeighing);
        btn_weight.setOnClickListener(this);

        iv_Bt = findViewById(R.id.ivBtstat);

        ll_m1 = findViewById(R.id.ll_m1);
        ll_m2 = findViewById(R.id.ll_m2);
        ll_m3 = findViewById(R.id.ll_m3);
        ll_m4 = findViewById(R.id.ll_m4);

        if(Build.VERSION.SDK_INT >= 29) {
            int currentNightMode = this.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO: {
                    Log.i("MODE4", " LIGHT");

                    ll_m1.setBackgroundResource(R.drawable.shadow_light);
                    ll_m2.setBackgroundResource(R.drawable.shadow_light);
                    ll_m3.setBackgroundResource(R.drawable.shadow_light);
                    ll_m4.setBackgroundResource(R.drawable.shadow_light);
                    tv_weight.setTextColor(Color.BLACK);
                    tv_device.setTextColor(Color.BLACK);
                    tv_status.setTextColor(Color.BLACK);
                    tv_unit.setTextColor(Color.BLACK);

                    break;
                }
                case Configuration.UI_MODE_NIGHT_YES:
                    //return true;
                {
                    Log.i("MODE4", " DARK");
                    setTheme(R.style.AppTheme2);

                    ll_m1.setBackgroundResource(R.drawable.shadow_dark);
                    ll_m2.setBackgroundResource(R.drawable.shadow_dark);
                    ll_m3.setBackgroundResource(R.drawable.shadow_dark);
                    ll_m4.setBackgroundResource(R.drawable.shadow_dark);
                    tv_weight.setTextColor(Color.WHITE);
                    tv_device.setTextColor(Color.WHITE);
                    tv_status.setTextColor(Color.WHITE);
                    tv_unit.setTextColor(Color.WHITE);

                    break;
                }
            }
        }

        // end night mode
    }

    private void setWeightText() {
        tv_weight.setText( String.valueOf(user.getWeight() / 10d));
    }

    private void setDefault() {
         tv_weight.setText(R.string.default_weight);
    }

    @Override
    public void onClick(View v) {

        // navigation buttons
        if (v.getId() == R.id.ibUsers) {

            Log.i(TAG, "Click to Users");
            Intent userIntent = new
                    Intent(MainActivity.this, UserActivity.class);
            startActivity(userIntent);
        }

        if (v.getId() == R.id.ibHistory) {

            Log.i(TAG, "Click to History");
            Intent userIntent = new
                    Intent(MainActivity.this, HistoryActivity.class);
            startActivity(userIntent);
        }

        if (v.getId() == R.id.ibTrends) {

            Log.i(TAG, "Click to Trend");
            Intent userIntent = new

                    Intent(MainActivity.this, CharActivity.class);
            startActivity(userIntent);
        }

        // weighing button
        if (v.getId() == R.id.ibWeighing) {

            Log.i(TAG, "Click to weighing");
            devicesDialog.show();
            devicesDialog.startScan();

        }


        // next buttons
        if (v.getId() == R.id.buVersion) {

            // for test
            saveResults();

        }

        if (v.getId() == R.id.buScaning) {

            initPermission();

            if (!isBLEEnabled()) {
                showBLEDialog();
            } else {
                if (isDeviceConnected()) {
                    binder.disconnect();
                } else {
                    if (cacheBroadData == null) {
                        devicesDialog.show();
                        devicesDialog.startScan();
                    } else {
                        cacheBroadData = null;
                        setStateTitle("", BleProfileService.STATE_DISCONNECTED);
                        stopLeScan();
                    }
                }
            }
            return;
        }

        if (isDeviceConnected()) {


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:

                initPermission();

                if (!isBLEEnabled()) {
                    showBLEDialog();
                } else {
                    if (isDeviceConnected()) {
                        binder.disconnect();

                    } else {
                        if (cacheBroadData == null) {
                            devicesDialog.show();
                            devicesDialog.startScan();
                            //item.setTitle(R.string.disconnect);
                        } else {
                            cacheBroadData = null;
                            setStateTitle("", BleProfileService.STATE_DISCONNECTED);
                            stopLeScan();
                            //item.setTitle(R.string.start_scan);

                        }
                    }
                }
                break;
           case R.id.action_saveDb:
                Intent intent2 =new Intent(this, SaveDbActivity.class);
                startActivity(intent2);
                break;
            case R.id.action_about:
                Intent int_about =new Intent(this, AboutActivity.class);
                startActivity(int_about);
                break;

            case R.id.action_unbind:
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Zygaria", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("Address", "0");


                editor.commit(); // commit changes

        }

        return true;
    }

    @Override
    protected void onServiceBinded(WBYService.WBYBinder binder) {
        this.binder = binder;
        L.e( TAG, "onService Binded: binder = " + binder);
    }

    @Override
    protected void onServiceUnbinded() {
        this.binder = null;
        L.e( TAG,  "onService Unbinded");
    }

    @Override
    protected void onDestroy() {

        stopScan();
        if (isDeviceConnected()) {
            this.binder.disconnect();
        }
        super.onDestroy();

    }

    private final Handler handler = new Handler();

    private void startLeScan() {
        startScan();
    }

    private void stopLeScan() {
        stopScan();
    }

    @Override
    public void scan() {
        startScan();
        devicesDialog.setScanning(true);
    }

    @Override
    public void stop() {
        stopScan();
        devicesDialog.setScanning(false);
    }

    @Override
    public void connect(BroadData device) {

        if (device.getDeviceType() == BleConfig.BM_CS) {
            cacheBroadData = device;

            // add  address device to preferences
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Zygaria", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("Address", String.valueOf(device.getAddress()));


            editor.commit(); // commit changes
            Log.e("SAVED Address", " "+ device.getAddress());


            showInfo(getString(R.string.state_bound, device.getAddress()));
            Log.i(TAG, "Connect: " + String.valueOf(device.getAddress()));

            setStateTitle(device.getAddress(), -1);
            //setStateTitle(device.getName(), -1);
            tv_device.setText(device.getName());
            startLeScan();
        } else {
            startConnect(device.getAddress());
        }

        //connection
        startConnect(device.getAddress());
    }

    @Override
    public void onStateChanged(String deviceAddress, int state) {
        super.onStateChanged(deviceAddress, state);
        Log.i(TAG, "onStateChanged  state " + state);
        switch (state) {
            case BleProfileService.STATE_CONNECTED:
                showInfo(getString(R.string.state_connected, deviceAddress));


                Log.i(TAG, "onStateChanged " + deviceAddress);

                setStateTitle(deviceAddress, state);
                break;
            case BleProfileService.STATE_DISCONNECTED:
                showInfo(getString(R.string.state_disconnected));
                Log.i(TAG, "onStateChanged disconnected");

                tv_device.setText("");
                setStateTitle(deviceAddress, state);
                break;
            case BleProfileService.STATE_SERVICES_DISCOVERED:
                showInfo(getString(R.string.state_service_discovered));
                Log.i(TAG, "services discovered");

                break;
            case BleProfileService.STATE_INDICATION_SUCCESS:

                /** here saveRecord **/

                /**
                if(BleConfig.WEIGHT > 0.0) {
                    saveResults();
                    vibrator.vibrate(250);
                }
                 **/
                showInfo(getString(R.string.state_indication_success));
                Log.i(TAG, "indication success");

                /**
                 * Sync users
                 */

                //original
                binder.syncUser(user);

                break;
            case BleProfileService.STATE_TIME_OUT:
                showInfo(getString(R.string.state_time_out));
                Log.i(TAG, "time_out");
                break;
            case BleProfileService.STATE_CONNECTING:
                showInfo(getString(R.string.state_connecting));
                Log.i(TAG, "onStateChanged connecting...");
                break;
        }
    }

    private void showInfo(String str) {

        Log.i(TAG," " + str);
        T.showShort(this, str);

        //Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();


        String time = ParseData.getCurrentTime() + "\n- " + str;
        dataList.add(time);

        //listAdapter.notifyDataSetChanged();
        //lv_data.setSelection(dataList.size() - 1);
    }

    private void setStateTitle(final String deviceAddress, final int state) {

        switch (state) {
            case BleProfileService.STATE_CONNECTED:
                L.e(TAG, "STATE_CONNECTED");
                btn_scan.setText(R.string.disconnect);
                tv_status.setText(R.string.conected);
                iv_Bt.setImageResource(R.drawable.ic_bt_on);
                break;

            case BleProfileService.STATE_DISCONNECTED:
                L.e(TAG, "STATE_DISCONNECTED");
                btn_scan.setText(R.string.start_scan);
                tv_status.setText("");
                tv_device.setText("");
                iv_Bt.setImageResource(R.drawable.ic_bt_off);

                //setDefault();
                break;

            case -1:
                btn_scan.setText(R.string.unbound);
                break;
        }
    }

    @Override
    public void onError(final String errMsg, final int errCode) {
        L.e(TAG, "Message = " + errMsg + " errCode = " + errCode);
        showInfo(getString(R.string.state_error, errMsg, errCode));
    }

    @Override
    protected void getBLEDevice(BroadData broadData) {
        if (broadData != null) {
            L.e(TAG, broadData.toString());
            L.e(TAG, "Is it the main thread：" + (Looper.myLooper() == Looper.getMainLooper()));
            if (devicesDialog.isShowing()) {
                devicesDialog.setDevice(broadData);
            }
            if (cacheBroadData != null && TextUtils.equals(cacheBroadData.getAddress(), broadData.getAddress())) {
            }
        }
    }

    @Override
    protected void onWeightData(boolean isHistory, CsFatScale csFatScale) {
        cacheWeight = csFatScale;

        L.e(TAG, "Weight: " + csFatScale.getWeight() / 10.0f + "");
        L.e(TAG, "BMI: " + BleConfig.BMI + "");

        setWeighDataText(csFatScale.getWeight() / 10.0f + "");

    }

    @Override
    protected void onBodyFatData(boolean isHistory, BodyFatData bodyFatData) {
        if (bodyFatData == null) {
            return;
        }

        /**  SAVE DATA IF IS DATA **/
        // if data is different

        /**
        if(different != BleConfig.WEIGHT ) {
            saveResults();
            vibrator.vibrate(250);

        }
        different = BleConfig.WEIGHT;
        **/
        Log.i("BFD", ">> isHistory >>" + isHistory + ">> BodyFatData " + bodyFatData.toString());
    }

    private void setWeighDataText(String weight) {

        tv_weight.setText( "" +  weight);

        if(different != BleConfig.WEIGHT ) {
            saveResults();
            vibrator.vibrate(250);

        }
        different = BleConfig.WEIGHT;
        // invalidate list view
        lv_results.invalidateViews();


    }

    private void saveResults() {

        L.i(TAG, "SAVE");

        //db = (new DbHelper(this)).getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put(DbHelper.KEY_USER, userId); // inserting a string

        values.put(DbHelper.KEY_NICK, nickname); // inserting a string
        values.put(DbHelper.KEY_BIRTH, birth); // inserting an string
        values.put(DbHelper.KEY_HEIGHT, height); // inserting an string
        values.put(DbHelper.KEY_GENDER, gender); // inserting an string

        /** testing **
         *

        values.put(DbHelper.KEY_WEIGHT, user.getAge()); // inserting an string
        values.put(DbHelper.KEY_BMI, String.valueOf(21.3f)); // inserting an string
        values.put(DbHelper.KEY_BFR, 23.3); // inserting an string
        values.put(DbHelper.KEY_BMR, 1700); // inserting an string
        values.put(DbHelper.KEY_SLM, 34.5f); // inserting an string
        values.put(DbHelper.KEY_THR, user.getHeight()); // inserting an string

        **/

        /** original **/

        values.put(DbHelper.KEY_WEIGHT, String.valueOf(BleConfig.WEIGHT)); // inserting an string // original
        values.put(DbHelper.KEY_BMI, String.valueOf(BleConfig.BMI)); // inserting an string
        values.put(DbHelper.KEY_BFR, String.valueOf(BleConfig.BFR)); // inserting an string
        int b;
        b = (int) BleConfig.BMR;
        values.put(DbHelper.KEY_BMR, String.valueOf( b  )); // inserting an string
        values.put(DbHelper.KEY_SLM, String.valueOf(BleConfig.SLM)); // inserting an string
        values.put(DbHelper.KEY_THR, String.valueOf(BleConfig.TFR)); // inserting an string


        /** Test **/
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

        String currDate = sdf.format(new Date());
        String currTime = sdf2.format(new Date());
        values.put(DbHelper.KEY_DATE, currDate); // inserting an string
        values.put(DbHelper.KEY_TIME, currTime); // inserting an string

        //values.put(DbHelper.KEY_DATE, BleConfig.currDate); // inserting an string
        //values.put(DbHelper.KEY_TIME, BleConfig.currTime); // inserting an string

        // Inserting Row
        db.insert(DbHelper.TABLE_NAME, null, values);
        // Closing database connection
        db.close();
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            binder.syncUser(new User());
        }
    };
}
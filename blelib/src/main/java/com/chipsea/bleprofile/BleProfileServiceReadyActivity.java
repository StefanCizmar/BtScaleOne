package com.chipsea.bleprofile;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.chipsea.entity.BodyFatData;
import com.chipsea.entity.BroadData;
import com.chipsea.entity.CsFatScale;
import com.chipsea.healthscale.CsAlgoBuilderEx;
import com.chipsea.healthscale.ScaleActivateStatusEvent;
import com.chipsea.utils.BleConfig;
import com.chipsea.utils.CsAlgoBuilderExUtil;
import com.chipsea.utils.L;
import com.chipsea.utils.ParseData;
import com.chipsea.wby.WBYService;

import static android.Manifest.permission.BLUETOOTH_ADMIN;

/**
 * @ClassName:BleProfileServiceReadyActivity
 * @PackageName:com.chipsea.bleprofile
 * @Create On 2019/3/24.
 * @Site:te:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/8/13  handongkeji All rights reserved.
 */
public abstract class BleProfileServiceReadyActivity<E extends WBYService.WBYBinder> extends AppCompatActivity {

    private final static String TAG = "BPSReady";

    protected static final int REQUEST_ENABLE_BT = 2;

    private E mService;

    private boolean mIsScanning = false;

    private BluetoothAdapter adapter = null;

    private BroadcastReceiver mCommonBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                bluetoothStateChanged(state);
            } else if (BleProfileService.ACTION_CONNECT_STATE_CHANGED.equals(action)) {
                int connectState = intent.getIntExtra(BleProfileService.EXTRA_CONNECT_STATE, -1);
                String address = intent.getStringExtra(BleProfileService.EXTRA_DEVICE_ADDRESS);
                onStateChanged(address, connectState);
            } else if (BleProfileService.ACTION_CONNECT_ERROR.equals(action)) {
                String errMsg = intent.getStringExtra(BleProfileService.EXTRA_ERROR_MSG);
                int errCode = intent.getIntExtra(BleProfileService.EXTRA_ERROR_CODE, -1);
                onError(errMsg, errCode);
            } else if (WBYService.ACTION_CSFAT_SCALE_DATA.equals(action)) {
                CsFatScale csFatScale = (CsFatScale) intent.getSerializableExtra(WBYService.EXTRA_CSFAT_SCALE_DATA);
                onWeightData(false, csFatScale);
            } else if (WBYService.ACTION_FAT_DATA.equals(action)) {
                BodyFatData bodyFatData = (BodyFatData) intent.getSerializableExtra(WBYService.EXTRA_FAT_DATA);
                boolean isHistory = intent.getBooleanExtra(WBYService.EXTRA_IS_HISTORY, false);
                onBodyFatData(isHistory, bodyFatData);
            }
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            final E bleService = mService = (E) service;
            onServiceBinded(bleService);
            // and notify user if device is connected
            if (bleService.isConnected())
                onStateChanged(bleService.getDeviceAddress(), BleProfileService.STATE_CONNECTED);
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            onServiceUnbinded();
        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onInitialize();
        bindService(null);
        getApplication().registerReceiver(mCommonBroadcastReceiver, makeIntentFilter());

    }

    protected void bindService(String address) {
        final Intent service = new Intent(this, WBYService.class);
        if (!TextUtils.isEmpty(address)) {
            service.putExtra(BleProfileService.EXTRA_DEVICE_ADDRESS, address);
            startService(service);
        }
        bindService(service, mServiceConnection, 0);
    }

    protected void onInitialize() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = bluetoothManager.getAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getApplication().unregisterReceiver(mCommonBroadcastReceiver);
        unbindService();
    }

    private static IntentFilter makeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BleProfileService.ACTION_CONNECT_STATE_CHANGED);
        intentFilter.addAction(BleProfileService.ACTION_CONNECT_ERROR);
        intentFilter.addAction(WBYService.ACTION_WEIGHT_DATA);
        intentFilter.addAction(WBYService.ACTION_CSFAT_SCALE_DATA);
        intentFilter.addAction(WBYService.ACTION_FAT_DATA);
        return intentFilter;
    }


    /**
     * Start connecting device
     *
     * @param address
     */
    public void startConnect(String address) {
        stopScan();//Need to stop scanning when connecting to the device
        bindService(address);
    }

    /**
     * Is it connected
     *
     * @return true:connected; false:not connected
     */
    protected boolean isDeviceConnected() {
        return mService != null && mService.isConnected();
    }

    /**
     * Whether to support BLE
     *
     * @return true:stand by; false:not support
     */
    protected boolean ensureBLESupported() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * Whether Bluetooth is on
     *
     * @return true:Turn on; false:Turn off
     */
    protected boolean isBLEEnabled() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    /**
     * Show the bluetooth-on prompt box
     */
    protected void showBLEDialog() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    /**
     * Is scanning
     *
     * @return true:Scanning; false:Scanning stopped
     */
    public boolean isScanning() {
        return mIsScanning;
    }

    /**
     * Start scanning
     */
    @RequiresPermission(BLUETOOTH_ADMIN)
    protected void startScan() {
        if (isBLEEnabled()) {
            if (!mIsScanning) {
                adapter.startLeScan(mLEScanCallback);
                mIsScanning = true;
                handler.postDelayed(stopScanRunnable, SCAN_DURATION);
            }
        } else {
            showBLEDialog();
        }
    }

    private Handler handler = new Handler();

    private static final int SCAN_DURATION = 60 * 1000;

    private Runnable startScanRunnable = new Runnable() {
        @Override
        public void run() {
            startScan();
        }
    };

    private Runnable stopScanRunnable = new Runnable() {
        @Override
        public void run() {
            stopScan();
            handler.post(startScanRunnable);
        }
    };

    /**
     * Stop scanning
     */
    @RequiresPermission(BLUETOOTH_ADMIN)
    protected void stopScan() {
        handler.removeCallbacks(startScanRunnable);
        handler.removeCallbacks(stopScanRunnable);
        if (mIsScanning) {
            if (adapter != null) {
                adapter.stopLeScan(mLEScanCallback);
            }
            mIsScanning = false;
        }
    }

    private final BluetoothAdapter.LeScanCallback mLEScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            L.e(TAG, "onLeScan");
            if (device != null) {
                L.e(TAG, "address: " + device.getAddress() + "; name: " + device.getName());
                L.e(TAG, ParseData.byteArr2Str(scanRecord));
                L.e(TAG, "name: " + device.getName() + "; address: " + device.getAddress());

                final BroadData bleInfo = BleConfig.getBroadData(device, rssi, scanRecord);
                if (bleInfo != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getBLEDevice(bleInfo);
                        }
                    });
                }
            }
        }
    };


    @RequiresPermission(BLUETOOTH_ADMIN)
    protected void bluetoothStateChanged(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_TURNING_OFF:
                if (mService != null) {
                    mService.disconnect();
                }
                stopScan();
                break;
        }
    }

    /**
     * Connection status change
     *
     * @param deviceAddress Device address
     * @param state         status
     * @see BleProfileService#STATE_CONNECTED
     * @see BleProfileService#STATE_DISCONNECTED
     * @see BleProfileService#STATE_INDICATION_SUCCESS
     * @see BleProfileService#STATE_SERVICES_DISCOVERED
     * @see BleProfileService#STATE_CONNECTED
     * @see BleProfileService#STATE_TIME_OUT
     */
    protected void onStateChanged(String deviceAddress, int state) {
        switch (state) {
            case BleProfileService.STATE_DISCONNECTED:
                unbindService();
                break;
            case BleProfileService.STATE_CONNECTED: //Successfully connected to the Bluetooth scale
                /**
                 * =================Please call the SDK activation method when there is a network connection after binding the Bluetooth scale.
                 * =================After the activation is successful, you do not need to call this method again =======================
                 * SDK activation SDK is not activated, there is a limit on the number of calls (500 times)，
                 * If the number of calls is exceeded, all the human body composition calculation interfaces return a result of 0 ;
                 * After the activation is successful, there is no limit. If the App is reinstalled or the user clears the cache, it needs to be reactivated
                 * If network problems cause onHttpError, you need to activate again
                 * @param mac The mac address of the Bluetooth scale, all capitals
                 *@param ScaleActivateStatusEvent Result callback This method is asynchronous and will not block the calling thread
                 */

                //SDK activation needs to declare the following permissions in Manifest.xml:
                //<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
                //<uses-permission android:name="android.permission.INTERNET" />
                //<uses-permission android:name="android.permission.READ_PHONE_STATE"/>

                CsAlgoBuilderExUtil.initCsAlgoBuilder(getApplicationContext());
                CsAlgoBuilderEx csAlgoBuilderEx = CsAlgoBuilderExUtil.getAlgoBuilderEx();
                if (!csAlgoBuilderEx.getAuthStatus()) {
                    //Activation method, please replace the mac address with the Bluetooth mac address of the Bluetooth scale for official activation
                    csAlgoBuilderEx.Authorize(deviceAddress, new ScaleActivateStatusEvent() {
                        @Override
                        public void onActivateSuccess() {
                            L.i(TAG, "Activated successfully!");
                        }

                        @Override
                        public void onActivateFailed() {
                            //If the activated mac address is activated too many times, or the mac address is not an authorized Bluetooth address, the SDK will be frozen
                            L.i(TAG, "Activation failed, SDK is frozen!");
                        }

                        @Override
                        public void onHttpError(int i, String s) {
                            L.i(TAG, "Activation failed, need to be reactivated, ErrCode:" + i);
                        }
                    });
                }
                break;
        }
    }

    private void unbindService() {
        try {
            unbindService(mServiceConnection);
            mService = null;
            onServiceUnbinded();
        } catch (final IllegalArgumentException e) {
            // do nothing. This should never happen but does...
        }
    }

    protected abstract void onServiceBinded(E binder);

    protected abstract void onServiceUnbinded();

    /**
     * Abnormal connection
     *
     * @param errMsg  Error message
     * @param errCode error code
     */
    protected abstract void onError(String errMsg, int errCode);


    /**
     * Get the match
     * Chipsea
     * Body fat scale
     *
     * @param broadData
     */
    protected abstract void getBLEDevice(BroadData broadData);

    /**
     * Get raw weight data
     *
     * @param isHistory
     * @param csFatScale
     */
    protected abstract void onWeightData(boolean isHistory, CsFatScale csFatScale);

    /**
     * Get body fat data
     *
     * @param isHistory
     * @param bodyFatData
     */
    protected abstract void onBodyFatData(boolean isHistory, BodyFatData bodyFatData);
}

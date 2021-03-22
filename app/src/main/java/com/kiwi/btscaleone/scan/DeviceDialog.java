package com.kiwi.btscaleone.scan;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chipsea.entity.BroadData;
import com.kiwi.btscaleone.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Suzy on 2016/10/26.
 */

public class DeviceDialog extends Dialog {

    private DeviceListAdapter adapter;
    private Button scanButton;
    private ImageView lvAni;
    private Context context;
    private OnDeviceScanListener listener;
    private boolean scanning;
    private List<BroadData> listValues;
    private ListView listView;
    private final BroadData.AddressComparator comparator = new BroadData.AddressComparator();

    private String addr;

    public void setScanning(boolean scanning) {
        this.scanning = scanning;
    }

    public interface OnDeviceScanListener {
        void scan();

        void stop();

        void connect(BroadData device);
    }

    public DeviceDialog(Context context, OnDeviceScanListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device);
        setTitle(R.string.scanner_title);
        listValues = new ArrayList<>();
        initViews();
        initEvents();
        initPreferences();
        animate();
    }

    private void animate() {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.clockwise);
        lvAni.startAnimation(animation);
    }

    private void initPreferences() {
        SharedPreferences pref = context.getSharedPreferences("Zygaria", 0);

        addr = pref.getString("Address", "0");  //"" is the default value.

        if(!addr.equalsIgnoreCase("0")) {
            listView.setVisibility(View.INVISIBLE);
            scanButton.setVisibility(View.INVISIBLE);
        }
    }

    private void initEvents() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                stopScanDevice();
                dismiss();
                listener.connect((BroadData) adapter.getItem(position));

            }
        });


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.action_cancel) {
                    if (scanning) {
                        stopScanDevice();
                        dismiss();
                    } else {
                        startScan();
                    }
                }
            }
        });
    }

    private void initViews() {
        listView = findViewById(android.R.id.list);
        adapter = new DeviceListAdapter(context, listValues);
        listView.setAdapter(adapter);
        scanButton = findViewById(R.id.action_cancel);
        lvAni = findViewById(R.id.ivAnimation);

    }

    private final static int SCAN_DURATION = 5000 * 1000000;

    private Handler handler = new Handler();

    public void startScan() {
        clearDevices();
        TextView textView = findViewById(android.R.id.empty);
        textView.setVisibility(View.GONE);
        /*if (listValues.isEmpty()) {
            listView.setEmptyView(textView);
        }*/
        scanButton.setText(R.string.scanner_action_cancel);
        if (!scanning) {
            listener.scan();
            handler.postDelayed(stopScanRunnable, SCAN_DURATION);
        }
    }

    private Runnable stopScanRunnable = new Runnable() {
        @Override
        public void run() {
            stopScanDevice();
        }
    };

    private void stopScanDevice() {
        if (scanning) {
            scanButton.setText(R.string.scanner_action_scan);
            listener.stop();
        }
    }

    public void setDevice(BroadData device) {
        comparator.address = device.getAddress();
        final int index = listValues.indexOf(comparator);
        if (index >= 0) {
            BroadData previousDevice = listValues.get(index);
            previousDevice.setRssi(device.getRssi());
            previousDevice.setName(device.getName());
            previousDevice.setBright(device.isBright());
            adapter.notifyDataSetChanged();


            // connect if device is bounded device

            //String addr = "C8:B2:1E:CA:58:A7";
            if(device.getAddress().equalsIgnoreCase(addr) && device.getDeviceType() == 15) {
                stopScanDevice();
                dismiss();
                listener.connect((BroadData) adapter.getItem(index));
            }
            return;
        }
        listValues.add(device);
        adapter.notifyDataSetChanged();



    }

    private void clearDevices() {
        listValues.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        handler.removeCallbacks(stopScanRunnable);
        stopScanDevice();
    }
}

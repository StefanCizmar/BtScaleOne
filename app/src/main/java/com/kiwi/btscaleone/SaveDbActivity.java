package com.kiwi.btscaleone;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SaveDbActivity   extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;



    String str = "0";
    int progress;
    static String DATABASE_NAME = "weighing";
    private Handler progressBarbHandler = new Handler();

    private LinearLayout ll_backup;
    private TextView textDestination;
    private TextView txtPerc;
    private TextView textDbv;
    private ProgressBar pb;

    private Button buttonSave;
    private Button buttonRecovery;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_db);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.save_database));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        buttonSave = this.findViewById(R.id.but_saveDb);
        buttonSave.setOnClickListener(this);
        buttonSave.setTextColor(getResources().getColor(R.color.colorWhite));
        buttonSave.setTransformationMethod(null);

        buttonRecovery = this.findViewById(R.id.but_recoveryDb);
        buttonRecovery.setOnClickListener(this);
        buttonRecovery.setTextColor(getResources().getColor(R.color.colorWhite));
        buttonRecovery.setTransformationMethod(null);

        pb = findViewById(R.id.progressBar1);
        pb.setProgress(0);
        pb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorBblue), PorterDuff.Mode.SRC_IN);

        txtPerc = this.findViewById(R.id.txtPerc);
        txtPerc.setText("");
        textDbv = this.findViewById(R.id.textDbv);
        textDestination= this.findViewById(R.id.txtNotification);

        ll_backup = this.findViewById(R.id.ll_backup);


         if(Build.VERSION.SDK_INT >= 29) {
             int currentNightMode = this.getResources().getConfiguration().uiMode
                     & Configuration.UI_MODE_NIGHT_MASK;
             switch (currentNightMode) {
                 case Configuration.UI_MODE_NIGHT_NO: {
                     Log.i("MODE4", " LIGHT");

                     ll_backup.setBackgroundResource(R.drawable.shadow_light);
                     txtPerc.setTextColor(Color.BLACK);
                     textDestination.setTextColor(Color.BLACK);

                     break;
                 }
                 case Configuration.UI_MODE_NIGHT_YES:{
                     Log.i("MODE4", " DARK");
                     setTheme(R.style.AppTheme2);

                     ll_backup.setBackgroundResource(R.drawable.shadow_dark);
                     txtPerc.setTextColor(Color.WHITE);
                     textDestination.setTextColor(Color.WHITE);

                     break;
                 }
             }
         }

    }

    /** copy DB file to SD card */
    public void copyDB()  {
        isWriteStoragePermissionGranted();
        if (isWriteStoragePermissionGranted()) {

            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            boolean success = true;

            if (success) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            File data = Environment.getDataDirectory();

                            String currentDBPath = "/data/" + "com.kiwi.btscaleone" + "/databases/" + DATABASE_NAME;
                            String backupDBPath =  DATABASE_NAME;

                            File currentDB = new File(data, currentDBPath);
                            File backupDB = new File(sd, backupDBPath);
                            final InputStream source = new FileInputStream(currentDB);
                            final OutputStream destination = new FileOutputStream(backupDB);

                            long lenght = currentDB.length();
                            Log.e("lenght", "" + lenght);

                            byte[] buffer = new byte[1024];
                            long total = 0;
                            int read;

                            while ((read = source.read(buffer)) != -1) {
                                total += read;
                                str = "" + (total * 100) / lenght;
                                Log.e("progress", str);
                                progress = (Integer.parseInt(str));
                                destination.write(buffer, 0, read);
                                Log.e("read", "" + total);

                                // Try to sleep the thread for 20 milliseconds
                                try {
                                    Thread.sleep(2);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                // Update the progress bar
                                progressBarbHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb.setProgress(progress);
                                        // Show the progress on TextView
                                        txtPerc.setText(progress + "%");

                                    }
                                });
                            }
                            try {
                                destination.flush();
                                Log.e("FLUSH", "Done");

                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            try {
                                source.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            try {
                                destination.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            txtPerc.post(new Runnable() {
                                @Override
                                public void run() {
                                    txtPerc.setText(R.string.save_done);
                                    pb.setProgress(0);
                                    Log.e("SAVE", "Done");

                                }
                            });

                            //Toast.makeText(save_events.this, "Events is backed up",  Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                        }
                    }
                }).start(); // Start the operation

            } else {
                //Toast.makeText(save_events.this, "Allow permission",  Toast.LENGTH_SHORT).show();


            }
            }
        }


    /** restore DB file to data folder */
    public void restoreDB(){

        isReadStoragePermissionGranted();
        if (isReadStoragePermissionGranted()) {

            File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File data = Environment.getDataDirectory();

            if (sd.canRead()) {
                String currentDBPath = "/data/" + "com.kiwi.btscaleone" + "/databases/" + DATABASE_NAME;
                String backupDBPath =  DATABASE_NAME;
                final File currentDB = new File(data, currentDBPath);
                final File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                final InputStream src = new FileInputStream(backupDB);
                                final OutputStream dst = new FileOutputStream(currentDB);
                                long lenght = backupDB.length();
                                byte[] buffer = new byte[1024];
                                int read;
                                long total = 0;

                                while ((read = src.read(buffer)) != -1) {
                                    total += read;
                                    str = "" + (total * 100) / lenght;
                                    Log.e("progress", str);
                                    progress = (Integer.parseInt(str));

                                    dst.write(buffer, 0, read);
                                    // Try to sleep the thread for 20 milliseconds
                                    try {
                                        Thread.sleep(2);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    // Update the progress bar
                                    progressBarbHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            pb.setProgress(progress);
                                            // Show the progress on TextView
                                            txtPerc.setText(progress + "%");

                                        }
                                    });

                                }
                                try {
                                    dst.flush();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    src.close();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                    dst.close();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                txtPerc.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtPerc.setText(R.string.recovery_done);
                                        pb.setProgress(0);
                                        Log.e("RECOVERY", "Done");

                                    }
                                });
                                //Toast.makeText(save_events.this, "Events is backed up",  Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                Log.e("RECOVERY", "impossible2");
                                // show alert dialog
                                ///showInfoImposs();

                                //Toast.makeText(SaveDbActivity.this, "Recovery is impossible", Toast.LENGTH_SHORT).show();



                            }
                        }
                    }).start();   // Start the operation
                }
            } else {
                showInfoImposs();
            }
        }

    }


    public boolean isReadStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission","Permission is granted1");
                return true;
            } else {

                Log.v("Permission","Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission","Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission","Permission is granted2");
                return true;
            } else {

                Log.v("Permission","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission","Permission is granted2");
            return true;
        }
    }



    public void showInfoImposs() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Info");

        builder2.setMessage("Recovery is impossible");
        builder2.setCancelable(true);

        builder2.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert12 = builder2.create();
        alert12.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.but_saveDb) {
            copyDB();
        }

        if (v.getId() == R.id.but_recoveryDb) {

            restoreDB();

            Log.e("onclick", "recovery");
        }

    }
}

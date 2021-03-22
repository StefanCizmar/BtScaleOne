package com.kiwi.btscaleone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity   extends AppCompatActivity implements  View.OnClickListener {

    private Toolbar toolbar;

    private Button okButton;
    private Button sendBtn;

    private LinearLayout ll_a;



    private TextView app_version;
    private TextView tv_Info;
    private TextView tv_c;

    private ImageView imScale;

    /**
     * strings for send email
     */
    private String recipient = "stefan.cizmar.pp@gmail.com";
    private String subject = "";
    private String body = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.app_name));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        okButton = findViewById(R.id.buttonOk2);
        sendBtn = findViewById(R.id.email_button);

        sendBtn.setTransformationMethod(null);

        okButton.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        app_version = findViewById(R.id.textInfoScale);
        tv_Info = findViewById(R.id.textInfo);
        tv_c = findViewById(R.id.textCopyright);
        ll_a = findViewById(R.id.ll_a);

        String appver = getString(R.string.app_name) + " v. " + BuildConfig.VERSION_NAME;
        app_version.setText(appver);
        subject = getString(R.string.app_name);
        body = getString(R.string.insert_text);

        imScale = findViewById(R.id.imageLogo);


        if(Build.VERSION.SDK_INT >= 29) {
            int currentNightMode = this.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO: {
                    Log.i("MODE4", " LIGHT");

                    ll_a.setBackgroundResource(R.drawable.shadow_light);
                    app_version.setTextColor(Color.BLACK);
                    tv_Info.setTextColor(Color.BLACK);
                    tv_c.setTextColor(Color.BLACK);
                    imScale.setImageResource(R.drawable.esperanza);
                    break;
                }
                case Configuration.UI_MODE_NIGHT_YES:
                    //return true;
                {
                    Log.i("MODE4", " DARK");

                    ll_a.setBackgroundResource(R.drawable.shadow_dark);
                    app_version.setTextColor(Color.WHITE);
                    tv_Info.setTextColor(Color.WHITE);
                    tv_c.setTextColor(Color.WHITE);
                    imScale.setImageResource(R.drawable.esperanza_white);

                    break;
                }
            }
        }

    }

        @Override
    public void onClick(View v) {

            if
            (v.getId() == R.id.buttonOk2) {
                finish();
            }

            if
            (v.getId() == R.id.email_button) {
                sendEmail();
            }

    }

    @SuppressLint("IntentReset")
    private void sendEmail() {

        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        // prompts email clients only
        email.setType("message/rfc822");

        // EXTRA_EMAIL must field !!!
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, body);

        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Choose an email client from..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AboutActivity.this, "No email client installed.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * choice from menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // app icon in action bar clicked; go home
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

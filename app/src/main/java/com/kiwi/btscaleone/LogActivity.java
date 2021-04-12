package com.kiwi.btscaleone;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kiwi.btscaleone.R;

import java.util.ArrayList;
import java.util.List;

public class LogActivity  extends AppCompatActivity {

    private Menu menu;
    private Toolbar toolbar;

    private ListView lv_data2;

    private ArrayAdapter listAdapter2;
    private final List<String> dataList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.app_name));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        lv_data2 = findViewById(R.id.lvLog2);
        /// add data list from main activity
        listAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, MainActivity.dataList);
        lv_data2.setAdapter(listAdapter2);
        listAdapter2.notifyDataSetChanged();

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

package com.kiwi.btscaleone;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chipsea.utils.BleConfig;
import com.kiwi.btscaleone.R;

public class ResultListAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] tresult;
    private final String[] tvalue;
    private final Integer[] imgid;
    protected SQLiteDatabase db;
    protected Context mcontext;

    private String user = "0";
    private String nick;
    private String gender;
    private String height;
    private String birth;
    private String unit;

    public ResultListAdapter(Activity context, String[] result,String[] value, Integer[] imgid) {
        super(context, R.layout.result_list_row, result);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.tresult=result;
        this.tvalue=value;
        this.imgid=imgid;

    }

    public View getView(final int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.result_list_row, null,true);

        TextView result = rowView.findViewById(R.id.tv_result_name);
        ImageView imageView = rowView.findViewById(R.id.iv_result);
        TextView value = rowView.findViewById(R.id.tv_result_value);

        imageView.setImageResource(imgid[position]);
        result.setText(tresult[position]);

        /** BMI **/
        if(position == 0) {
            tvalue[position] = String.valueOf(BleConfig.BMI);
            value.setText(tvalue[position]);
            result.setText(context.getString(R.string.bmi));

        }
        /** WEIGHT kg**/
        if(position == 1) {
            tvalue[position] = String.valueOf(BleConfig.WEIGHT);
            value.setText(String.format("%s Kg", tvalue[position]));
            result.setText(context.getString(R.string.re_weight));

        }
        /** BFR percent **/
        if(position == 2) {
            tvalue[position] = String.valueOf(BleConfig.BFR);
            value.setText(String.format("%s %%", tvalue[position]));
            result.setText(context.getString(R.string.re_fat) );
        }
        /** WATER percent **/
        if(position == 3) {
            tvalue[position] = String.valueOf(BleConfig.TFR);
            value.setText(String.format("%s %%", tvalue[position]));
            result.setText(context.getString(R.string.re_water) );

        }
        /** BMR calorie **/
        if(position == 4) {
            tvalue[position] = String.valueOf(BleConfig.BMR);
            int b;
            b = (int) BleConfig.BMR;

            tvalue[position] = String.valueOf( b  );
            value.setText(String.format("%s kcal", tvalue[position]));
            result.setText(context.getString(R.string.re_bmr));

        }
        /** SLM kg **/
        if(position == 5) {
            tvalue[position] = String.valueOf(BleConfig.SLM);
            value.setText(String.format("%s Kg", tvalue[position]));
            result.setText(context.getString(R.string.re_muscle) );

        }


        if(Build.VERSION.SDK_INT >= 29) {
            int currentNightMode = context.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO: {
                    Log.i("MODE4", " LIGHT");


                    value.setTextColor(Color.BLACK);
                    result.setTextColor(Color.BLACK);



                    break;
                }
                case Configuration.UI_MODE_NIGHT_YES:
                    //return true;
                {
                    Log.i("MODE4", " DARK");
                    //setTheme(R.style.AppTheme2);


                    value.setTextColor(Color.WHITE);
                    result.setTextColor(Color.WHITE);


                    break;
                }
            }
        }



        return rowView;
    }

}

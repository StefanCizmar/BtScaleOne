package com.kiwi.btscaleone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
@SuppressLint("InflateParams")
public class HistoryAdapter  extends BaseAdapter {

    protected Context context;

    private final ArrayList<String> hsId;            // id
    //private ArrayList<String> hsUser;          // user
    //private ArrayList<String> hsNick;          // nick
    private final ArrayList<String> hsGender;        // gender
    //private ArrayList<String> hsBirth;         // birth
    //private ArrayList<String> hsHeight;        // height
    //private ArrayList<String> hsFigure;        // figure

    private final ArrayList<String> hsDate;         // date
    private final ArrayList<String> hsTime;          // time

    private final ArrayList<String> hsWeight;        // weight
    private final ArrayList<String> hsBMI;           // bmi
    private final ArrayList<String> hsBFR;           // fat
    private final ArrayList<String> hsBMR;           // bmr
    private final ArrayList<String> hsSLM;           // muscle
    private final ArrayList<String> hsTHR;           // water




    public HistoryAdapter(Context c, ArrayList<String> sId , ArrayList<String> sGender, ArrayList<String> sDate, ArrayList<String> sTime, ArrayList<String> sWeight, ArrayList<String> sBMI, ArrayList<String> sBFR, ArrayList<String> sBMR, ArrayList<String> sSLM, ArrayList<String> sTHR) {
        this.context = c;
        this.hsId = sId;

        this.hsGender = sGender;

        this.hsDate = sDate;
        this.hsTime = sTime;

        this.hsWeight = sWeight;
        this.hsBMI = sBMI;
        this.hsBFR = sBFR;
        this.hsBMR = sBMR;
        this.hsSLM = sSLM;
        this.hsTHR = sTHR;
    }

    @Override
    public int getCount() {
        return hsId.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(final int position, View child, ViewGroup parent) {

        Holder mHolder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.history_list_row, parent, false);
            mHolder = new Holder();

            mHolder.iv_del = child.findViewById(R.id.ivDelete);
            mHolder.iv_HUser = child.findViewById(R.id.ivHUser);

            mHolder.txt_Date = child.findViewById(R.id.hisDate);
            mHolder.txt_Time = child.findViewById(R.id.hisTime);

            mHolder.txt_vWei = child.findViewById(R.id.hvWei);
            mHolder.txt_vBmi = child.findViewById(R.id.hvBMI);

            mHolder.txt_vBfr = child.findViewById(R.id.hvBFR);
            mHolder.txt_vThr = child.findViewById(R.id.hvTHR);
            mHolder.txt_vSlm = child.findViewById(R.id.hvSLM);
            mHolder.txt_vBmr = child.findViewById(R.id.hvBMR);

            mHolder.txt_Wei = child.findViewById(R.id.tvWei2);
            mHolder.txt_Bmi = child.findViewById(R.id.hBMI);
            mHolder.txt_hWei = child.findViewById(R.id.hWei);
            mHolder.txt_Perc = child.findViewById(R.id.tvPerc);
            mHolder.txt_Perc2 = child.findViewById(R.id.tvPerc2);
            mHolder.txt_hBFR = child.findViewById(R.id.hBFR);
            mHolder.txt_hTHR = child.findViewById(R.id.hTHR);



            mHolder.ll_ha = child.findViewById(R.id.ll_ha);


            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }

        mHolder.txt_Date.setText(hsDate.get(position));
        mHolder.txt_Time.setText(hsTime.get(position));

        mHolder.txt_vWei.setText(hsWeight.get(position));

        mHolder.txt_vBmi.setText(hsBMI.get(position));
        mHolder.txt_vBfr.setText(hsBFR.get(position));
        mHolder.txt_vThr.setText(hsTHR.get(position));
        mHolder.txt_vSlm.setText(hsSLM.get(position));
        mHolder.txt_vBmr.setText(hsBMR.get(position));

        mHolder.iv_del.setVisibility(View.GONE);

        /**
        if(HistoryActivity.EnableDel== true)
            mHolder.iv_del.setVisibility(View.VISIBLE);
        else             mHolder.iv_del.setVisibility(View.INVISIBLE);**/

        String sex;
        sex = hsGender.get(position);
        if(sex.equalsIgnoreCase("0")) mHolder.iv_HUser.setImageResource(R.drawable.ic_action_user_f);
        else mHolder.iv_HUser.setImageResource(R.drawable.ic_action_user_m);

        Log.i("ADAPTER", "" + hsBMI.get(position));


        if(Build.VERSION.SDK_INT >= 29) {
            int currentNightMode = context.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO: {
                    Log.i("MODE4", " LIGHT");

                    mHolder.ll_ha.setBackgroundResource(R.drawable.shadow_light);

                    mHolder.txt_Date.setTextColor(Color.BLACK);
                    mHolder.txt_Time.setTextColor(Color.BLACK);
                    mHolder.txt_vWei.setTextColor(Color.BLACK);
                    mHolder.txt_vBmi.setTextColor(Color.BLACK);
                    mHolder.txt_vBfr.setTextColor(Color.BLACK);
                    mHolder.txt_vThr.setTextColor(Color.BLACK);
                    mHolder.txt_Wei.setTextColor(Color.BLACK);
                    mHolder.txt_Bmi.setTextColor(Color.BLACK);
                    mHolder.txt_hWei.setTextColor(Color.BLACK);
                    mHolder.txt_Perc.setTextColor(Color.BLACK);
                    mHolder.txt_Perc2.setTextColor(Color.BLACK);
                    mHolder.txt_hBFR.setTextColor(Color.BLACK);
                    mHolder.txt_hTHR.setTextColor(Color.BLACK);


                    break;
                }
                case Configuration.UI_MODE_NIGHT_YES:
                    //return true;
                {
                    Log.i("MODE4", " DARK");
                    //setTheme(R.style.AppTheme2);

                    mHolder.ll_ha.setBackgroundResource(R.drawable.shadow_dark);

                    mHolder.txt_Date.setTextColor(Color.WHITE);
                    mHolder.txt_Time.setTextColor(Color.WHITE);
                    mHolder.txt_vWei.setTextColor(Color.WHITE);
                    mHolder.txt_vBmi.setTextColor(Color.WHITE);
                    mHolder.txt_vBfr.setTextColor(Color.WHITE);
                    mHolder.txt_vThr.setTextColor(Color.WHITE);
                    mHolder.txt_Wei.setTextColor(Color.WHITE);
                    mHolder.txt_Bmi.setTextColor(Color.WHITE);
                    mHolder.txt_hWei.setTextColor(Color.WHITE);
                    mHolder.txt_Perc.setTextColor(Color.WHITE);
                    mHolder.txt_Perc2.setTextColor(Color.WHITE);
                    mHolder.txt_hBFR.setTextColor(Color.WHITE);
                    mHolder.txt_hTHR.setTextColor(Color.WHITE);

                    break;
                }
            }
        }



        return child;
    }

    public class Holder {

        TextView txt_Date;
        TextView txt_Time;
        TextView txt_vWei;

        TextView txt_vBfr;
        TextView txt_vThr;
        TextView txt_vSlm;
        TextView txt_vBmr;
        TextView txt_vBmi;

        TextView txt_Wei;
        TextView txt_Bmi;
        TextView txt_hWei;
        TextView txt_Perc;
        TextView txt_Perc2;
        TextView txt_hTHR;
        TextView txt_hBFR;


        ImageView iv_del;
        ImageView iv_HUser;

        //
        LinearLayout ll_ha;


    }

}

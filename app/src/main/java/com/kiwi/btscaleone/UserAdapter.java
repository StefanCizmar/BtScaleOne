package com.kiwi.btscaleone;

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
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter  extends BaseAdapter {

    protected Context context;

    private ArrayList<String> hsId;            // id
    private ArrayList<String> hsUser;          // user
    private ArrayList<String> hsNick;          // nick
    private ArrayList<String> hsGender;        // gender
    private ArrayList<String> hsBirth;         // birth
    private ArrayList<String> hsHeight;        // height

    public UserAdapter(Context c, ArrayList<String> sId, ArrayList<String> sUser, ArrayList<String> sGender, ArrayList<String> sNick, ArrayList<String> sBirth, ArrayList<String> sHeight) {
        this.context = c;
        this.hsId = sId;
        this.hsUser = sUser;
        this.hsNick = sNick;
        this.hsGender = sGender;
        this.hsBirth = sBirth;
        this.hsHeight = sHeight;

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
            child = layoutInflater.inflate(R.layout.user_list_row, parent, false);
            mHolder = new Holder();

            mHolder.txt_Nick = child.findViewById(R.id.tv_Nickname);
            mHolder.iv_Nick = child.findViewById(R.id.iv_nick);

            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }

        mHolder.txt_Nick.setText(hsNick.get(position));

        // change image if gender is male or female
        String gender;
        gender = hsGender.get(position);

        if(gender.equalsIgnoreCase("0"))
            mHolder.iv_Nick.setImageResource(R.drawable.ic_action_user_f);
        else mHolder.iv_Nick.setImageResource(R.drawable.ic_action_user_m);


        if(Build.VERSION.SDK_INT >= 29) {
            int currentNightMode = context.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO: {
                    Log.i("MODE4", " LIGHT");



                    mHolder.txt_Nick.setTextColor(Color.BLACK);


                    break;
                }
                case Configuration.UI_MODE_NIGHT_YES:
                    //return true;
                {
                    Log.i("MODE4", " DARK");
                    //setTheme(R.style.AppTheme2);


                    mHolder.txt_Nick.setTextColor(Color.WHITE);

                    break;
                }
            }
        }


        return child;
    }

    public class Holder {

        TextView txt_Nick;
        ImageView iv_Nick;

    }
}

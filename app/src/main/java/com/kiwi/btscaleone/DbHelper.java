package com.kiwi.btscaleone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class DbHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weighing";
    public static final String TABLE_NAME = "weighing";
    public static final int DB_VERSION = 1 ;

    /** table weighing */
    public static final String KEY_ID = "id";
    public static final String KEY_USER = "user";           // 0 user
    public static final String KEY_NICK = "nick";           // 1 nick
    public static final String KEY_GENDER = "gender";       // 2 gender
    public static final String KEY_BIRTH = "birth";         // 3 birth
    public static final String KEY_HEIGHT = "height";       // 4 height
    public static final String KEY_FIGURE = "figure";       // 5 figure
    public static final String KEY_DATE = "date";           // 6 date
    public static final String KEY_TIME = "time";           // 7 time

    public static final String KEY_WEIGHT = "weight";       // 8 weight
    public static final String KEY_BMI = "bmi";             // 9 bmi
    public static final String KEY_BFR = "bfr";             // 10 fat
    public static final String KEY_BMR = "bmr";             // 11 bmr
    public static final String KEY_SLM = "slm";             // 12 muscle
    public static final String KEY_THR = "thr";             // 13 water

    protected Context context;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME+ " (" +KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_USER +" TEXT, "+
                KEY_NICK +" TEXT, "+
                KEY_GENDER +" TEXT, "+
                KEY_BIRTH +" TEXT, "+
                KEY_HEIGHT +" TEXT, "+
                KEY_FIGURE +" TEXT, "+
                KEY_DATE +" TEXT, "+
                KEY_TIME +" TEXT, "+
                KEY_WEIGHT +" TEXT, "+
                KEY_BMI +" TEXT, "+
                KEY_BFR +" TEXT, "+
                KEY_BMR +" TEXT, "+
                KEY_SLM +" TEXT, "+
                KEY_THR +" TEXT )";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS weighing");
        onCreate(db);
    }
}

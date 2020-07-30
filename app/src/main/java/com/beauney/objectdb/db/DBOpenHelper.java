package com.beauney.objectdb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.beauney.objectdb.db.manager.UserDBHelper;

import androidx.annotation.Nullable;

/**
 * @author zengjiantao
 * @since 2020-07-30
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "user.db";

    private static final int VERSION = 1;

    private volatile static DBOpenHelper instance;

    private DBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static DBOpenHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DBOpenHelper.class) {
                if (instance == null) {
                    instance = new DBOpenHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UserDBHelper.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserDBHelper.onUpgrade(db, oldVersion, newVersion);
    }
}

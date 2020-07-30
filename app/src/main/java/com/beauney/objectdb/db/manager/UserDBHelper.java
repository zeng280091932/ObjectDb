package com.beauney.objectdb.db.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.beauney.objectdb.db.DBOpenHelper;
import com.beauney.objectdb.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengjiantao
 * @since 2020-07-30
 */
public class UserDBHelper {

    private static final String TAG = "UserDBHelper";

    private static final String TABLE_NAME = "tb_user";

    private static final String[] COLUMS = {
            "username", "password"
    };

    private static DBOpenHelper dbOpenHelper;

    private volatile static UserDBHelper instance;

    private UserDBHelper(Context context) {
        dbOpenHelper = DBOpenHelper.getInstance(context);
    }

    public static UserDBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (UserDBHelper.class) {
                if (instance == null) {
                    instance = new UserDBHelper(context);
                }
            }
        }
        return instance;
    }

    /**
     * 创建表
     *
     * @param db
     */
    private static void createTable(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME + " ("
                + COLUMS[0] + " varchar primary key," + COLUMS[1] + " varchar)");
    }

    /**
     * 删除表
     *
     * @param db
     */
    private static void dropTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
    }

    public static void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db);
        createTable(db);
    }

    public void insert(User user) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMS[0], user.getUsername());
        values.put(COLUMS[1], user.getPassword());
        Log.d(TAG, "user------>" + user);
        Log.d(TAG, "values------>" + values);
        db.insert(TABLE_NAME, null, values);
    }

    public void update(User user) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMS[1], user.getPassword());
        Log.d(TAG, "user------>" + user);
        Log.d(TAG, "values------>" + values);
        db.update(TABLE_NAME, values, COLUMS[0] + "=?", new String[]{user.getUsername()});
    }

    public void delete(String username) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME + " where " + COLUMS[0] + "='" + username + "'");
    }

    public List<User> findAll() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        List<User> users = new ArrayList<>();
        try {
            if (cursor != null) {
                int usernameIndex = cursor.getColumnIndex(COLUMS[0]);
                int passwordIndex = cursor.getColumnIndex(COLUMS[1]);
                User user = null;
                while (cursor.moveToNext()) {
                    user = new User();
                    user.setUsername(cursor.getString(usernameIndex));
                    user.setPassword(cursor.getString(passwordIndex));
                    users.add(user);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "查询数据库出错" + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return users;
    }
}

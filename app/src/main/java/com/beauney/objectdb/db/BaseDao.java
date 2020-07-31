package com.beauney.objectdb.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.beauney.objectdb.db.annotation.DbField;
import com.beauney.objectdb.db.annotation.DbTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zengjiantao
 * @since 2020-07-31
 */
public abstract class BaseDao<T> implements IBaseDao<T> {
    private static final String TAG = "BaseDao";

    private SQLiteDatabase mSQLiteDatabase;

    /**
     * 实例类的引用
     */
    private Class<T> mClazz;

    /**
     * 维护表名称
     */
    private String mTableName;


    /**
     * 确保之初始化一次
     */
    private boolean mIsInit = false;

    protected synchronized void init(Class<T> clazz, SQLiteDatabase sqLiteDatabase) {
        if (!mIsInit) {
            mClazz = clazz;
            mSQLiteDatabase = sqLiteDatabase;
            //初始化表名称
            DbTable dbTable = clazz.getAnnotation(DbTable.class);
            if (dbTable != null) {
                mTableName = dbTable.value();
            } else {
                mTableName = clazz.getSimpleName();
            }

            if (!mSQLiteDatabase.isOpen()) {
                return;
            }

            //创建数据库表
            if (!TextUtils.isEmpty(createTable())) {
                mSQLiteDatabase.execSQL(createTable());
            }
            mIsInit = true;
        }
    }

    protected abstract String createTable();

    @Override
    public long insert(T entity) {
        ContentValues values = createContentValues(entity);
        Log.d(TAG, "values------>" + values);
        return mSQLiteDatabase.insert(mTableName, null, values);
    }

    @Override
    public int update(T entity, T where) {
        ContentValues values = createContentValues(entity);
        Condition condition = new Condition(mClazz, where);
        return mSQLiteDatabase.update(mTableName, values, condition.getWhereClause(), condition.getWhereArgs());
    }

    @Override
    public int delete(T where) {
        Condition condition = new Condition(mClazz, where);
        return mSQLiteDatabase.delete(mTableName, condition.getWhereClause(), condition.getWhereArgs());
    }

    class Condition {
        private String whereClause;

        private String[] whereArgs;

        public Condition(Class<T> clazz, T entity) {
            List<String> args = new ArrayList<>();
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder builder = new StringBuilder();
            builder.append("1=1");
            for (Field field : fields) {
                field.setAccessible(true);
                Object object = null;
                try {
                    object = field.get(entity);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (object == null) {
                    continue;
                }
                DbField dbField = field.getAnnotation(DbField.class);
                String columnName;
                if (dbField != null) {
                    columnName = dbField.value();
                } else {
                    columnName = field.getName();
                }
                builder.append(" and ").append(columnName).append("=?");
                args.add(object.toString());
            }
            whereClause = builder.toString();
            whereArgs = args.toArray(new String[args.size()]);
            Log.d(TAG, "whereClause------>" + whereClause);
            Log.d(TAG, "whereArgs------>" + whereArgs.toString());
        }

        public String getWhereClause() {
            return whereClause;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }
    }

    private ContentValues createContentValues(T entity) {
        ContentValues contentValues = new ContentValues();
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object object = null;
            try {
                object = field.get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (object == null) {
                continue;
            }
            DbField dbField = field.getAnnotation(DbField.class);
            String columnName;
            if (dbField != null) {
                columnName = dbField.value();
            } else {
                columnName = field.getName();
            }
            contentValues.put(columnName, object.toString());
        }
        return contentValues;
    }
}

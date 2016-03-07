package com.chinausky.lanbowan.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.chinausky.lanbowan.model.bean.Register;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by succlz123 on 15/9/2.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "lbw.db";
    private static final int DATABASE_VERSION = 1;
    private Map<String, Dao> mDaos = new HashMap<String, Dao>();

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Register.class);
//            TableUtils.createTable(connectionSource, RequestMessage.class);
//            TableUtils.createTable(connectionSource, RequestMessage.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            //删除旧表
            TableUtils.dropTable(connectionSource, Register.class, true);
            //建新表
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (mDaos.containsKey(className)) {
            dao = mDaos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            mDaos.put(className, dao);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        for (String key : mDaos.keySet()) {
            Dao dao = mDaos.get(key);
            dao = null;
        }
    }
}

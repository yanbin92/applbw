package com.chinausky.lanbowan.model.database;

import android.content.Context;

import com.chinausky.lanbowan.model.bean.Register;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by succlz123 on 15/9/14.
 */
public class LbwDao {
    private Context mContext;
    private DatabaseHelper mHelper;
    private Dao<Register, Integer> mRegisterDao;

    public LbwDao(Context context) {
        this.mContext = context;

        if (mContext == null) {
            return;
        }

        mHelper = DatabaseHelper.getHelper(mContext);

        try {
            mRegisterDao = mHelper.getDao(Register.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int addRegister(Register register) {
        try {
            return mRegisterDao.create(register);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteRegister(Register register) {
        try {
            mRegisterDao.delete(register);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteAllRegister() {
        try {
            mRegisterDao.delete(queryAllForRegister());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Register> queryRegister(String key, String value) {
        try {
            return mRegisterDao.queryBuilder().where().eq(key, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Register> queryAllForRegister() {
        try {
            return mRegisterDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateRegister(Register register) {
        try {
            mRegisterDao.update(register);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.radektesar.weather.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.radektesar.weather.android.database.model.Forecast;
import com.radektesar.weather.android.database.model.Store;

import java.sql.SQLException;

/**
* Created by Radek on 12. 3. 2015.
*/
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    //Base set up of database
    public static String DATABASE_NAME = "smartpills.db";
    public static final int DATABASE_VERSION = 1; // Modify onUpgrade() if you need to change DB version !!!!

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //Here can definite other models with separate part of databese
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

              TableUtils.createTable(connectionSource, Store.class);
               TableUtils.createTable(connectionSource, Forecast.class);







        } catch (SQLException e) {
            Log.e("Database", "Create table pill - failed", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

}




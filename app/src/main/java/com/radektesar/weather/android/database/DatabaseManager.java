package com.radektesar.weather.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.radektesar.weather.android.database.model.Forecast;
import com.radektesar.weather.android.database.model.Store;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;


/**
* Created by Radek on 12. 3. 2015.
*/
public class DatabaseManager implements Serializable {

    private Context mContext;
    private static DatabaseHelper mHelper;

    public DatabaseManager(Context context) {
        this.mContext = context;
        mHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.setLocale(new Locale("cz_CZ"));
    }

// Getters and setters for database
    private Dao<Store, Integer> getStoresDao() throws SQLException {
        return mHelper.getDao(Store.class);
    }

    public List<Store> getStores() throws SQLException {
        return getStoresDao().queryBuilder().query();}

    public  void addStore(Store store) throws SQLException {
        getStoresDao().create(store);
    }

    public  void updateStore(Store store) throws SQLException {
        getStoresDao().update(store);
    }

    private Dao<Forecast, Integer> getForecastsDao() throws SQLException {
        return mHelper.getDao(Forecast.class);
    }

    public List<Forecast> getForecasts() throws SQLException {
        return getForecastsDao().queryBuilder().query();}

    public  void addForecast(Forecast forecast) throws SQLException {
        getForecastsDao().create(forecast);
    }

    public  void updateForecast(Forecast forecast) throws SQLException {
        getForecastsDao().update(forecast);
    }


}

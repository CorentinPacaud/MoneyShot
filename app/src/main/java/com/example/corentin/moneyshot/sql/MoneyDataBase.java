package com.example.corentin.moneyshot.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.corentin.moneyshot.model.AccountOperation;
import com.example.corentin.moneyshot.model.BankAccount;

/**
 * Created by Corentin on 31/10/2015.
 */
public class MoneyDataBase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "MoneyShot.db";
    private static MoneyDataBase mInstance;
    private SQLiteDatabase db;

    public static MoneyDataBase getInstance(Context context) {
        if (mInstance != null) return mInstance;
        mInstance = new MoneyDataBase(context);
        mInstance.openDB();
        return mInstance;
    }

    private MoneyDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BankAccount.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2)
            db.execSQL(AccountOperation.SQL_CREATE_ENTRIES);
        if (oldVersion < 3) {
            db.execSQL(AccountOperation.DROP_TABLE);
            db.execSQL(AccountOperation.SQL_CREATE_ENTRIES);
        }
    }

    private void openDB() {
        this.db = this.getWritableDatabase();
    }

    public SQLiteDatabase getDb() {
        if (db != null) return db;
        openDB();
        return db;
    }
}

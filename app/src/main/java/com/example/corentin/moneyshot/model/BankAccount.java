package com.example.corentin.moneyshot.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.corentin.moneyshot.sql.MoneyDataBase;

import java.util.ArrayList;

/**
 * Created by Corentin on 31/10/2015.
 */
public class BankAccount {

    private static final String TYPE_TEXT = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + BankAccount.TABLE_NAME + " (" +
            BankAccount.ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            BankAccount.NAME + TYPE_TEXT +
            " )";


    private static final String TABLE_NAME = "bankAccount";
    private static final String ID = "id";
    private static final String NAME = "name";

    private static final String[] ALL_COLUMNS = new String[]{ID, NAME};

    private long id;
    private String name;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static BankAccount addAccount(Context context, String name) {
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        long id = MoneyDataBase.getInstance(context).getDb().insert(TABLE_NAME, null, values);
        return getAccount(context, id);
    }

    public static ArrayList<BankAccount> getAllAccount(Context context) {
        Cursor cursor = MoneyDataBase.getInstance(context).getDb().query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);
        ArrayList<BankAccount> list = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursorToBankAccount(cursor));
            cursor.moveToNext();
        }
        return list;
    }

    @Nullable
    public static BankAccount getAccount(Context context, long id) {
        Cursor c = MoneyDataBase.getInstance(context).getDb().query(TABLE_NAME, ALL_COLUMNS, ID + " = " + id, null, null, null, null);
        if (c.moveToFirst()) {
            return cursorToBankAccount(c);
        }
        return null;
    }

    @Nullable
    private static BankAccount cursorToBankAccount(Cursor cursor) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(cursor.getLong(0));
        bankAccount.setName(cursor.getString(1));
        return bankAccount;
    }

    public static int removeAccount(Context context, long id) {
        AccountOperation.removeAllOperationFromAccountId(context, id);
        return MoneyDataBase.getInstance(context).getDb().delete(TABLE_NAME, ID + " = " + id, null);
    }
}

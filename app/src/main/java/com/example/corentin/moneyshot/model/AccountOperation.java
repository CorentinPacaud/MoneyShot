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
public class AccountOperation {

    private static final String TAG = AccountOperation.class.getSimpleName();

    private static final String TYPE_TEXT = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String TYPE_DOUBLE = " REAL";
    private static final String TYPE_LONG = " INTEGER";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + AccountOperation.TABLE_NAME + " (" +
            AccountOperation.ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            AccountOperation.ACCOUNT_ID + TYPE_LONG + COMMA_SEP +
            AccountOperation.NAME + TYPE_TEXT + COMMA_SEP +
            AccountOperation.VALUE + TYPE_DOUBLE +
            " )";


    public static final String TABLE_NAME = "accountOperation";
    private static final String ID = "id";
    private static final String ACCOUNT_ID = "accountId";
    private static final String NAME = "name";
    private static final String VALUE = "value";

    public static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME;

    private static final String[] ALL_COLUMNS = new String[]{ID, ACCOUNT_ID, NAME, VALUE};

    private long id;
    private String name;
    private double value;
    private long accountId;

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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public static AccountOperation addOperation(Context context, long accountId, String name, double value) {
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_ID, accountId);
        values.put(NAME, name);
        values.put(VALUE, value);
        long id = MoneyDataBase.getInstance(context).getDb().insert(TABLE_NAME, null, values);
        return getAccountOperation(context, id);
    }

    public static ArrayList<AccountOperation> getAllOperation(Context context) {
        Cursor cursor = MoneyDataBase.getInstance(context).getDb().query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);
        ArrayList<AccountOperation> list = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursorToBankAccount(cursor));
            cursor.moveToNext();
        }
        return list;
    }

    public static ArrayList<AccountOperation> getAllOperationFromAccountId(Context context, long accountId) {
        Cursor cursor = MoneyDataBase.getInstance(context).getDb().query(TABLE_NAME, ALL_COLUMNS, ACCOUNT_ID + " = " + accountId, null, null, null, null);
        ArrayList<AccountOperation> list = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursorToBankAccount(cursor));
            cursor.moveToNext();
        }
        return list;
    }

    public static double getCountAllOperation(Context context) {
        Cursor cursor = MoneyDataBase.getInstance(context).getDb().query(TABLE_NAME, new String[]{"sum(" + AccountOperation.VALUE + ")"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }

    @Nullable
    public static AccountOperation getAccountOperation(Context context, long id) {
        Cursor c = MoneyDataBase.getInstance(context).getDb().query(TABLE_NAME, ALL_COLUMNS, ID + " = " + id, null, null, null, null);
        if (c.moveToFirst()) {
            return cursorToBankAccount(c);
        }
        return null;
    }

    @Nullable
    public static double getAccountOperationCountFromAccountId(Context context, long id) {
        Cursor c = MoneyDataBase.getInstance(context).getDb().query(TABLE_NAME, new String[]{"sum(" + AccountOperation.VALUE + ")"}, ACCOUNT_ID + " = " + id, null, null, null, null);
        if (c.moveToFirst()) {
            double result = c.getDouble(0);
            c.close();
            return result;
        }
        return 0;
    }


    private static AccountOperation cursorToBankAccount(Cursor cursor) {
        AccountOperation bankAccount = new AccountOperation();
        bankAccount.setId(cursor.getLong(0));
        bankAccount.setAccountId(cursor.getLong(1));
        bankAccount.setName(cursor.getString(2));
        bankAccount.setValue(cursor.getDouble(3));
        return bankAccount;
    }

    public static int removeOperation(Context context, long id) {
        return MoneyDataBase.getInstance(context).getDb().delete(TABLE_NAME, ID + " = " + id, null);
    }

    public static int removeAllOperationFromAccountId(Context context, long accountId) {
        int count = MoneyDataBase.getInstance(context).getDb().delete(TABLE_NAME, ACCOUNT_ID + " = " + accountId, null);
        Log.i(TAG, "Removed " + count + " operation from account " + accountId);
        return count;
    }
}

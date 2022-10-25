package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UserDataQuery {
    public static long insert(Context context, User us) {
        UserDBHelper userDBHelper = new UserDBHelper(context);
        SQLiteDatabase sqLiteDatabase = userDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utils.COLUMN_USER_NAME, us.name);
        values.put(Utils.COLUMN_USER_AVATAR, us.avatar);
        long rs = sqLiteDatabase.insert(Utils.TABLE_USER, null, values);
        return (rs);
    }

    public static ArrayList<User> getAll(Context context) {
        ArrayList<User> lstUser = new ArrayList<>();
        UserDBHelper userDBHelper = new UserDBHelper(context);
        SQLiteDatabase db = userDBHelper.getReadableDatabase();
        Cursor cs = db.rawQuery("Select * from " + Utils.TABLE_USER, null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int id = cs.getInt(0);
            String name = cs.getString(1);
            String avatar = cs.getString(2);
            lstUser.add(new User(id, name, avatar));
            cs.moveToNext();
        }
        cs.close();
        db.close();
        return lstUser;
    }

    public static boolean delete(Context context, int id) {
        UserDBHelper userDBHelper = new UserDBHelper(context);
        SQLiteDatabase sqLiteDatabase = userDBHelper.getWritableDatabase();
        int rs = sqLiteDatabase.delete(Utils.TABLE_USER, Utils.COLUMN_USER_ID +"=?", new String[]{String.valueOf(id)});
        return (rs > 0);
    }

    public static int update(Context context, User us)
    {
        UserDBHelper userDBHelper = new UserDBHelper(context);
        SQLiteDatabase sqLiteDatabase = userDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utils.COLUMN_USER_NAME,us.getName());
        values.put(Utils.COLUMN_USER_AVATAR,us.getAvatar());
        int rs = sqLiteDatabase.update(Utils.TABLE_USER,values,Utils.COLUMN_USER_ID+"=?",new String[]{String.valueOf(us.id)});
        return (rs);
    }
}
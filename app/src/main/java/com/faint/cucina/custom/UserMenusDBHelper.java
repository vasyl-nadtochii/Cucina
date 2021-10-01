package com.faint.cucina.custom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.faint.cucina.R;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.login_register.UserDataSP;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UserMenusDBHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "UserDishes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "user_dishes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DISHES = "dishes";
    private static final String COLUMN_NAME = "name";

    public UserMenusDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query =
                "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_PHONE + " TEXT, " + COLUMN_DISHES + " TEXT, " + COLUMN_NAME + " TEXT);";

        sqLiteDatabase.execSQL(query);
    }

    public void addMenu(String phone, String dishes, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PHONE, phone);
        cv.put(COLUMN_DISHES, dishes);
        cv.put(COLUMN_NAME, name);

        long result = db.insert(TABLE_NAME, null, cv);

        assert context != null;
        if(result == -1) {
            Toast.makeText(context, context.getString(R.string.sqlite_error), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,
                    context.getString(R.string.menu_added), Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readData() {
        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_DISHES + " FROM " + TABLE_NAME + " WHERE "
                + COLUMN_PHONE + " = " + UserDataSP.getInstance(context).getUser().getPhone();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, "_id=?", new String[]{ row_id });

        assert context != null;
        if(result == -1) {
            Toast.makeText(context, context.getString(R.string.sqlite_error), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, context.getString(R.string.successfully_deleted), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData(String row_id, String dishes, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DISHES, dishes);
        cv.put(COLUMN_NAME, name);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[] {row_id});

        assert context != null;
        if(result == -1) {
            Toast.makeText(context, context.getString(R.string.sqlite_error), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, context.getString(R.string.successfully_deleted), Toast.LENGTH_SHORT).show();
        }
    }

    public void updatePhone(String oldPhone, String newPhone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PHONE, newPhone);

        long result = db.update(TABLE_NAME, cv, "phone=?", new String[] {oldPhone});

        assert context != null;
        if(result == -1) {
            Toast.makeText(context, context.getString(R.string.sqlite_error), Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getDishes(String id) {
        String query = "SELECT " + COLUMN_DISHES + " FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = " + id;

        SQLiteDatabase dbRead = this.getReadableDatabase();

        Cursor cursor = null;
        if(dbRead != null)
            cursor = dbRead.rawQuery(query, null);

        return cursor;
    }

    public void setDishes(String id, ArrayList<OrderDish> dishes) {
        String jsonDishes = new Gson().toJson(dishes);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DISHES, jsonDishes);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[] {id});

        assert context != null;
        if(result == -1) {
            Toast.makeText(context, context.getString(R.string.sqlite_error), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, context.getString(R.string.successfully_deleted), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}

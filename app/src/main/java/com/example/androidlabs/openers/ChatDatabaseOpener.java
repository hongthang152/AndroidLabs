package com.example.androidlabs.openers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDatabaseOpener extends SQLiteOpenHelper {
    public final static String MESSAGE_TABLE_NAME = "messages";
    public final static String MESSAGE_TABLE_TEXT_COL = "text";
    public final static String MESSAGE_TABLE_IS_SEND_COL = "is_send";
    public final static String MESSAGE_TABLE_ID_COL = "_id";
    public final static Integer VERSION_NUM = 1;
    public final static String DATABASE_NAME = "message_db";

    public ChatDatabaseOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MESSAGE_TABLE_NAME + " (" + MESSAGE_TABLE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MESSAGE_TABLE_TEXT_COL + " TEXT, " +
                MESSAGE_TABLE_IS_SEND_COL + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

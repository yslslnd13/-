package com.androidprogramming.myjournal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//DBHelper 클래스 정의
public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {
        super(context, "JournalDB", null, 1);
    }

    //테이블 생성코드
    @Override
    public void onCreate(SQLiteDatabase db) {
        //테이블이 존재하지 않는 경우 새로 만듬.
        db.execSQL("CREATE TABLE IF NOT EXISTS journal_table(date integer PRIMARY KEY, title text NOT NULL, content text NOT NULL)");
    }

    //테이블 업그레이드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }
}
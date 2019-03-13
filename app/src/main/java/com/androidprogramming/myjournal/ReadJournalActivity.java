package com.androidprogramming.myjournal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

public class ReadJournalActivity extends AppCompatActivity {

    TextView tvJournal, tvTitle, tvDate;
    String fileName;
    Toolbar toolBar;
    SQLiteDatabase sqlDB;
    MyDBHelper myHelper = new MyDBHelper(this);
    int date;
    String title, content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_journal);

        //View와 id를 매칭
        tvJournal = findViewById(R.id.tvJournal);
        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);

        //툴바 관련
        toolBar = findViewById(R.id.read_toolbar);
        setSupportActionBar(toolBar);

        //CalendarActivity로부터 넘겨받은 fileName값(ex 20190311을 받음
        Intent intent = getIntent();
        fileName = intent.getExtras().getString("fileName");

        //DB에 있는 내용을 보여주는 코드
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM journal_table WHERE date = ?", new String[]{fileName});
        cursor.moveToFirst();
        date = cursor.getInt(0);
        title = cursor.getString(1);
        content = cursor.getString(2);

        System.out.println("date :" + date);

        tvJournal.setText(content);
        tvTitle.setText(title);
        tvDate.setText(intDateToStrDate(date));

        cursor.close();
        sqlDB.close();

    }

    private static String intDateToStrDate(int date) {
        String strDate = ((Integer) date).toString();
        return strDate.substring(0, 4) + "년 " + strDate.substring(4, 6) + "월 " + strDate.subSequence(6, strDate.length()) + "일";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_action, menu);
        return true;
    }
}

package com.androidprogramming.myjournal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
        tvDate.setText(CalendarActivity.intDateToStrDate(date));

        cursor.close();
        sqlDB.close();

    }

    //툴바의 액션 등록
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_action, menu);
        return true;
    }
    //툴바의 액션 선택시
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_edit:
                Intent intent = new Intent(ReadJournalActivity.this,WriteJournalActivity.class);
                intent.putExtra("fileName", fileName);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

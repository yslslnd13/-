package com.androidprogramming.myjournal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class WriteJournalActivity extends AppCompatActivity {

    TextView tvDate;
    EditText edtJournal, edtTitle;
    Toolbar toolBar;
    String fileName;
    SQLiteDatabase sqlDB;
    MyDBHelper myHelper = new MyDBHelper(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_journal);

        edtJournal = findViewById(R.id.edtJournal);
        tvDate = findViewById(R.id.tvDate);
        edtTitle = findViewById(R.id.edtTitle);

        //툴바 관련
        toolBar = findViewById(R.id.write_toolbar);
        setSupportActionBar(toolBar);

        //CalendarActivity로부터 값 받음
        Intent intent = getIntent();

        //fileName을 가져옴
        fileName = intent.getExtras().getString("fileName");
        tvDate.setText(fileName);

    }




    //메뉴의 액션바 아이템 등록(저장아이콘)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //저장 아이콘이 선택되었을 때는 파일 생성
            case R.id.action_save:

                //onCreate 발동하여 테이블이 없는 경우 테이블 생성(date, title, content순으로 입력)
                sqlDB = myHelper.getWritableDatabase();

                sqlDB.execSQL("INSERT INTO journal_table VALUES(?, ?, ?)",
                    new Object[] {fileName, edtTitle.getText().toString(), edtJournal.getText().toString()});




                sqlDB.close();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

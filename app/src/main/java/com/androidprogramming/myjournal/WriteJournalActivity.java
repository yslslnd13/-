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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WriteJournalActivity extends AppCompatActivity {

    TextView tvDate;
    EditText edtJournal, edtTitle;
    Toolbar toolBar;
    String fileName;
    SQLiteDatabase sqlDB;
    MyDBHelper myHelper = new MyDBHelper(this);
    boolean isJournalExist = false;


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

        String strFileName = CalendarActivity.intDateToStrDate(Integer.parseInt(fileName));
        tvDate.setText(strFileName);

        //이미 작성중인 파일이 있는지 확인하는 코드
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT EXISTS (SELECT * FROM journal_table WHERE date = ?)",
                new String[] {fileName});
        cursor.moveToFirst();

        //만약 이미 작성된 일기가 있는 경우 -> 일기 내용을 가져와서 현재 일기작성화면에 뿌림
        if(cursor.getInt(0)==1){
            cursor = sqlDB.rawQuery("SELECT * FROM journal_table WHERE date = ?;",new String[]{fileName});
            cursor.moveToFirst();
            edtTitle.setText(cursor.getString(1));
            edtJournal.setText(cursor.getString(2));
            isJournalExist = true;
        }

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

                //myHelper.onCreate() 발동하여 테이블이 없는 경우 테이블 생성
                sqlDB = myHelper.getWritableDatabase();

                //만약 해당 날짜의 일기가 이미 DB에 존재하는 경우 -> 데이터를 UPDATE 한다.
                if(isJournalExist) {
                    sqlDB.execSQL("UPDATE journal_table SET title = ?, content = ? WHERE date = ?",
                            new Object[]{edtTitle.getText().toString(), edtJournal.getText().toString(), fileName});
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(), "일기가 작성되었습니다", Toast.LENGTH_SHORT).show();
                    finish();

                //해당 날짜의 일기가 없는 경우 -> 데이터를 INSERT 한다
                }else {

                    sqlDB.execSQL("INSERT INTO journal_table VALUES(?, ?, ?)",
                            new Object[]{fileName, edtTitle.getText().toString(), edtJournal.getText().toString()});
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(), "일기가 작성되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

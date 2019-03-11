package com.androidprogramming.myjournal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class WriteJournalActivity extends AppCompatActivity {

    TextView tvDate, tvTitle;
    EditText edtJournal;
    Toolbar toolBar;
    String fileName;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_journal);

        edtJournal = findViewById(R.id.edtJournal);
        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);

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
        switch(item.getItemId()){

            //저장 아이콘이 선택되었을 때는 파일 생성
            case R.id.action_save:
                try {
                    FileOutputStream outFs = openFileOutput(fileName + ".txt",
                            Context.MODE_PRIVATE);
                    //일기 작성 에디터로부터 문자열을 가져온다

                    String strJournal = new String();
                    strJournal = edtJournal.getText().toString();
                    //파일에 문자열을 쓴다
                    outFs.write(strJournal.getBytes());
                    outFs.close();
                    //확인용 코드
                    Toast.makeText(getApplicationContext(), fileName+".txt가 생성됨",
                            Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                    System.out.println(e);
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

package com.androidprogramming.myjournal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadJournalActivity extends AppCompatActivity {

    TextView tvJournal;
    String fileName;
    Toolbar toolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_journal);

        //View와 id를 매칭
        tvJournal = findViewById(R.id.tvJournal);

        //툴바 관련
        toolBar = findViewById(R.id.read_toolbar);
        setSupportActionBar(toolBar);

        //CalendarActivity로부터 넘겨받은 fileName값(ex 20190311을 받음
        Intent intent = getIntent();
        fileName = intent.getExtras().getString("fileName");

        //파일 연결해서 TextView에 보여주는 코드
        try {
            FileInputStream inFs = openFileInput(fileName + ".txt");
            byte[] txt = new byte[30];
            inFs.read(txt);
            String strJournal = new String(txt);
            tvJournal.setText(strJournal);

        }catch (IOException e){
            System.out.println();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_action, menu);
        return true;
    }
}

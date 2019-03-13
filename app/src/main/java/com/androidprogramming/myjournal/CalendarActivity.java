package com.androidprogramming.myjournal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    //변수선언
    CalendarView calView;
    Button btnView, btnWrite, btnExit;
    TextView tvJournal;
    Toolbar toolBar;
    int selectedYear, selectedMonth, selectedDay;
    String fileName;
    SQLiteDatabase sqlDB;
    MyDBHelper myHelper = new MyDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //뷰를 id와 매칭
        calView = findViewById(R.id.calendarView);
        btnView = findViewById(R.id.btnView);
        btnWrite = findViewById(R.id.btnWrite);
        btnExit = findViewById(R.id.btnExit);
        tvJournal = findViewById(R.id.tvJournal);
        //툴바 관련
        toolBar = findViewById(R.id.calendar_toolbar);
        setSupportActionBar(toolBar);

        //처음에 선택되어있는 날짜(오늘)의 연,월,일을 변수에 대입하는 코드
        Calendar nowDay = Calendar.getInstance();
        nowDay.setTimeInMillis(calView.getDate());
        selectedYear = nowDay.get(Calendar.YEAR);
        selectedMonth = nowDay.get(Calendar.MONTH);
        selectedDay = nowDay.get(Calendar.DATE);

        //일기 작성 버튼 클릭시 WriteJournalActivity로 이동
        btnWrite.setOnClickListener(new View. OnClickListener(){
            @Override
            public void onClick(View v) {
                //파일명 산출
                fileName = getFileName(selectedYear, selectedMonth, selectedDay);

                Intent intent = new Intent(CalendarActivity.this,WriteJournalActivity.class);

                //파일명을 일기작성 액티비티로 넘겨줌
                intent.putExtra("fileName", fileName);
                startActivity(intent);

            }
        });


        //일기보기 버튼 클릭시 ReadJournalActivity로 이동
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //파일명 산출
                fileName = getFileName(selectedYear, selectedMonth, selectedDay);
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT EXISTS (SELECT * FROM journal_table WHERE date=?)",
                        new String[] {fileName});
                cursor.moveToFirst();
                //해당 날짜의 데이터(일기)가 없는 경우 AlertDialog를 띄워서 새로 작성할 것인지 물어봄
                if(cursor.getInt(0)==0){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CalendarActivity.this);
                    alertDialog.setMessage("작성된 일기가 없습니다.\n새로 작성하시겠습니까?");
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //일기 작성 액티비티로 fileName을 전달
                            Intent intent = new Intent(CalendarActivity.this,WriteJournalActivity.class);

                            //파일명을 일기작성 액티비티로 넘겨줌
                            intent.putExtra("fileName", fileName);
                            startActivity(intent);

                        }
                    });
                    alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }else {          //작성된 내용이 있는 경우, 일기보기 액티비티로 전환
                    Intent intent = new Intent(CalendarActivity.this,ReadJournalActivity.class);

                    //파일명을 일기보기 액티비티로 넘겨줌
                    intent.putExtra("fileName", fileName);
                    startActivity(intent);
                }

                cursor.close();
                sqlDB.close();

            }
        });

        //끝내기 버튼 클릭시 어플 종료
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        //날짜가 변경되었을때 연,월,일 변수값을 변경시켜주는 코드
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_action, menu);
        return true;
    }

    //파일명을 만들어주는 메서드
    private String getFileName(int year, int month, int day){

        String y, m, d;

        y = Integer.toString(year);
        m = (month/10==0) ? "0" + Integer.toString(month) : Integer.toString(month);   //ex) 3월의 경우 03월로 변경
        d = (day/10==0) ? "0" + Integer.toString(day) : Integer.toString(day);  //ex) 7일의 경우 07일로 변경

        return y+m+d;
    }

    static String intDateToStrDate(int date) {
        String strDate = ((Integer) date).toString();
        return strDate.substring(0, 4) + "년 " + strDate.substring(4, 6) + "월 " + strDate.subSequence(6, strDate.length()) + "일";
    }
}

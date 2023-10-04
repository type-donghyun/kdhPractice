package com.swt_calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public String readDay = null;
    public String str = null;
    public CalendarView calendar;
    public Button add, change, remove;
    public EditText contextEditText;
    Calendar today = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contextEditText = findViewById(R.id.contextEditText);
        calendar = findViewById(R.id.calendar);
        add = findViewById(R.id.add);
        change = findViewById(R.id.change);
        remove = findViewById(R.id.remove);


        checkDay(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));

        if (contextEditText.getText().toString().isEmpty()) {
            add.setEnabled(false);
            change.setEnabled(false);
            remove.setEnabled(false);
        } else {
            add.setEnabled(false);
            change.setEnabled(false);
            remove.setEnabled(true);
        }

//        캘린더 선택
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                contextEditText.setText(null);
                checkDay(year, month, dayOfMonth);

                if (contextEditText.getText().toString().isEmpty()) {
                    add.setEnabled(false);
                    change.setEnabled(false);
                    remove.setEnabled(false);
                } else {
                    add.setEnabled(false);
                    change.setEnabled(false);
                    remove.setEnabled(true);
                }
            }
        });

        contextEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 시 조치
                if (str.isEmpty() && contextEditText.getText().toString().isEmpty()) {
                    add.setEnabled(false);
                    change.setEnabled(false);
                } else if (str.equals(contextEditText.getText().toString())) {
                    add.setEnabled(false);
                    change.setEnabled(false);
                } else if (str.isEmpty() && !contextEditText.getText().toString().equals(null)) {
                    add.setEnabled(true);
                    change.setEnabled(false);
                } else {
                    add.setEnabled(false);
                    change.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때 조치
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에 조치
            }
        });

//        추가 버튼
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDiary(readDay);

                contextEditText.clearFocus();
                str = contextEditText.getText().toString();
                if (contextEditText.getText().toString().equals(str.toString())) {
                    add.setEnabled(true);
                    change.setEnabled(false);
                    remove.setEnabled(false);
                } else {
                    add.setEnabled(false);
                    change.setEnabled(false);
                    remove.setEnabled(true);
                }
            }
        });
    }

    public void checkDay(int cYear, int cMonth, int cDay) {
        readDay = cYear + "-" + (cMonth + 1) +  "-" + cDay + ".txt";
        FileInputStream fis;

        try {
            fis = openFileInput(readDay);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str = new String(fileData);
            contextEditText.setText(str);

            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveDiary(readDay);

                    contextEditText.clearFocus();
                    add.setEnabled(false);
                    change.setEnabled(false);
                    if (contextEditText.getText().toString().isEmpty()) {
                        remove.setEnabled(false);
                    } else {
                        remove.setEnabled(true);
                    }
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contextEditText.setText(null);
                    removeDiary(readDay);

                    contextEditText.clearFocus();
                    add.setEnabled(false);
                    change.setEnabled(false);
                    remove.setEnabled(false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay) {
        FileOutputStream fos;
        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = "";
            fos.write((content).getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkDay(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }

    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay) {
        FileOutputStream fos;
        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkDay(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }
}
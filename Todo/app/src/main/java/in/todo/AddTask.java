package in.todo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Database;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTask extends AppCompatActivity {
    EditText etTask,etDate , etTime;
    private int mYear, mMonth, mDay ,mHour ,mMinute;
    Button btnSubmit;
    DataBase database;
    ImageButton btnBack;
    String timeTonotify;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        etTask = findViewById(R.id.etTask);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
        etDate.setShowSoftInputOnFocus(false);
        etTime.setShowSoftInputOnFocus(false);
        etDate.setCursorVisible(false);
        etTime.setCursorVisible(false);
        etTask.setShowSoftInputOnFocus(true);
        etTask.setCursorVisible(true);

        setDB();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AddTask.this,MainActivity.class);
                startActivity(in);
                finish();
            }
        });

        etTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*    etDate.getBackground().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
                etTime.getBackground().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
                etTask.getBackground().setColorFilter(getResources().getColor(R.color.teal_200), PorterDuff.Mode.SRC_ATOP);*/

              showKeyboard();
                etTask.setShowSoftInputOnFocus(true);
                etTask.setCursorVisible(true);
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                etTask.setShowSoftInputOnFocus(false);
               etTask.setCursorVisible(false);
                etDate.setShowSoftInputOnFocus(true);
             /*   etTask.getBackground().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
                etTime.getBackground().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
                etDate.getBackground().setColorFilter(getResources().getColor(R.color.teal_200), PorterDuff.Mode.SRC_ATOP);*/
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTask.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String month;
                                String date;
                                if(monthOfYear<10)
                                {
                                    month = "0"+Integer.toString(monthOfYear+1);
                                }
                                else
                                {
                                    month = Integer.toString(monthOfYear+1);
                                }
                                if(dayOfMonth<10)
                                {
                                   date = "0"+Integer.toString(dayOfMonth);
                                }
                                else
                                {
                                   date = Integer.toString(dayOfMonth);
                                }


                                etDate.setText(date + "-" + month + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                etTask.setShowSoftInputOnFocus(false);
                etTask.setCursorVisible(false);
                etDate.setShowSoftInputOnFocus(false);
                etTime.setShowSoftInputOnFocus(true);
             /*   etDate.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
                etTask.getBackground().mutate().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
                etTime.getBackground().mutate().setColorFilter(getResources().getColor(R.color.teal_200), PorterDuff.Mode.SRC_ATOP);*/
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        timeTonotify = i + ":" + i1;
                       etTime.setText(FormatTime(i, i1));
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task1 = etTask.getText().toString();
                String date = etDate.getText().toString();
                String time = etTime.getText().toString();
                if(task1.length() == 0 || date.length() == 0 || time.length() == 0)
                {
                    Toast.makeText(AddTask.this, "Enter all details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                Task task = new Task(task1,date,time);
                database.dao().taskInsertion(task);
                    setAlarm(task1, date, time);
                Intent i = new Intent(AddTask.this,MainActivity.class);
                startActivity(i);
                finish();}
            }
        });
    }



    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        finish();

    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v != null)
            imm.showSoftInput(v, 0);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    public void setDB() {
        database = Room.databaseBuilder(this, DataBase.class, "Task DB")
               .allowMainThreadQueries().build();
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back press disabled", Toast.LENGTH_SHORT).show();
    }
    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }


}


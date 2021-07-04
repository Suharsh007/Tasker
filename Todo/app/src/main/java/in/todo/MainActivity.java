package in.todo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {
    public final static int PERMISSION_REQUEST_CODE =1;
    private WindowManager windowManager;
    private View chatheadView;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ArrayList<Task> task_list;
    RecyclerAdapter adapter;
    DataBase db;
    ArrayList<Task> today_task;
    private static final String TAG = "MainActivity";
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    Context context;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(i, PERMISSION_REQUEST_CODE);
        } else {
            showChatHead();
        }

        setDB();
        init();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddTask.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void showChatHead() {
        startService(new Intent(MainActivity.this,ChatheadService.class));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Destroyed Chat header");
        if (chatheadView != null)
        {
            showChatHead();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       if(chatheadView == null)
       {
        showChatHead();}
    }
    /*   private void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    private void createNotificationChannel() {
        if (mNotifyManager == null){
           mNotifyManager = (NotificationManager)  getSystemService(Context.NOTIFICATION_SERVICE);}
       //mNotifyManager = (NotificationManager)
          //     getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
  private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_android);
        return notifyBuilder;
    }*/

    private void init() {
        task_list = new ArrayList<Task>();
        task_list.addAll(db.dao().getTask());
        int n = task_list.size();
      //  Log.d(TAG, "onCreate: "+n);
        recyclerView = findViewById(R.id.rvView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new RecyclerAdapter(MainActivity.this,task_list);
        recyclerView.setAdapter(adapter);
        today_task = new ArrayList<Task>();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = df.format(c);
        today_task.addAll(db.dao().getTodayTask(date));
        int s = today_task.size();
      //  Log.d(TAG, "init: "+s);
    }

    public void setDB() {
        db = Room.databaseBuilder(MainActivity.this, DataBase.class, "Task DB")
                .allowMainThreadQueries().build();
    }



}
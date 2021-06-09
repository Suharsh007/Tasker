package in.todo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatheadService extends Service {
    private static final String TAG ="Chat" ;
    private WindowManager windowManager;
    private View chatheadView;
    RecyclerView recyclerView;
    DayAdapter adapter;
    DataBase db;
    ArrayList<Task> today_task;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        chatheadView = LayoutInflater.from(this).inflate(R.layout.chathead_bubble,null);
        final View  chatheadImage = chatheadView.findViewById(R.id.chatHead);
        final View expandedView = chatheadView.findViewById(R.id.clickBubble);
        ImageButton closeBtn = chatheadView.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              expandedView.setVisibility(View.GONE);
              chatheadImage.setVisibility(View.VISIBLE);

            }
        });

        setDB();
        init();

        int layout_params;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)

        {
            layout_params = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        }

        else {

            layout_params = WindowManager.LayoutParams.TYPE_PHONE;

        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layout_params,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );



        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;



        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
   chatheadImage.setVisibility(View.VISIBLE);
   expandedView.setVisibility(View.GONE);

        windowManager.addView(chatheadView,params);


        chatheadImage.setOnTouchListener(new View.OnTouchListener() {
                 private int initialX;
                 private int initialY;
                 private float touchX;
                 private float touchY;
                 private int lastAction;
                 @SuppressLint("ClickableViewAccessibility")

                 @Override
                 public boolean onTouch(View v, MotionEvent event) {
                     if(event.getAction() == MotionEvent.ACTION_DOWN)
                     {
                         initialX = params.x;
                         initialY = params.y;

                         touchX = event.getRawX();
                         touchY = event.getRawY();

                         lastAction = event.getAction();

                         return  true;
                     }
                     if(event.getAction() == MotionEvent.ACTION_UP)
                     {
                         if (params.y > screenHeight * 0.8) {
                             chatheadView.setVisibility(View.GONE);
                             Toast.makeText(getApplication(), "Removed!",
                                     Toast.LENGTH_SHORT).show();
                             vibe.vibrate(200);
                             stopSelf();
                         }
                        if(lastAction == MotionEvent.ACTION_DOWN)
                         {

                             chatheadImage.setVisibility(View.GONE);
                             params.gravity = Gravity.TOP | Gravity.LEFT;
                             params.x = 0;
                             params.y = 100;
                             windowManager.updateViewLayout(chatheadView,params);
                             expandedView.setVisibility(View.VISIBLE);


                         }
                         lastAction = event.getAction();
                         return true;
                     }

                     if(event.getAction() == MotionEvent.ACTION_MOVE)
                     {
                         params.x = initialX + (int)(event.getRawX() - touchX);
                         params.y = initialY + (int)(event.getRawY() - touchY);

                         windowManager.updateViewLayout(chatheadView,params);
                         lastAction = event.getAction();
                         return true;
                     }
                     return false;
                 }
             }
        );

    }

    private void setDB() {
        db = Room.databaseBuilder(ChatheadService.this, DataBase.class, "Task DB")
                .allowMainThreadQueries().build();
    }
    private void init() {
        today_task = new ArrayList<Task>();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Log.d(TAG, "init: "+df.toString());
        String date = df.format(c);
        today_task.addAll(db.dao().getTodayTask(date));
        TextView tvNotask = chatheadView.findViewById(R.id.tvNotask);
        recyclerView = chatheadView.findViewById(R.id.rView);
        if(today_task.size() == 0)
        {

            tvNotask.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

        }
        else
        {
        tvNotask.setVisibility(View.INVISIBLE);
       recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatheadService.this));
        adapter = new DayAdapter(ChatheadService.this,today_task);
            adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatheadView != null)
        {
            windowManager.removeView(chatheadView);
        }
    }

}
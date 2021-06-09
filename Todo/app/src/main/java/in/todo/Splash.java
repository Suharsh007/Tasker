package in.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Splash extends AppCompatActivity {
ImageButton btnSwipe;
RelativeLayout rvView;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        btnSwipe = findViewById(R.id.btnSwipe);
        rvView = findViewById(R.id.rvView);
        rvView.setOnTouchListener(new OnSwipeTouchListener(Splash.this) {
            public void onSwipeTop() {
                Toast.makeText(Splash.this, "Swipe Left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(Splash.this, "Swipe Left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Intent i = new Intent(Splash.this,MainActivity.class);
                startActivity(i);
                vibe.vibrate(200);
                finish();
            }
            public void onSwipeBottom() {
                Toast.makeText(Splash.this, "Swipe Left", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
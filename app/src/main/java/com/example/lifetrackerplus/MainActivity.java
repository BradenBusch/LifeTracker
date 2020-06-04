package com.example.lifetrackerplus;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/*
 * This class handles the splash screen and animation of the splash screen
 */
public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000; // Time to hang on the splash screen (in milliseconds)
    Animation topLogoAnim;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topLogoAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        logo = findViewById(R.id.spash_textview_logo);
        logo.setAnimation(topLogoAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);
                finish(); // removes this current activity from the activity list
            }
        }, SPLASH_SCREEN);
    }
}

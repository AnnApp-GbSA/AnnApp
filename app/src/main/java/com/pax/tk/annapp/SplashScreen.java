package com.pax.tk.annapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 600;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);

        switch (prefs.getInt(getString(R.string.bundleKey_colorThemePosition), 0)){
            case 0:
                //TODO nur zum Anzeigen
                //setTheme(R.style.Tim);
                setTheme(R.style.AppThemeDefault);
                break;
            case 1:
                setTheme(R.style.AppThemeOrange);
                break;
            case 2:
                setTheme(R.style.AppThemeBlue);
                break;
            case 3:
                setTheme(R.style.AppThemeColorful);
        }
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            // Start your app main activity
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);

            // close this activity
            finish();
        }, SPLASH_TIME_OUT);

    }

}
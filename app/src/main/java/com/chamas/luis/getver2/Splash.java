package com.chamas.luis.getver2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseUser;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "T4mKLSOGaa3dLavmKuvMlLvuXClmAdHxj295mOzc", "6KjuRBv415crv76Go4vqQyUD9Nt7pbCBS5CrLvQB");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ParseUser currentUser = ParseUser.getCurrentUser();
                if (currentUser != null) {
                    Intent eventIntent = new Intent(Splash.this, EventList.class);
                    Splash.this.startActivity(eventIntent);
                    Splash.this.finish();
                } else {
                    Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }

            }
        }, 2000);
    }

}

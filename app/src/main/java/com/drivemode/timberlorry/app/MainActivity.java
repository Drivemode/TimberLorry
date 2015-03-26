package com.drivemode.timberlorry.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.drivemode.timberlorry.TimberLorry;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {
        TimberLorry.getInstance().load(new ButtonClick(System.currentTimeMillis()));
    }
}

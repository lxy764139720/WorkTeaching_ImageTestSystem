package com.benson.face;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void next(View view) {
        startActivity(new Intent(MainActivity.this, StudentLoginActivity.class));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
    }
}

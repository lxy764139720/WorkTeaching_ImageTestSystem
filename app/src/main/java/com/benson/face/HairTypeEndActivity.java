package com.benson.face;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HairTypeEndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_complexion);
    }

    public void exitTest(View view) {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
        finish();
    }

    public void bodyTest(View view) {
        startActivity(new Intent(this, BodyCameraActivity.class));
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
        finish();
    }
}

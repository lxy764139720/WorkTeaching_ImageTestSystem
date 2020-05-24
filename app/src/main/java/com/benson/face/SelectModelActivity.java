package com.benson.face;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SelectModelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_model);
    }

    //选择面部识别模块
    public void selectFace(View view) {
        try {
            startActivity(new Intent(this, FaceCameraActivity.class)); //页面跳转到面部拍照
            overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //选择全身识别模块
    public void selectBody(View view) {
        startActivity(new Intent(this, BodyCameraActivity.class)); //页面跳转到全身识别
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right);
    }
}

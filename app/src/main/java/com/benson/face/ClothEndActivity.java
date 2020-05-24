package com.benson.face;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class ClothEndActivity extends AppCompatActivity {
    private int index;
    private int[] arr;
    private String style;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloth_end);
        index = getIntent().getIntExtra("index", 0);
        arr = getIntent().getIntArrayExtra("arr");
        style = getIntent().getStringExtra("style");
        MyApplication myApplication = (MyApplication) getApplication();
        gender = myApplication.user.gender;
        gue = new GestureDetector(this, new MyGestureListener());
    }

    public void exitTest(View view) {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
        finish();
    }

    public void faceTest(View view) {
        startActivity(new Intent(this, FaceCameraActivity.class));
        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
        finish();
    }

    private GestureDetector gue;

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //onFling方法的第一个参数是 手指按下的位置， 第二个参数是 手指松开的位置，第三个参数是手指的速度
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float startX = e1.getX();//通过e1.getX（）获得手指按下位置的横坐标
            float endX = e2.getX();//通过e2.getX（）获得手指松开位置的横坐标
            float startY = e1.getY();//通过e1.getY（）获得手指按下位置的纵坐标
            float endY = e2.getY();//通过e2.getY（）获得手指松开的纵坐标
            if ((endX - startX) > 50 && Math.abs(startY - endY) < 200) {
                if ("w".equals(gender)) {
                    Intent intent = new Intent(ClothEndActivity.this, WomanClothDetailsActivity.class);
                    intent.putExtra("index", index);
                    intent.putExtra("arr", arr);
                    intent.putExtra("style", style);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                } else {
                    Intent intent = new Intent(ClothEndActivity.this, ManClothDetailsActivity.class);
                    intent.putExtra("index", index);
                    intent.putExtra("arr", arr);
                    intent.putExtra("style", style);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                }
            }
//返回值是重点：如果返回值是true则动作可以执行，如果是flase动作将无法执行
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gue.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

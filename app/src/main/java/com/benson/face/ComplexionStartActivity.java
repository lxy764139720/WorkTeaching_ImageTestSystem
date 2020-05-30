package com.benson.face;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class ComplexionStartActivity extends AppCompatActivity {

    private String scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complexion_start);
        scene = getIntent().getStringExtra("scene");
        gue = new GestureDetector(this, new MyGestureListener());
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
            if ((startX - endX) > 50 && Math.abs(startY - endY) < 200) {
                startActivity(new Intent(ComplexionStartActivity.this, ComplexionActivity.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
            }
            //返回值是重点：如果返回值是true则动作可以执行，如果是flase动作将无法执行
            return true;
        }
    }

    public void lookResult() {
        Intent intent = new Intent(ComplexionStartActivity.this, ComplexionActivity.class);
        intent.putExtra("scene", scene);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        finish();
    }

    public void black(View view) {
        ((MyApplication) getApplication()).user.complexion = User.Complexion.BLACK;
        lookResult();
    }

    public void white(View view) {
        ((MyApplication) getApplication()).user.complexion = User.Complexion.WHITE;
        lookResult();
    }

    public void yellow(View view) {
        ((MyApplication) getApplication()).user.complexion = User.Complexion.YELLOW;
        lookResult();
    }

    public void wheat(View view) {
        ((MyApplication) getApplication()).user.complexion = User.Complexion.WHEAT;
        lookResult();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gue.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

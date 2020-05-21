package com.benson.face;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

public class ComplexionActivity extends AppCompatActivity {

    private GestureDetector gue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_complexion, null);
        setContentView(view);
        gue = new GestureDetector(this, new MyGestureListener());
        int bg = -1;
        if ("m".equals(((MyApplication) getApplication()).user.gender)) {
            switch (((MyApplication) getApplication()).user.complexion) {
                case BLACK:
                    bg = R.drawable.m_black;
                    break;
                case WHEAT:
                    bg = R.drawable.m_wheat;
                    break;
                case YELLOW:
                    bg = R.drawable.m_yellow;
                    break;
                case WHITE:
                    bg = R.drawable.m_white;
                    break;
                default:
            }
        } else {
            switch (((MyApplication) getApplication()).user.complexion) {
                case BLACK:
                    bg = R.drawable.w_black;
                    break;
                case WHEAT:
                    bg = R.drawable.w_wheat;
                    break;
                case YELLOW:
                    bg = R.drawable.w_yellow;
                    break;
                case WHITE:
                    bg = R.drawable.w_white;
                    break;
                default:
            }
        }
        view.setBackgroundResource(bg);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        //onFling方法的第一个参数是 手指按下的位置， 第二个参数是 手指松开的位置，第三个参数是手指的速度
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float startX = e1.getX();//通过e1.getX（）获得手指按下位置的横坐标
            float endX = e2.getX();//通过e2.getX（）获得手指松开位置的横坐标
            float startY = e1.getY();//通过e1.getY（）获得手指按下位置的纵坐标
            float endY = e2.getY();//通过e2.getY（）获得手指松开的纵坐标
            if ((startX - endX) > 50 && Math.abs(startY - endY) < 200) {
                startActivity(new Intent(ComplexionActivity.this, HairTypeEndActivity.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                finish();
            }
            if ((endX - startX) > 50 && Math.abs(startY - endY) < 200) {
                startActivity(new Intent(ComplexionActivity.this, FaceResultActivity.class));
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
                finish();
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

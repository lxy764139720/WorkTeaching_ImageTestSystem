package com.benson.face;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class ComplexionActivity extends AppCompatActivity {

    private String scene;
    private int index;
    private int[] arr;
    private String style;
    private GestureDetector gue;

    private LinearLayout colorLayout;
    private TextView colorName;
    private ImageView colorImg_1;
    private TextView colorDes;
    private ImageView colorImg_2;
    private ImageView colorImg_3;
    private ImageView colorImg_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complexion);
        scene = getIntent().getStringExtra("scene");
        Toast.makeText(ComplexionActivity.this, "左滑可查看详情以及其他配件的推荐", Toast.LENGTH_SHORT).show();
        gue = new GestureDetector(this, new MyGestureListener());
        init();
        setImage();
        index = getIntent().getIntExtra("index", 0);
        arr = getIntent().getIntArrayExtra("arr");
        if (arr == null) {
            arr = new int[3];
            Random random = new Random(System.currentTimeMillis());
            for (int i = 0; i < arr.length; i++) {
                int num;
                do {
                    num = random.nextInt(7) + 1;
                } while (num == arr[0] || num == arr[1] || num == arr[2]);
                arr[i] = num;
            }
        }
        Random random = new Random(System.currentTimeMillis());
        style = random.nextInt(6) + "";
    }

    private void init() {
        colorLayout = findViewById(R.id.colorLayout);
        colorName = findViewById(R.id.colorName);
        colorImg_1 = findViewById(R.id.colorImg_1);
        colorDes = findViewById(R.id.colorDes);
        colorImg_2 = findViewById(R.id.colorImg_2);
        colorImg_3 = findViewById(R.id.colorImg_3);
        colorImg_4 = findViewById(R.id.colorImg_4);
    }

    private void setImage() {
        User user = ((MyApplication) getApplication()).user;
        if ("m".equals(user.gender)) {
            switch (user.complexion) {
                case BLACK:
                    colorName.setText("黝黑色皮肤");
                    colorImg_1.setImageResource(R.drawable.m_black);
                    colorDes.setText(R.string.m_black);
                    colorImg_2.setBackgroundColor(getResources().getColor(R.color.white));
                    colorImg_3.setBackgroundColor(getResources().getColor(R.color.tea));
                    colorImg_4.setBackgroundColor(getResources().getColor(R.color.purple));
                    break;
                case WHEAT:
                    colorName.setText("小麦色皮肤");
                    colorImg_1.setImageResource(R.drawable.m_wheat);
                    colorDes.setText(R.string.m_wheat);
                    colorImg_2.setBackgroundColor(getResources().getColor(R.color.blue));
                    colorImg_3.setBackgroundColor(getResources().getColor(R.color.green));
                    colorImg_4.setBackgroundColor(getResources().getColor(R.color.gray));
                    break;
                case YELLOW:
                    colorName.setText("黄色皮肤");
                    colorImg_1.setImageResource(R.drawable.m_yellow);
                    colorDes.setText(R.string.m_yellow);
                    colorImg_2.setBackgroundColor(getResources().getColor(R.color.white));
                    colorImg_3.setBackgroundColor(getResources().getColor(R.color.tea));
                    colorImg_4.setBackgroundColor(getResources().getColor(R.color.purple));
                    break;
                case WHITE:
                    colorName.setText("白色皮肤");
                    colorImg_1.setImageResource(R.drawable.m_white);
                    colorDes.setText(R.string.m_white);
                    colorImg_2.setBackgroundColor(getResources().getColor(R.color.blue));
                    colorImg_3.setBackgroundColor(getResources().getColor(R.color.green));
                    colorImg_4.setBackgroundColor(getResources().getColor(R.color.gray));
                    break;
                default:
            }
        } else {
            switch (user.complexion) {
                case BLACK:
                    colorName.setText("黝黑色皮肤");
                    colorImg_1.setImageResource(R.drawable.w_black);
                    colorDes.setText(R.string.w_black);
                    colorLayout.removeView(colorImg_1);
                    colorLayout.removeView(colorImg_2);
                    colorLayout.removeView(colorImg_3);
                    break;
                case WHEAT:
                    colorName.setText("小麦色皮肤");
                    colorImg_1.setImageResource(R.drawable.w_wheat_1);
                    colorDes.setText(R.string.w_wheat);
                    colorLayout.removeView(colorImg_1);
                    colorLayout.removeView(colorImg_2);
                    colorLayout.removeView(colorImg_3);
                    break;
                case YELLOW:
                    colorName.setText("黄色皮肤");
                    colorImg_1.setImageResource(R.drawable.w_yellow_1);
                    colorDes.setText(R.string.w_wheat);
                    colorLayout.removeView(colorImg_1);
                    colorLayout.removeView(colorImg_2);
                    colorLayout.removeView(colorImg_3);
                    break;
                case WHITE:
                    colorName.setText("白色皮肤");
                    colorImg_1.setImageResource(R.drawable.w_white);
                    colorDes.setText(R.string.w_white);
                    colorLayout.removeView(colorImg_1);
                    colorLayout.removeView(colorImg_2);
                    colorLayout.removeView(colorImg_3);
                    break;
                default:
            }
        }
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
                turnActivity();
            }
            //返回值是重点：如果返回值是true则动作可以执行，如果是flase动作将无法执行
            return true;
        }
    }

    public void turnActivity() {
        MyApplication myApplication = (MyApplication) getApplication();
        Intent intent = new Intent();
        if (myApplication.user.gender.equals("w")) {
            switch (scene) {
                case "求职正装":
                    intent.setClass(ComplexionActivity.this, WomanWorkClothDetailsActivity.class);
                    break;
                case "约会穿搭":
                    intent.setClass(ComplexionActivity.this, WomanDateClothDetailsActivity.class);
                    break;
                case "日常穿搭":
                    intent.setClass(ComplexionActivity.this, WomanDailyClothDetailsActivity.class);
                    break;
                default:
                    break;
            }
        } else {
            switch (scene) {
                case "求职正装":
                    intent.setClass(ComplexionActivity.this, ManWorkClothDetailsActivity.class);
                    break;
                case "约会穿搭":
                    intent.setClass(ComplexionActivity.this, ManDateClothDetailsActivity.class);
                    break;
                case "日常穿搭":
                    intent.setClass(ComplexionActivity.this, ManDailyClothDetailsActivity.class);
                    break;
                default:
                    break;
            }
        }
        intent.putExtra("index", index);
        intent.putExtra("arr", arr);
        intent.putExtra("style", style);
        intent.putExtra("scene", scene);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gue.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

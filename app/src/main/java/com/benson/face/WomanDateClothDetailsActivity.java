package com.benson.face;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class WomanDateClothDetailsActivity extends AppCompatActivity {

    private String scene;
    private int index;
    private int[] arr;
    private User.BodyType bodyType;
    private GestureDetector gue;
    private User user;

    private TextView descriptionText;
    private ImageView clothsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clothing);
        Toast.makeText(WomanDateClothDetailsActivity.this, "左滑可返回主界面", Toast.LENGTH_SHORT).show();
        initView();
        user = ((MyApplication) getApplication()).user;
        bodyType = user.bodyType;
        index = getIntent().getIntExtra("index", 0);
        arr = getIntent().getIntArrayExtra("arr");
        scene = getIntent().getStringExtra("scene");
        clothsImg.setImageBitmap(getClothImg(index));
        descriptionText.setText(getDescription());//获取strings.xml里的服装描述文本信息
        gue = new GestureDetector(WomanDateClothDetailsActivity.this, new WomanDateClothDetailsActivity.MyGestureListener());
    }

    private void initView() {
        clothsImg = findViewById(R.id.cloth_img);
        descriptionText = findViewById(R.id.description);
    }

    private Bitmap getClothImg(int index) {
        StringBuilder sb = new StringBuilder(user.gender);
        sb.append("/date/").append(user.bodyType.getBodyType()).append("/").append(arr[index]).append(".jpg");
        try {
            InputStream in = getAssets().open(sb.toString());
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getDescription() {
        String description = null;
        switch (bodyType) {
            case A:
                description = (String) this.getResources().getText(R.string.w_date_A);
                break;
            case H:
                description = (String) this.getResources().getText(R.string.w_date_H);
                break;
            case O:
                description = (String) this.getResources().getText(R.string.w_date_O);
                break;
            case X:
                description = (String) this.getResources().getText(R.string.w_date_X);
                break;
            case Y:
                description = (String) this.getResources().getText(R.string.w_date_Y);
            default:
                break;
        }
        return description;
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
                Intent intent = new Intent(WomanDateClothDetailsActivity.this, SelectModelActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
            }//左滑
            if ((endX - startX) > 50 && Math.abs(startY - endY) < 200) {
                Intent intent = new Intent(WomanDateClothDetailsActivity.this, ComplexionActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("arr", arr);
                intent.putExtra("scene", scene);
                intent.putExtra("style", bodyType);//返回上一界面保存数据
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
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

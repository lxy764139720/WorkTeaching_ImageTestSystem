package com.benson.face;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class HairAndGlassesActivity extends AppCompatActivity {

    private GestureDetector gue;

    private TextView hairDesText;
    private TextView glassesDesText;

    private ImageView hairImg;
    private ImageView glassesImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hairandglasses);
        initView();
        MyApplication myApplication = (MyApplication) getApplication();
        String gender = myApplication.user.gender;
        User.FaceType faceType = myApplication.user.facetype;
        String hair = "hair";
        hairImg.setImageBitmap(getImg(hair, gender, faceType));
        String glasses = "glasses";
        glassesImg.setImageBitmap(getImg(glasses, gender, faceType));
        hairDesText.setText(getDescription(hair, gender, faceType));
        glassesDesText.setText(getDescription(glasses, gender, faceType));
        Toast.makeText(HairAndGlassesActivity.this, "左滑可返回主界面", Toast.LENGTH_SHORT).show();
        gue = new GestureDetector(HairAndGlassesActivity.this, new MyGestureListener());
    }

    private void initView() {
        hairImg = findViewById(R.id.hair);
        glassesImg = findViewById(R.id.glasses);
        hairDesText = findViewById(R.id.hairDes);
        glassesDesText = findViewById(R.id.glassesDes);
    }

    private Bitmap getImg(String item, String gender, User.FaceType faceType) {
        StringBuilder sb = new StringBuilder();
        if (item.equals("hair")) {
            int number = (int) (Math.random() * 4) + 1;
            if (gender.equals("m")) {
                sb.append("hair").append("/").append("man").append("/").append(faceType.toString().toLowerCase()).append("/").append(number).append(".png");
            } else {
                sb.append("hair").append("/").append("women").append("/").append(faceType.toString().toLowerCase()).append("/").append(number).append(".png");
            }
        } else {
            sb.append("glasses").append("/").append(faceType.toString().toLowerCase()).append(".png");
        }
        try {
            InputStream in = getAssets().open(sb.toString());
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getDescription(String item, String gender, User.FaceType faceType) {
        String description = null;
        if (item.equals("hair") && gender.equals("m")) {
            switch (faceType) {
                case FANG:
                    description = (String) this.getResources().getText(R.string.des_hair_man_fang);
                    break;
                case CHANG:
                    description = (String) this.getResources().getText(R.string.des_hair_man_chang);
                    break;
                case LING:
                    description = (String) this.getResources().getText(R.string.des_hair_man_ling);
                    break;
                case EDAN:
                    description = (String) this.getResources().getText(R.string.des_hair_man_edan);
                    break;
                case YUAN:
                    description = (String) this.getResources().getText(R.string.des_hair_man_yuan);
                    break;
            }
        } else if (item.equals("hair") && gender.equals("w")) {
            switch (faceType) {
                case FANG:
                    description = (String) this.getResources().getText(R.string.des_hair_woman_fang);
                    break;
                case CHANG:
                    description = (String) this.getResources().getText(R.string.des_hair_woman_chang);
                    break;
                case LING:
                    description = (String) this.getResources().getText(R.string.des_hair_woman_ling);
                    break;
                case EDAN:
                    description = (String) this.getResources().getText(R.string.des_hair_woman_edan);
                    break;
                case YUAN:
                    description = (String) this.getResources().getText(R.string.des_hair_woman_yuan);
                    break;
            }
        } else {
            switch (faceType) {
                case FANG:
                    description = (String) this.getResources().getText(R.string.des_glasses_fang);
                    break;
                case CHANG:
                    description = (String) this.getResources().getText(R.string.des_glasses_chang);
                    break;
                case LING:
                    description = (String) this.getResources().getText(R.string.des_glasses_ling);
                    break;
                case EDAN:
                    description = (String) this.getResources().getText(R.string.des_glasses_edan);
                    break;
                case YUAN:
                    description = (String) this.getResources().getText(R.string.des_glasses_yuan);
                    break;
            }
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
                startActivity(new Intent(HairAndGlassesActivity.this, SelectModelActivity.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
            }
            if ((endX - startX) > 50 && Math.abs(startY - endY) < 200) {
                startActivity(new Intent(HairAndGlassesActivity.this, FaceResultActivity.class));
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

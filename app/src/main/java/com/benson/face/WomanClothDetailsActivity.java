package com.benson.face;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class WomanClothDetailsActivity extends AppCompatActivity {
    private int index;
    private int[] arr;
    private String style;
    private GestureDetector gue;

    private TextView descriptionText;

    private ImageView clothsImg;
    private ImageView shoesImg;
    private ImageView wristWatchImg;
    private ImageView briefCaseImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.woman_clothing);
        initView();
        MyApplication myApplication = (MyApplication) getApplication();
        index = getIntent().getIntExtra("index", 0);
        arr = getIntent().getIntArrayExtra("arr");
        style = getIntent().getStringExtra("style");
        clothsImg.setImageBitmap(getClothImg(myApplication.user.gender, myApplication.user.height, index));
        String shoes = "shoes";
        shoesImg.setImageBitmap(getOtherImg(myApplication.user.gender, shoes));
        String wristwatch = "wrist_watch";
        wristWatchImg.setImageBitmap(getOtherImg(myApplication.user.gender, wristwatch));
        String briefcase = "brief_case";
        briefCaseImg.setImageBitmap(getOtherImg(myApplication.user.gender, briefcase));
        descriptionText.setText(getDescription());//获取strings.xml里的服装描述文本信息
        gue = new GestureDetector(WomanClothDetailsActivity.this, new WomanClothDetailsActivity.MyGestureListener());
    }

    private void initView() {
        clothsImg = findViewById(R.id.cloths);
        shoesImg = findViewById(R.id.shoes);
        wristWatchImg = findViewById(R.id.wristwatch);
        briefCaseImg = findViewById(R.id.briefcase);
        descriptionText = findViewById(R.id.description);
    }

    private Bitmap getClothImg(String gender, String height, int index) {
        StringBuilder sb = new StringBuilder("cloth/");
        if ("w".equals(gender)) {
            sb.append("woman");
        } else {
            sb.append("man");
        }
        sb.append("/height/").append(getHeight(Integer.parseInt(height))).append("/").append(getWomanColor(style)).append("/").append(arr[index]).append(".png");
        try {
            InputStream in = getAssets().open(sb.toString());
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap getOtherImg(String gender, String other) {
        StringBuilder sb = new StringBuilder("cloth/");
        if ("w".equals(gender)) {
            sb.append("woman");
        } else {
            sb.append("man");
        }
        sb.append("/height/").append(getWomanColor(style)).append("/").append(other).append(".png");
        try {
            InputStream in = getAssets().open(sb.toString());
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getHeight(int height) {
        if (height < 300) {
            return 160;
        } else if (height < 350) {
            return 160;
        } else if (height < 400) {
            return 165;
        } else if (height < 450) {
            return 170;
        } else if (height < 500) {
            return 175;
        } else {
            return 180;
        }
    }

    private String getWomanColor(String style) {
        String color;
        switch (style) {
            case "0":
                color = "black_and_white";
                break;
            case "1":
                color = "grey_stripes";
                break;
            case "2":
                color = "grey_stripes2";
                break;
            case "3":
                color = "izzue";
                break;
            case "4":
                color = "OL";
                break;
            default:
                color = "white";
                break;
        }
        return color;
    }

    private String getDescription() {
        String description;
        switch (style) {
            case "0":
                description = (String) this.getResources().getText(R.string.des_woman_black_and_white);
                break;
            case "1":
                description = (String) this.getResources().getText(R.string.des_woman_grey_stripes);
                break;
            case "2":
                description = (String) this.getResources().getText(R.string.des_woman_grey_stripes2);
                break;
            case "3":
                description = (String) this.getResources().getText(R.string.des_woman_izzue);
                break;
            case "4":
                description = (String) this.getResources().getText(R.string.des_woman_OL);
                break;
            default:
                description = (String) this.getResources().getText(R.string.des_woman_white);
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
                Intent intent = new Intent(WomanClothDetailsActivity.this, ClothEndActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("arr", arr);
                intent.putExtra("style", style);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
            }//左滑
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

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

public class ManClothDetailsActivity extends AppCompatActivity {
    private int index;
    public static Bitmap bp;
    private int[] arr;
    private String style;
    private GestureDetector gue;

    private String shoes = "shoes";
    private String necktie = "necktie";
    private String wristwatch = "wrist_watch";
    private String briefcase = "brief_case";

    private TextView descriptionText;

    private ImageView clothsImg;
    private ImageView shoesImg;
    private ImageView necktieImg;
    private ImageView wristWatchImg;
    private ImageView briefCaseImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_clothing);
        initView();
        MyApplication myApplication = (MyApplication) getApplication();
        index = getIntent().getIntExtra("index", 0);
        arr = getIntent().getIntArrayExtra("arr");
        style = getIntent().getStringExtra("style");
        clothsImg.setImageBitmap(getClothImg(myApplication.user.height, index));
        shoesImg.setImageBitmap(getOtherImg(shoes));
        necktieImg.setImageBitmap(getOtherImg(necktie));
        wristWatchImg.setImageBitmap(getOtherImg(wristwatch));
        briefCaseImg.setImageBitmap(getOtherImg(briefcase));
        descriptionText.setText(getDescription());//获取strings.xml里的服装描述文本信息
        gue = new GestureDetector(ManClothDetailsActivity.this, new ManClothDetailsActivity.MyGestureListener());
    }

    private void initView() {
        clothsImg = (ImageView) findViewById(R.id.cloths);
        shoesImg = (ImageView) findViewById(R.id.shoes);
        necktieImg = (ImageView) findViewById(R.id.necktie);
        wristWatchImg = (ImageView) findViewById(R.id.wristwatch);
        briefCaseImg = (ImageView) findViewById(R.id.briefcase);
        descriptionText = (TextView) findViewById(R.id.description);
    }

    private Bitmap getClothImg(String height, int index) {
        StringBuilder sb = new StringBuilder("cloth/");
        sb.append("man");
        sb.append("/height/").append(getHeight(Integer.parseInt(height))).append("/").append(getManColor(style)).append("/").append(arr[index]).append(".png");
        try {
            InputStream in = getAssets().open(sb.toString());
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap getOtherImg(String other) {
        StringBuilder sb = new StringBuilder("cloth/");
        sb.append("man");
        sb.append("/height/").append(getManColor(style)).append("/").append(other).append(".png");
        try {
            InputStream in = getAssets().open(sb.toString());
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final int getHeight(int height) {
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

    private String getManColor(String style) {
        String color = null;
        switch (style) {
            case "0":
                color = "black";
                break;
            case "1":
                color = "blue";
                break;
            case "2":
                color = "gray";
                break;
            case "3":
                color = "gray2";
                break;
            case "4":
                color = "tibetan_blue";
                break;
            case "5":
                color = "tibetan_blue";
                break;
            default:
                color = "blue";
                break;
        }
        return color;
    }

    private String getDescription() {
        String description = null;
        switch (style) {
            case "0":
                description = (String) this.getResources().getText(R.string.des_man_black);
                break;
            case "1":
                description = (String) this.getResources().getText(R.string.des_man_blue);
                break;
            case "2":
                description = (String) this.getResources().getText(R.string.des_man_gray);
                break;
            case "3":
                description = (String) this.getResources().getText(R.string.des_man_gray2);
                break;
            case "4":
                description = (String) this.getResources().getText(R.string.des_man_tibetan_blue);
                break;
            case "5":
                description = (String) this.getResources().getText(R.string.des_man_tibetan_blue);
                break;
            default:
                description = (String) this.getResources().getText(R.string.des_man_blue);
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
                Intent intent = new Intent(ManClothDetailsActivity.this, ClothEndActivity.class);
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

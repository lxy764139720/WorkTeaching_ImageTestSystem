package com.benson.face;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ShowClothActivity extends AppCompatActivity {
    private int index;
    private int[] arr;
    private String style;
    private GestureDetector gue;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cloth);
        ImageView imageView = findViewById(R.id.cloth);
        index = getIntent().getIntExtra("index", 0);
        arr = getIntent().getIntArrayExtra("arr");
        MyApplication myApplication = (MyApplication) getApplication();
        gender = myApplication.user.gender;
        if (arr == null) {
            arr = new int[3];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = 0;
            }
            Random random = new Random(System.currentTimeMillis());
            for (int i = 0; i < arr.length; i++) {
                int num = 1;
                while (true) {
                    num = random.nextInt(8) + 1;
                    if (num != arr[0] && num != arr[1] && num != arr[2]) break;
                }
                arr[i] = num;
            }
        }
        Random random = new Random(System.currentTimeMillis());
        style = random.nextInt(6) + "";
        imageView.setImageBitmap(getImg(gender, myApplication.user.height, index));
        Toast.makeText(ShowClothActivity.this, "左滑可查看详情以及其他配件的推荐", Toast.LENGTH_LONG).show();
        gue = new GestureDetector(ShowClothActivity.this, new MyGestureListener());
    }

    private Bitmap getImg(String gender, String height, int index) {
        StringBuilder sb = new StringBuilder("cloth/");
        if ("w".equals(gender)) {
            sb.append("woman");
            sb.append("/height/").append(getHeight(Integer.parseInt(height))).append("/").append(getWomanColor(style)).append("/").append(arr[index]).append(".png");
        } else {
            sb.append("man");
            sb.append("/height/").append(getHeight(Integer.parseInt(height))).append("/").append(getManColor(style)).append("/").append(arr[index]).append(".png");
        }
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
                color = "black";
                break;
        }
        return color;
    }

    private String getWomanColor(String style) {
        String color = null;
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
            case "5":
                color = "white";
                break;
            default:
                color = "white";
                break;
        }
        return color;
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
                if ("w".equals(gender)) {
                    Intent intent = new Intent(ShowClothActivity.this, WomanClothDetailsActivity.class);
                    intent.putExtra("index", index);
                    intent.putExtra("arr", arr);
                    intent.putExtra("style", style);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                } else {
                    Intent intent = new Intent(ShowClothActivity.this, ManClothDetailsActivity.class);
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

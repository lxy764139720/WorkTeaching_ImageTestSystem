package com.benson.face;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WomanWorkClothDetailsActivity extends AppCompatActivity {
    private String scene;
    private int index;
    private int[] arr;
    private String style;
    private GestureDetector gue;

    private TextView descriptionText;
    private ImageView shoesImg;
    private ImageView wristWatchImg;
    private ImageView briefCaseImg;
    private ViewPager mViewPager;
    private List<View> mViews;
    private ViewGroup mDotViewGroup;
    private List<ImageView> mDotViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.woman_work_clothing);
        Toast.makeText(WomanWorkClothDetailsActivity.this, "左滑可返回主界面", Toast.LENGTH_SHORT).show();
        initView();
        MyApplication myApplication = (MyApplication) getApplication();
        index = getIntent().getIntExtra("index", 0);
        arr = getIntent().getIntArrayExtra("arr");
        style = getIntent().getStringExtra("style");
        scene = getIntent().getStringExtra("scene");
        //滑动图片
        mViews = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(getClothImg(myApplication.user.gender, Integer.toString((int) myApplication.user.height), i));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mViews.add(imageView);
            //设置位置点
            ImageView dot = new ImageView(this);
            dot.setImageResource(R.mipmap.unselected);
            dot.setMaxHeight(100);
            dot.setMaxWidth(100);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
            layoutParams.leftMargin = 20;
            dot.setLayoutParams(layoutParams);
            dot.setEnabled(false);

            mDotViewGroup.addView(dot);
            mDotViews.add(dot);
        }
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);
        setDotViews(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setDotViews(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //动态设置其他图片
        String shoes = "shoes";
        shoesImg.setImageBitmap(getOtherImg(myApplication.user.gender, shoes));
        String wristwatch = "wrist_watch";
        wristWatchImg.setImageBitmap(getOtherImg(myApplication.user.gender, wristwatch));
        String briefcase = "brief_case";
        briefCaseImg.setImageBitmap(getOtherImg(myApplication.user.gender, briefcase));
        descriptionText.setText(getDescription());//获取strings.xml里的服装描述文本信息
        gue = new GestureDetector(WomanWorkClothDetailsActivity.this, new WomanWorkClothDetailsActivity.MyGestureListener());
    }

    private void setDotViews(int i) {
        for (int j = 0; j < mDotViews.size(); j++) {
            mDotViews.get(j).setImageResource(i == j ? R.mipmap.selected : R.mipmap.unselected);
        }
    }

    private void initView() {
        mViewPager = findViewById(R.id.viewPager);
        mDotViewGroup = findViewById(R.id.dotGrop);
        shoesImg = findViewById(R.id.shoes);
        wristWatchImg = findViewById(R.id.wristwatch);
        briefCaseImg = findViewById(R.id.briefcase);
        descriptionText = findViewById(R.id.description);
    }

    PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View child = mViews.get(position);
            container.addView(child);
            return child;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViews.get(position));
        }
    };

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
                Intent intent = new Intent(WomanWorkClothDetailsActivity.this, SelectModelActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
            }//左滑
            if ((endX - startX) > 50 && Math.abs(startY - endY) < 200) {
                Intent intent = new Intent(WomanWorkClothDetailsActivity.this, ComplexionActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("arr", arr);
                intent.putExtra("scene", scene);
                intent.putExtra("style", ((MyApplication) getApplication()).user.bodyType);//返回上一界面保存数据
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

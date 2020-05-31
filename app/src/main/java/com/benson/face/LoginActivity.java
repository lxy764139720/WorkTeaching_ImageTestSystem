package com.benson.face;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Spinner genderSpinner;
    private EditText nameText;
    private EditText heightText;
    private EditText shoulderText;
    private EditText hipText;
    private EditText waistText;
    private EditText weightText;

    private class GenderAdapter extends BaseAdapter {
        //BaseAdapter的使用方法：https://www.jianshu.com/p/24833a2cffd1
        private final String[] data = new String[]{"男", "女"};

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.item_gender, null);
            TextView tv = view.findViewById(R.id.genderText);
            tv.setText(data[position]);
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int bgNumber = getIntent().getIntExtra("bgNumber", 1);
        String bgName = "start" + bgNumber + "a";
        int bgID = getResources().getIdentifier(bgName, "drawable", "com.benson.face");
        getWindow().getDecorView().setBackgroundResource(bgID);
        setContentView(R.layout.activity_login);

        //init 实例化各个变量
        nameText = findViewById(R.id.name);
        heightText = findViewById(R.id.height);
        shoulderText = findViewById(R.id.shoulder);
        hipText = findViewById(R.id.hip);
        waistText = findViewById(R.id.waist);
        weightText = findViewById(R.id.weight);
        genderSpinner = findViewById(R.id.gender);
        genderSpinner.setAdapter(new GenderAdapter());
    }

    //判断登录，实际是需要获取性别
    public void login(View view) {
        if ("".equals(heightText.getText().toString())
                || "".equals(shoulderText.getText().toString())
                || "".equals(hipText.getText().toString())
                || "".equals(waistText.getText().toString())
                || "".equals(weightText.getText().toString())) {
            Toast.makeText(this, "用户信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Float.parseFloat(heightText.getText().toString()) < 130 || Float.parseFloat(heightText.getText().toString()) > 220
                || Float.parseFloat(shoulderText.getText().toString()) < 30 || Integer.parseInt(shoulderText.getText().toString()) > 120
                || Float.parseFloat(hipText.getText().toString()) < 30 || Float.parseFloat(hipText.getText().toString()) > 140
                || Float.parseFloat(waistText.getText().toString()) < 30 || Float.parseFloat(waistText.getText().toString()) > 140
                || Float.parseFloat(weightText.getText().toString()) < 30 || Float.parseFloat(weightText.getText().toString()) > 120) {
            Toast.makeText(this, "请填写正确的数值", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.gender = genderSpinner.getSelectedItemPosition() == 0 ? "m" : "w"; //获取性别
        user.name = nameText.getText().toString();
        user.height = Float.parseFloat(heightText.getText().toString());
        user.shoulder = Float.parseFloat(shoulderText.getText().toString());
        user.hip = Float.parseFloat(hipText.getText().toString());
        user.waist = Float.parseFloat(waistText.getText().toString());
        user.weight = Float.parseFloat(weightText.getText().toString());
        user.bodyType(); //计算体型
        ((MyApplication) getApplication()).user = user;  //交给活动保存
        startActivity(new Intent(this, SelectModelActivity.class)); //页面跳转
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left); //切换动画
    }
}

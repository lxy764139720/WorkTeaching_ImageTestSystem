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

public class StudentLoginActivity extends AppCompatActivity {
    private EditText nameText;
    private Spinner genderSpinner;

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
            View view = LayoutInflater.from(StudentLoginActivity.this).inflate(R.layout.item_gender, null);
            TextView tv = view.findViewById(R.id.genderText);
            tv.setText(data[position]);
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        //init 实例化各个变量
        nameText = findViewById(R.id.name);
        genderSpinner = findViewById(R.id.gender);
        genderSpinner.setAdapter(new GenderAdapter());
    }

    //判断登录，实际是需要获取性别
    public void login(View view) {
        if ("".equals(nameText.getText().toString())) {
            Toast.makeText(this, "名字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.name = nameText.getText().toString();
        user.gender = genderSpinner.getSelectedItemPosition() == 0 ? "m" : "w"; //获取性别
        ((MyApplication) getApplication()).user = user;
        startActivity(new Intent(this, SelectModelActivity.class)); //页面跳转
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left); //切换动画
    }
}

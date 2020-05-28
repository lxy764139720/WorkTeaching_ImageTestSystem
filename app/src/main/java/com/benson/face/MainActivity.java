package com.benson.face;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        Random random = new Random(System.currentTimeMillis());
        final int bgNumber = random.nextInt(3) + 1;
        final String bgName = "start" + bgNumber;
        int bgID = getResources().getIdentifier(bgName, "drawable", "com.benson.face");
        getWindow().getDecorView().setBackgroundResource(bgID);
        setContentView(R.layout.activity_main);
        //动态申请权限
        checkNeedPermissions();
        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(3000);//使程序休眠三秒
                    Intent it = new Intent(getApplicationContext(), LoginActivity.class);//启动LoginActivity
                    it.putExtra("bgNumber", bgNumber);
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }

    public void next(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
    }

    /**
     * 动态处理申请权限的结果
     * 用户点击同意或者拒绝后触发
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果码
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                //获取权限验证
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //权限申请失败
                    Toast.makeText(this, "权限申请失败，用户拒绝权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 检测需要申请的权限
     */
    private void checkNeedPermissions() {
        //6.0以上需要动态申请权限 动态权限校验 Android 6.0 的 oppo & vivo 手机时，始终返回 权限已被允许 但是当真正用到该权限时，却又弹出权限申请框。
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //多个权限一起申请
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }  //已经全部申请 初始化相机资源
        }//6.0以下不用动态申请
    }

}

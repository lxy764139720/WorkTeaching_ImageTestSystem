package com.benson.face;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;

public class FaceCameraActivity extends AppCompatActivity implements View.OnClickListener {

    private volatile boolean isTake = false;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private CameraPresenter mCameraPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        SurfaceView mSurfaceView = findViewById(R.id.photo);
        mSurfaceView.setKeepScreenOn(true);
        View takePhotoBtn = findViewById(R.id.mCatture);
        takePhotoBtn.setOnClickListener(this);
        //初始化CameraPresenter
        mCameraPresenter = new CameraPresenter(this, mSurfaceView);
        //设置前置摄像头
        mCameraPresenter.setFrontOrBack(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mDialogBuilder = new AlertDialog.Builder(this).setMessage("正在识别");
    }

    @Override
    public void onClick(View v) {
        Camera mCamera = this.mCameraPresenter.getmCamera();
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                mDialog = mDialogBuilder.show();
                final Bitmap bp = BitmapFactory.decodeByteArray(data, 0, data.length);  //关于图片的操作都是需要用到这个类BitmapFactory
                Matrix matrix = new Matrix();
                matrix.postRotate(270);  //前置270 后置90
                final Bitmap newBp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                newBp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                if (!isTake) {
                    isTake = true;
                    System.out.println(isTake);
                    FaceppDetect.detect(bos.toByteArray(), new FaceppDetect.CallBack() { //调用API
                        @Override
                        public void success(String result) {  //成功调用API后返回结果
                            bp.recycle();
                            newBp.recycle();
                            isTake = false;
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDialog.dismiss();
                                    }
                                });
                                JSONObject json = new JSONObject(result);
                                if (json.getJSONArray("faces").length() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(FaceCameraActivity.this).setMessage("未检测到，请重新拍摄").setCancelable(true).show();
                                        }
                                    });
                                } else {
                                    ((MyApplication) getApplication()).faceResult = result;
                                    startActivity(new Intent(FaceCameraActivity.this, FaceResultActivity.class));
                                    overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                                    finish();
                                }
                            } catch (JSONException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(FaceCameraActivity.this).setMessage("未检测到，请重新拍摄").setCancelable(true).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void error(EOFException exception) {
                            isTake = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FaceCameraActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraPresenter != null) {
            mCameraPresenter.releaseCamera();
        }
    }
}

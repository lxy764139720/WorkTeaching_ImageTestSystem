package com.benson.face;

import android.app.AlertDialog;
import android.content.ComponentName;
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
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;

public class BodyCameraActivity extends AppCompatActivity implements View.OnClickListener {

    private volatile boolean isTake = false;
    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;
    private CameraPresenter mCameraPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_camera);
        SurfaceView mSurfaceView = findViewById(R.id.photo);
        mSurfaceView.setKeepScreenOn(true);
        ImageView takePhotoBtn = findViewById(R.id.btn);
        takePhotoBtn.setOnClickListener(this);
        //初始化CameraPresenter
        mCameraPresenter = new CameraPresenter(this, mSurfaceView);
        //设置后置摄像头
        mCameraPresenter.setFrontOrBack(Camera.CameraInfo.CAMERA_FACING_BACK);
        mDialogBuilder = new AlertDialog.Builder(this).setMessage("正在识别");
    }

    @Override
    public void onClick(View v) {
        Camera mCamera = this.mCameraPresenter.getmCamera();
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                mDialog = mDialogBuilder.show();
                final Bitmap bp = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                // 和顺序有关，必须先缩放再旋转
                matrix.setScale(0.25f, 0.25f);  //图片压缩不超过2M，大小不超过1080*1080
                matrix.postRotate(90);  //前置270 后置90
                final Bitmap newBp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                newBp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                System.out.println(newBp.getByteCount());
                System.out.println(newBp.getWidth());
                System.out.println(newBp.getHeight());
                if (!isTake) {
                    isTake = true;
                    System.out.println(isTake);
                    FaceppDetect1.detect(bos.toByteArray(), new FaceppDetect1.CallBack() {
                        @Override
                        public void success(String result) {
                            bp.recycle();
                            newBp.recycle();
                            isTake = false;
                            System.out.println(result);
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDialog.dismiss();
                                    }
                                });
                                JSONObject json = new JSONObject(result);
                                if (json.getJSONArray("humanbodies").length() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(BodyCameraActivity.this).setMessage("未检测到，请重新拍摄").setCancelable(true).show();
                                        }
                                    });
                                } else {
                                    Intent intent = new Intent(BodyCameraActivity.this, ComplexionStartActivity.class);
                                    JSONObject body = json.getJSONArray("humanbodies").getJSONObject(0);
                                    JSONObject rec = body.getJSONObject("humanbody_rectangle");
                                    ((MyApplication) getApplication()).user.height = Integer.parseInt(rec.getString("height"));
                                    intent.putExtra("top", rec.getInt("top"));
                                    intent.putExtra("left", rec.getInt("left"));
                                    intent.putExtra("width", rec.getInt("width"));
                                    intent.putExtra("height", rec.getInt("height"));
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(BodyCameraActivity.this).setMessage("未检测到，请重新拍摄").setCancelable(true).show();
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
                                    Toast.makeText(BodyCameraActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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

package com.benson.face;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements Camera.PreviewCallback, View.OnClickListener {

    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int SNAPSHOT_CODE = 5;

    private int angle = 0;
    private float oldDist = 1f;
    private volatile boolean isTake = false;

    private AlertDialog.Builder mDialogBuilder;
    private AlertDialog mDialog;

    private CameraSurfaceView mSurfaceView;
    private ImageView takePhotoBtn;
    private Camera mCamera;
    private CameraTool cameraTool;          //cameraTool封装了android的系统相机，使用此模块可轻松实现拍照后获取照片路径的功能
    private CameraTool.CameraToolCallback callback = new CameraTool.CameraToolCallback() {
        @Override
        public void onOpenCamera(Camera camera) {
            CameraActivity.this.mCamera = camera;
            Camera.Parameters parameters = camera.getParameters();

            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);  //自动对焦
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);  //场景模式关闭
            camera.setParameters(parameters);
//            camera.setPreviewCallback(cameraTool);
            camera.setPreviewCallback(CameraActivity.this);
//            camera.setPreviewCallback(faceView);
            // 旋转预览图片angle度
            int angle = MyApplication.getPhotoRotationAngle();
            camera.setDisplayOrientation(angle);
//            initFace();
            if (mSurfaceView != null) {
                mSurfaceView.setCamera(camera);
            }
        }

        @Override
        public void onOpenCameraError(String error) {
            Toast.makeText(CameraActivity.this, "暂不能使用相机设备", Toast.LENGTH_SHORT).show();
        }
    };

    private void initData() {
        angle = MyApplication.getPhotoRotationAngle();
        if (cameraTool == null) {
            cameraTool = new CameraTool(this, callback);    //实例化cameraTool
        }
        if (!cameraTool.hasCameraDevice()) {
            new AlertDialog.Builder(this)
                    .setMessage("无相机设备")
                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
            return;
        }

        // 动态申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                return;
            }
        }

        int cameraId = MyApplication.getCameraId();
        int cameraNumber = Camera.getNumberOfCameras();
        if (cameraId < cameraNumber) {                 //打开相机
            cameraTool.openCamera(cameraId);
        } else {
            cameraTool.openCamera();
        }
    }

    private static float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void handleZoom(boolean isZoomIn, Camera camera) {
        Camera.Parameters params = camera.getParameters();
        if (params.isZoomSupported()) {
            int maxZoom = params.getMaxZoom();
            int zoom = params.getZoom();
            if (isZoomIn && zoom < maxZoom - 10) {
                zoom = zoom + 10;
            } else if (zoom > 10) {
                zoom = zoom - 10;
            }
            params.setZoom(zoom);
            camera.setParameters(params);
        } else {
//            Log.i(TAG, "zoom not supported");
        }
    }

    private static Rect calculateTapArea(float x, float y, float coefficient, Camera.Size previewSize) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / previewSize.width - 1000);
        int centerY = (int) (y / previewSize.height - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);

        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    private static void handleFocus(MotionEvent event, Camera camera) {
        Camera.Parameters params = camera.getParameters();
        Camera.Size previewSize = params.getPreviewSize();
        Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f, previewSize);

        camera.cancelAutoFocus();

        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 800));
            params.setFocusAreas(focusAreas);
        } else {
//            Log.i(TAG, "focus areas not supported");
        }
        final String currentFocusMode = params.getFocusMode();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        camera.setParameters(params);

        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                Camera.Parameters params = camera.getParameters();
                params.setFocusMode(currentFocusMode);
                camera.setParameters(params);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 1) {
            handleFocus(event, mCamera);
        } else {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = getFingerSpacing(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float newDist = getFingerSpacing(event);
                    if (newDist > oldDist) {
                        handleZoom(true, mCamera);
                    } else if (newDist < oldDist) {
                        handleZoom(false, mCamera);
                    }
                    oldDist = newDist;
                    break;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initData();
                } else {
                    Toast.makeText(this, "需要使用相机权限", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initData();
        mSurfaceView = findViewById(R.id.photo);
        mSurfaceView.setKeepScreenOn(true);
        takePhotoBtn = findViewById(R.id.mCatture);
        takePhotoBtn.setOnClickListener(this);
        if (mCamera != null) {
            mSurfaceView.setCamera(mCamera);
        }
        mDialogBuilder = new AlertDialog.Builder(this).setMessage("正在识别");
    }

    @Override
    public void onPreviewFrame(final byte[] data, final Camera camera) {

//            new Thread() {
//                @Override
//                public void run() {
//                    getImage(data, camera);
//                }
//            }.start();
//            isTakePhoto = false;

    }

    public byte[] getImage(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();

        YuvImage image = new YuvImage(data, parameters.getPreviewFormat(), size.width, size.height, null);
        Rect rect = new Rect(0, 0, size.width, size.height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compressToJpeg(rect, 100, baos);
        byte[] imageData = baos.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        // 旋转图片
        Matrix m = new Matrix();
        int cameraId = MyApplication.getCameraId();
        if (cameraId == 0) {
            m.setRotate(angle);
        } else {
            m.setRotate(-angle);
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        baos.reset();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageData = baos.toByteArray();
        return imageData;
    }

    @Override
    public void onClick(View v) {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                mDialog = mDialogBuilder.show();
                final Bitmap bp = BitmapFactory.decodeByteArray(data, 0, data.length);  //关于图片的操作都是需要用到这个类BitmapFactory
                Matrix matrix = new Matrix();
                matrix.postRotate(0);
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
                                            new AlertDialog.Builder(CameraActivity.this).setMessage("未检测到，请重新拍摄").setCancelable(true).show();
                                        }
                                    });
                                } else {
                                    ((MyApplication) getApplication()).faceResult = result;
                                    startActivity(new Intent(CameraActivity.this, FaceResultActivity.class));
                                    overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                                    finish();
                                }
                            } catch (JSONException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(CameraActivity.this).setMessage("未检测到，请重新拍摄").setCancelable(true).show();
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
                                    Toast.makeText(CameraActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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
        if (mCamera != null) {
            mCamera.release();
        }

        if (cameraTool != null) {
//            cameraTool.stop();
        }
    }
}

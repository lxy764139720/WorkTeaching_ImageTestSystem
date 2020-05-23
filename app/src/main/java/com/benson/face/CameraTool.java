package com.benson.face;


import android.content.Context;
import android.content.pm.PackageManager;

import android.hardware.Camera;
import android.util.Log;


import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class CameraTool implements Camera.PreviewCallback {

    private static final String TAG = CameraTool.class.getSimpleName();

    private Camera camera;
    private Context context;
    private CameraToolCallback cameraToolCallback;
    private Callback callback;
    private int angle = 0;

    public CameraTool(Context context, CameraToolCallback callback) {
        this.context = context;
        this.cameraToolCallback = callback;
        angle = MyApplication.getPhotoRotationAngle();
    }

    /**
     * 判断收集设备是否有相机设备
     *
     * @return
     */
    public boolean hasCameraDevice() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void openCamera(final int cameraId) {
        new Thread() {
            @Override
            public void run() {
                try {
                    camera = Camera.open(cameraId);
                    if (camera != null) {
                        cameraToolCallback.onOpenCamera(camera);
                    } else {
                        cameraToolCallback.onOpenCameraError("暂不能打开相机设备");
                    }
                } catch (RuntimeException e) {
                    Log.e(TAG, "openCamera: Camera.open(cameraId)", e);
                    cameraToolCallback.onOpenCameraError(e.getMessage());
                }
            }
        }.start();
    }

    public void openCamera() {
        new Thread() {
            @Override
            public void run() {
                try {
                    camera = Camera.open();
                    if (camera != null) {
                        cameraToolCallback.onOpenCamera(camera);
                    } else {
                        cameraToolCallback.onOpenCameraError("暂不能打开相机设备");
                    }
                } catch (RuntimeException e) {
                    Log.e(TAG, "openCamera: Camera.open(cameraId)", e);
                    cameraToolCallback.onOpenCameraError(e.getMessage());
                }
            }
        }.start();
    }

    public Camera getCamera() {
        return camera;
    }

    public static int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }

    private long lastTime = 0;
    // 刷新延迟时间
    private long delay = 250;
    // 保存脸图片的队列，最多100张
    private LinkedBlockingQueue<byte[]> faceQueue = new LinkedBlockingQueue<>(100);

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // 设置刷新间隔
        if (lastTime != 0 && System.currentTimeMillis() - lastTime < delay) return;
        lastTime = System.currentTimeMillis();
        // 队列已经有100张图片，丢掉新的图片帧
        if (faceQueue.size() == 100) return;
        try {
            faceQueue.add(data.clone());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onCapturedFace(byte[] bitmap);
    }

    public interface CameraToolCallback {
        void onOpenCamera(Camera camera);

        void onOpenCameraError(String error);
    }

    private void setPreviewSize(int width, int height) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        Camera.Size bestSize = null;
        double showRate = width * 1.0 / height;
        double rate, bestRate = 0;
        for (Camera.Size size :
                sizeList) {
            if (bestSize == null) {
                bestSize = size;
                bestRate = size.width * 1.0 / size.height;
                continue;
            }
            rate = size.width * 1.0 / size.height;
            if (Math.abs(rate - showRate) < Math.abs(bestRate - showRate)) {
                bestSize = size;
                bestRate = size.width * 1.0 / size.height;
            }
        }

        parameters.setPreviewSize(bestSize.width, bestSize.height);
        camera.setParameters(parameters);
    }
}

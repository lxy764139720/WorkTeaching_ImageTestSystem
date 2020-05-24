package com.benson.face;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

import static android.hardware.Camera.getNumberOfCameras;

public class CameraPresenter {
    private AppCompatActivity mAppCompatActivity;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private int mCameraId;
    private Camera.Parameters mParameters;

    //获取屏幕宽和高
    private int screenWidth, screenHeight;

    public CameraPresenter(AppCompatActivity mAppCompatActivity, SurfaceView mSurfaceView) {
        this.mAppCompatActivity = mAppCompatActivity;
        this.mSurfaceHolder = mSurfaceView.getHolder();
        DisplayMetrics dm = new DisplayMetrics();
        mAppCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //获取宽高像素
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        Log.d("sssd-手机宽高尺寸:", screenWidth + "*" + screenHeight);
        init();
    }

    public Camera getmCamera() {
        return mCamera;
    }

    /**
     * 初始化增加回调
     */
    private void init() {
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //surface创建时执行
                if (mCamera == null) {
                    // mCameraId是后置还是前置 0是后置 1是前置
                    openCamera(mCameraId);
                }
                //设置预览
                startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //surface绘制时执行
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //surface销毁时执行
                releaseCamera();
            }
        });
    }

    /**
     * 设置前置还是后置
     *
     * @param mCameraId 前置还是后置
     */
    public void setFrontOrBack(int mCameraId) {
        this.mCameraId = mCameraId;
    }

    /**
     * 打开相机 并且判断是否支持该摄像头
     *
     * @param mCameraId 前置还是后置
     */
    private void openCamera(int mCameraId) {
        //是否支持前后摄像头
        boolean isSupportCamera = isSupport(mCameraId);
        //如果支持
        if (isSupportCamera) {
            try {
                //打开相机并设置参数
                mCamera = Camera.open(mCameraId);
                initParameters(mCamera);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置相机参数
     *
     * @param camera 相机对象
     */
    private void initParameters(Camera camera) {
        try {
            //获取Parameters对象
            mParameters = camera.getParameters();
            //设置预览格式
            mParameters.setPreviewFormat(ImageFormat.NV21);
            //设置预览界面尺寸
            setPreviewSize();
            //设置保存图片的尺寸
            setPictureSize();
            //判断是否支持连续自动对焦图像
            if (isSupportFocus(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                //判断是否支持单次自动对焦
            } else if (isSupportFocus(Camera.Parameters.FOCUS_MODE_AUTO)) {
                //自动对焦(单次)
                mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            //给相机设置参数
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始预览
     */
    private void startPreview() {
        try {
            //根据所传入的SurfaceHolder对象来设置实时预览
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //调整预览角度
            setCameraDisplayOrientation(mAppCompatActivity, mCameraId, mCamera);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保证预览方向正确
     *
     * @param appCompatActivity Activity
     * @param cameraId          相机Id
     * @param camera            相机
     */
    private void setCameraDisplayOrientation(AppCompatActivity appCompatActivity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        //rotation是预览Window的旋转方向，对于手机而言，当在清单文件设置Activity的screenOrientation="portait"时，
        //rotation=0，这时候没有旋转，当screenOrientation="landScape"时，rotation=1。
        int rotation = appCompatActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        //计算图像所要旋转的角度
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // 后置相机
            result = (info.orientation - degrees + 360) % 360;
        }
        //调整预览图像旋转角度
        camera.setDisplayOrientation(result);
    }

    /**
     * 设置保存图片的尺寸
     */
    private void setPictureSize() {
        List<Camera.Size> localSizes = mParameters.getSupportedPictureSizes();
        Camera.Size biggestSize = null;
        Camera.Size fitSize = null;// 优先选预览界面的尺寸
        Camera.Size previewSize = mParameters.getPreviewSize();//获取预览界面尺寸
        float previewSizeScale = 0;
        if (previewSize != null) {
            previewSizeScale = previewSize.width / (float) previewSize.height;
        }

        if (localSizes != null) {
            int cameraSizeLength = localSizes.size();
            for (int n = 0; n < cameraSizeLength; n++) {
                Camera.Size size = localSizes.get(n);
                if (biggestSize == null) {
                    biggestSize = size;
                } else if (size.width >= biggestSize.width && size.height >= biggestSize.height) {
                    biggestSize = size;
                }

                // 选出与预览界面等比的最高分辨率
                if (previewSizeScale > 0
                        && size.width >= previewSize.width && size.height >= previewSize.height) {
                    float sizeScale = size.width / (float) size.height;
                    if (sizeScale == previewSizeScale) {
                        if (fitSize == null) {
                            fitSize = size;
                        } else if (size.width >= fitSize.width && size.height >= fitSize.height) {
                            fitSize = size;
                        }
                    }
                }
            }

            // 如果没有选出fitSize, 那么最大的Size就是FitSize
            if (fitSize == null) {
                fitSize = biggestSize;
            }
            mParameters.setPictureSize(fitSize.width, fitSize.height);
        }

    }

    /**
     * 设置预览界面尺寸
     */
    private void setPreviewSize() {
        //获取系统支持预览大小
        List<Camera.Size> localSizes = mParameters.getSupportedPreviewSizes();
        Camera.Size biggestSize = null;//最大分辨率
        Camera.Size fitSize = null;// 优先选屏幕分辨率
        Camera.Size targetSize = null;// 没有屏幕分辨率就取跟屏幕分辨率相近(大)的size
        Camera.Size targetSiz2 = null;// 没有屏幕分辨率就取跟屏幕分辨率相近(小)的size
        if (localSizes != null) {
            int cameraSizeLength = localSizes.size();
            for (int n = 0; n < cameraSizeLength; n++) {
                Camera.Size size = localSizes.get(n);
                Log.d("sssd-系统支持的尺寸:", size.width + "*" + size.height);
                if (biggestSize == null ||
                        (size.width >= biggestSize.width && size.height >= biggestSize.height)) {
                    biggestSize = size;
                }

                //如果支持的比例都等于所获取到的宽高
                if (size.width == screenHeight
                        && size.height == screenWidth) {
                    fitSize = size;
                    //如果任一宽或者高等于所支持的尺寸
                } else if (size.width == screenHeight
                        || size.height == screenWidth) {
                    if (targetSize == null) {
                        targetSize = size;
                        //如果上面条件都不成立 如果任一宽高小于所支持的尺寸
                    } else if (size.width < screenHeight
                            || size.height < screenWidth) {
                        targetSiz2 = size;
                    }
                }
            }

            if (fitSize == null) {
                fitSize = targetSize;
            }
            if (fitSize == null) {
                fitSize = targetSiz2;
            }
            if (fitSize == null) {
                fitSize = biggestSize;
            }
            Log.d("sssd-最佳预览尺寸:", fitSize.width + "*" + fitSize.height);
            mParameters.setPreviewSize(fitSize.width, fitSize.height);
        }
    }

    /**
     * 判断是否支持对焦格式
     *
     * @param focusMode 对焦格式
     * @return 是否支持
     */
    private boolean isSupportFocus(String focusMode) {
        List<String> focusModeList = this.mParameters.getSupportedFocusModes();
        return focusModeList.contains(focusMode);
    }

    /**
     * 判断是否支持该摄像头
     *
     * @param mCameraId 摄像头ID
     * @return 能否使用
     */
    private static boolean isSupport(int mCameraId) {
        return mCameraId < getNumberOfCameras();
    }

    /**
     * 释放相机资源
     */
    public void releaseCamera() {
        if (mCamera != null) {
            //停止预览
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            //释放相机资源
            mCamera.release();
            mCamera = null;
        }
    }

}

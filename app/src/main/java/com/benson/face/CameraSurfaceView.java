package com.benson.face;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private enum SurfaceState {
        CREATED, CHANGED, DESTROYED
    }

    private static final String TAG = CameraSurfaceView.class.getSimpleName();
    private Context context;
    private SurfaceHolder holder;
    private SurfaceState state = null;
    private Camera camera;
    private boolean isPreviewing = false;
    public int width = 0;
    public int height = 0;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated");
        state = SurfaceState.CREATED;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.d(TAG, String.format("surfaceChanged %d %d %d", format, width, height));
        this.width = width;
        this.height = height;
        state = SurfaceState.CHANGED;
        if (camera != null && !isPreviewing) {
            setPreviewSize(width, height);
            startPreviewDisplay();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceDestroyed");
        state = SurfaceState.DESTROYED;
        stopPreviewDisplay();
    }

    public void setCamera(Camera camera) {
        //camera.setDisplayOrientation(90);
        this.camera = camera;
        if (state == SurfaceState.CHANGED) {
            setPreviewSize(width, height);
            startPreviewDisplay();
        }
    }

    private void startPreviewDisplay() {
        if (camera != null) {
            try {
                camera.setPreviewDisplay(holder);
                isPreviewing = true;
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error while START preview for camera", e);
            }
        }
    }

    private void stopPreviewDisplay() {
        if (camera != null) {
            try {
                camera.stopPreview();
            } catch (Exception e) {
                Log.e(TAG, "Error while STOP preview for camera", e);
            }
        }
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

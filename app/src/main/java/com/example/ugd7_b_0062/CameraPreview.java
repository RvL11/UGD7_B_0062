package com.example.ugd7_b_0062;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);

        mCamera = camera;
        mCamera.setDisplayOrientation(90);

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try
        {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }
        catch (IOException e)
        {
            Log.d("Error", "Camera Error on SurfaceCreated" + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if(mHolder.getSurface() == null)
        {
            return;
        }
        try
        {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }
        catch (IOException e)
        {
            Log.d("Error", "Camera Error on SurfaceCreated" + e.getMessage());
        }
    }
}


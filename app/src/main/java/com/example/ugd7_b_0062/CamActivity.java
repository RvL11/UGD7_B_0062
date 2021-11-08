package com.example.ugd7_b_0062;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;


public class CamActivity extends AppCompatActivity implements SensorEventListener {

    private Camera mCamera = null;
    private CameraPreview mCameraPreview = null;
    private int currentCameraId;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        try
        {
            mCamera = Camera.open();
        }
        catch (Exception e)
        {
            Log.d("Error", "Failed to Get Camera" + e.getMessage());
        }

        if(mCamera != null)
        {
            mCameraPreview = new CameraPreview(this, mCamera);
            FrameLayout camera_view;
            camera_view = findViewById(R.id.FLCamera);
            camera_view.addView(mCameraPreview);
        }

        ImageButton imageClose = findViewById(R.id.imgClose);
        imageClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        Sensor mySensor = sensorEvent.sensor;
        if(mySensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            if(sensorEvent.values[0] == 0)
            {
                if (mCamera != null)
                {
                    mCamera.stopPreview();
                }
                mCamera.release();

                currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

                mCamera = Camera.open(currentCameraId);

                mCameraPreview = new CameraPreview(this, mCamera);
                FrameLayout camera_view = findViewById(R.id.FLCamera);
                camera_view.addView(mCameraPreview);
                Toast.makeText(getApplicationContext(), "Kamera Belakang",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (mCamera != null)
                {
                    mCamera.stopPreview();
                }
                mCamera.release();

                currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

                mCamera = Camera.open(currentCameraId);

                mCameraPreview = new CameraPreview(this, mCamera);
                FrameLayout camera_view = findViewById(R.id.FLCamera);
                camera_view.addView(mCameraPreview);
                Toast.makeText(getApplicationContext(), "Kamera Depan",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }
}
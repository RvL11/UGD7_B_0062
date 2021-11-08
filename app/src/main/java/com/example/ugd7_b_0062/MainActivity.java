package com.example.ugd7_b_0062;

import static android.Manifest.permission.CAMERA;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        checkPermission(CAMERA, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        Sensor mySensor = sensorEvent.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[0];
            float z = sensorEvent.values[0];
            long curTime = System.currentTimeMillis();
            if((curTime - lastUpdate) > 100)
            {
                long diffTime = curTime - lastUpdate;
                lastUpdate = curTime;
                float speed = Math.abs(x+y+z-last_x-last_y-last_z) / diffTime * 10000;

                if(speed > SHAKE_THRESHOLD)
                {
                    Toast.makeText(getApplicationContext(),
                            "Cam Opened", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(this, CamActivity.class);
                    startActivity(i);
                }
                last_z = z;
                last_y = y;
                last_x = x;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }

    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void checkPermission(String permission, int requestCode)
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{CAMERA}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE)
         {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this, "Camera Permission Granted",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Camera Permission Denied",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
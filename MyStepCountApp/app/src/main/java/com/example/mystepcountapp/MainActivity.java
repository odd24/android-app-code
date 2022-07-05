package com.example.mystepcountapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor mStepCount;
    TextView stepCountText;
    Switch mSwitch;

    private static final int PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwitch = (Switch) findViewById(R.id.StepCountSwitch);
        stepCountText = findViewById(R.id.stepCountText);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCount = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        askPermission();


        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }else {

                }
            }
        });
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                        PERMISSION_REQUEST_CODE);
            }
        } else {
            //initStepModule();
            Log.d(TAG, "askPermission: Permission Allow");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 授予权限
                Log.d(TAG, "onRequestPermissionsResult: Permission Allow");
                //initStepModule();
            } else {
                //binding.stepCount.setText("Permission Denied");
                Log.e(TAG, "onRequestPermissionsResult: Permission Denied");
            }
        }
    }

    SensorEventListener listener = new SensorEventListener() {
        //传感器改变时,一般是通过这个方法里面的参数确定传感器状态的改变
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Log.e("TAG", "-------传感状态发生变化-------");
            //获取传感器的数据
            float count = event.values[0];
            String stepCountData = "" + count;
            Log.w(TAG, "onSensorChanged: stepCountData:" + stepCountData);
            stepCountText.setText(stepCountData);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //当精准度改变时
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: listener mStepCount startttttttttt......" );
        sensorManager.registerListener(listener, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: enterrrrrrrrrrrr........");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy: listener mStepCount enddddddddddd......");
        sensorManager.unregisterListener(listener);
    }

}
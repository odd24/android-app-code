package com.example.teststepcountservice;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MyService sensorService;
    private Intent sensorIntent;
    TextView stepCountText;

    private static final int PERMISSION_REQUEST_CODE = 100;

    public void setStepCountText(float count ){
        String stepCountData = "" + count;
        stepCountText.setText(stepCountData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepCountText = findViewById(R.id.stepCountText);
        askPermission();

        sensorService = new MyService();
        sensorIntent = new Intent(this, MyService.class);
        Log.d(TAG, "onCreate: sensorService starttttttttttttttttt");
        startService(sensorIntent);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(sensorIntent);
    }
}
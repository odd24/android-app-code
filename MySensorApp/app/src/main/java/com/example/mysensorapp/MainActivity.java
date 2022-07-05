package com.example.mysensorapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor mLight;
    private Sensor mProximity;
    TextView lux_text;
    TextView ps_text;
    TextView ps_data2;
    Switch mSwitch;
    TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwitch = (Switch) findViewById(R.id.ps_switch);
        lux_text = findViewById(R.id.lux_text);
        ps_text = findViewById(R.id.ps_text);
        ps_data2 = findViewById(R.id.ps_data2);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sensorManager.registerListener(ps_listener, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
                }else {
                    sensorManager.unregisterListener(ps_listener);
                    ps_data2.setText("距感数据：0.0");
                }
            }
        });
    }



    SensorEventListener als_listener = new SensorEventListener() {
        //传感器改变时,一般是通过这个方法里面的参数确定传感器状态的改变
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Log.e("TAG", "-------传感状态发生变化-------");
            //获取传感器的数据
            float lux = event.values[0];
            String light_data = "光感数据：" + lux;
            lux_text.setText(light_data);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //当精准度改变时
        }
    };

    SensorEventListener ps_listener = new SensorEventListener() {
        //传感器改变时,一般是通过这个方法里面的参数确定传感器状态的改变
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Log.e("TAG", "-------传感状态发生变化-------");
            //获取传感器的数据
            float ps = event.values[0];
            String ps_data = "距感数据：" + ps;
            ps_text.setText(ps_data);
            ps_data2.setText(ps_data);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //当精准度改变时
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(als_listener, mLight, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(als_listener);

    }
}
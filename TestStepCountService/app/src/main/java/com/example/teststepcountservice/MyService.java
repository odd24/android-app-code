package com.example.teststepcountservice;

import static android.content.ContentValues.TAG;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;


public class MyService extends Service {
    private static final String CHANNEL_DEFAULT_ID = "MyChannelId";
    private static final CharSequence CHANNEL_DEFAULT_NAME = "My Foreground Service";
    private SensorManager sensorManager;
    private Sensor mStepCount;

    private float stepCountData;

    public MyService() {
    }

    public float getStepCountData() {
        return stepCountData;
    }

    public void setStepCountData(float countData) {
        stepCountData = countData;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCount = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel chan = new NotificationChannel(
                    CHANNEL_DEFAULT_ID,
                    CHANNEL_DEFAULT_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            Notification notification = new Notification.Builder(this, CHANNEL_DEFAULT_ID)
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("StepCount Sensor is running on foreground")
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setChannelId("MyChannelId")
                    .build();

            startForeground(1, notification);
        }

        Log.d(TAG, "onStartCommand: register sensor Listener ...");
        sensorManager.registerListener(listener, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
        return START_STICKY;
    }

    SensorEventListener listener = new SensorEventListener() {
        //传感器改变时,一般是通过这个方法里面的参数确定传感器状态的改变
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Log.e("TAG", "-------传感状态发生变化-------");
            //获取传感器的数据
            float count = event.values[0];
            setStepCountData(count);
            Log.w(TAG, "onSensorChanged: stepCountData:" + count);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //当精准度改变时
        }
    };

    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy: listener mStepCount enddddddddddd......");
        sensorManager.unregisterListener(listener);
    }
}
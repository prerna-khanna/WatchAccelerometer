package com.vasudha.watchaccelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.*;



public class MainActivity extends WearableActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";

    private TextView mTextView;
    private SensorManager sensorManager;
    Sensor accelerometer;
    String str;
    //boolean record;
    Button start_stop;
    FileWriter myWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "On create: initializing sensor services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "on create: registered accelerometer listener");
        mTextView = (TextView) findViewById(R.id.text);
        start_stop = (Button) findViewById(R.id.start_stop);
        start_stop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (start_stop.getText().length() == 0
                        || start_stop.getText().equals("start")) {
                    //start recording

                    Log.d(TAG, "on CLICK start");
                    start_stop.setText("stop");
                } else {

                    start_stop.setText("start");
                }
            }
        });


        // Enables Always-on
        setAmbientEnabled();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(start_stop.getText().equals("stop")){
            str = event.values[0] + " "+ event.values[1] + " "+ event.values[2] + "\n";
            try {
                myWriter = new FileWriter(Environment.getExternalStorageDirectory().toString()+ "/output.txt", true);
                myWriter.write(str);
                myWriter.close();
                Log.d(TAG, str);
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        //Log.d(TAG, "on sensor changed: printing: " + record);
        //Log.d(TAG, event.values[0] + " "+ event.values[1] + " "+ event.values[2]);
        //Log.d(TAG, "onSensorChanged: " +  event.values);
        //return event.values[0] + " "+ event.values[1] + " "+ event.values[2];
    }


    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });
}

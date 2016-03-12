package com.example.fraser.accelerometerdata;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Sensor mySensor;
    private SensorManager SM;

    Values v = new Values();
    private static ArrayList <Values> values = new ArrayList<Values>(10000);
    static DTW dtw;

    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];

    private TextView x,y,z, action, txtOrientation;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create sensor manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        //acceleremoter
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register sensor to listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        //hello
        x = (TextView)findViewById(R.id.xText);
        y = (TextView)findViewById(R.id.yText);
        z = (TextView)findViewById(R.id.zText);
        action = (TextView)findViewById(R.id.txtAction);
        txtOrientation = (TextView)findViewById(R.id.txtOrientation);

        image = (ImageView) findViewById(R.id.imgDirection);
        image.setVisibility(View.GONE);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            txtOrientation.setText("Device Orientation: Portrait" );
        }
        else {
            txtOrientation.setText("Device Orientation: NOT PORTRAIT");
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        final float alpha = (float) 0.8;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        float x1 =  linear_acceleration[0];
        float y1 =  linear_acceleration[1];
        float z1 =  linear_acceleration[2];



        x.setText("X: "+ x1);
        y.setText("Y: "+ y1);
        z.setText("Z: " + z1);

        if(y1 < -2 && z1 > 2)
        {
            start(x1, y1, z1);

            action.setText("Phone Moved Forward!!");
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.forwardarrow);
        }
        else if(y1 < -2 && z1 < -2)
        {
            action.setText("Phone Moved Toward Face!!");
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.backarrow);
        }
        else if(y1 < -2 && x1 > 2)//y down, x up = left tilt
        {
            action.setText("Phone Titled Left!");
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.leftarrow);
        }
        else if(y1 < -2 && x1 < -2)//y down, x down = right tilt
        {
            action.setText("Phone Titled Right!");
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.rightarrow);
        }
        else
        {
            image.setVisibility(View.GONE);
            action.setText("Stable");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    protected void onPause() {
        super.onPause();
        SM.unregisterListener(this);
    }
    protected void onResume() {
        super.onResume();
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void compassOpen(View view)
    {
        Intent intent = new Intent(this, CompassActivity2.class);

    }

    public void start(float x1, float y1, float z1 )
    {
        float[] yValues = {y1};//use one y reading from device
        float[] zValues = {z1};//use one z reading from device

        float y = v.getY();//get the stored reading of a forward gesture
        float[] storedYValues = {y};

        float z = v.getZ();//get the stored reading of a forward gesture
        float[] storedZValues = {z};

        //values.add((new Values(x1, y1, z1)));
        //System.out.println("****** - X: " + x1 + " Y: " + y1 + " Z: " + z1);

        System.out.println("**** DTW - Y - Reading ===  ");
        System.out.println(dtw = new DTW(yValues, storedYValues));
        System.out.println("**** DTW - Z - Reading ===  ");
        System.out.println(dtw = new DTW(zValues, storedZValues));



    }
    public void stop()
    {

    }




}

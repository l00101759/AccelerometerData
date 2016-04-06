package com.example.fraser.accelerometerdata;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Sensor mySensor;
    private SensorManager SM;
    private ProgressBar spinner;

    FileOperations file =  new FileOperations();

    Values v = new Values();
    private static ArrayList <Values> values = new ArrayList<Values>(10000);
    static DTW dtw;

    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];

    private float[] xValues = new float[20];

    float [] xVals = new float[5];
    float [] yVals = new float[5];
    float [] zVals = new float[5];

    List<Float> xValList;//x
    float[] floatXValues;
    List<Float> yValList;//y
    float[] floatYValues;
    List<Float> zValList;//z
    float[] floatZValues;

    float [] storedX = v.getxValues();
    float [] storedY = v.getyValues();
    float [] storedZ = v.getzValues();

    double xDistance, yDistance, zDistance, distance;


    int counter  = 0;
    Boolean clickedFlag = false;

    private TextView x,y,z, action, txtOrientation, txtWarping;

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
        action = (TextView)findViewById(R.id.txtAction);
        //txtWarping = (TextView)findViewById(R.id.txtWarping);

        //image = (ImageView) findViewById(R.id.imgDirection);
        //image.setVisibility(View.GONE);

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);



        action.setText("Gesture detection");

        /*if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            txtOrientation.setText("Device Orientation: Portrait");
        }
        else {
            txtOrientation.setText("Device Orientation: NOT PORTRAIT");
        }*/


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

        //store gesture when input
        //storing should begin when user beings to move device

        if(clickedFlag)
        {
            xValList.add(x1);
            yValList.add(y1);
            zValList.add(z1);
        //    System.out.println(y1);
            //System.out.println("X: " +x1 + " Y: " +y1 + " Z: " +z1 );
        }




        //stop button will read it
        /*if(y1 < -2 && z1 > 2)
        {
            action.setText("Phone Moved Forward!!");
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.forwardarrow);

            System.out.println("***********READING: " + x1 + " Y: " + y1 + " Z: " + z1);

            //try something else
            yValList = new ArrayList<>();//make the array

            //yValList.add(y1);//add it to array list
            //yValues = new float[yValList.size()];//new each time (important) - diff gesture each time

            //convert it to float array []

            if(clickedFlag)
                WriteBtn(""+y1);//write it



        }

        else if(y1 < -2 && z1 < -2)
        {
           // action.setText("Phone Moved Toward Face!!");
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.backarrow);
        }
        else if(y1 < -2 && x1 > 2)//y down, x up = left tilt
        {
           // action.setText("Phone Titled Left!");
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.leftarrow);
        }
        else if(y1 < -2 && x1 < -2)//y down, x down = right tilt
        {
            //action.setText("Phone Titled Right!");
            image.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.rightarrow);
        }
        else {
            image.setVisibility(View.GONE);
           // action.setText("Stable");
        }*/

        //if(yValList.size() > 0)//compare values
        //{
          //  displayReading();//test float array with stored value
           // action.setText("Array Size = " + yValList.size());
        //}



    }

    public void startRecording(View view){

        spinner.setVisibility(View.VISIBLE);
        action.setText("Waiting for Match..");

        xValList =  new ArrayList<>();//new array each time we start recording
        yValList =  new ArrayList<>();//new array each time we start recording
        zValList =  new ArrayList<>();//new array each time we start recording

        clickedFlag = true;//will change flag to true, will start recording
        //change to false when stop clicked

    }

    public void compareValues(View view)
    {
        clickedFlag = false;

        if(yValList.size() > 0)
        {
            // int i = 0;//counter
            // yValues = new float [] {yValList.size() - 1};
            //action.setText("Array Size = " + yValList.size()); //*****setting it to 1 ?!?!?!?!

            int i = 0;
            float [] floatXValues = new float [xValList.size()];
            for (Float f : xValList)
            {
                if (f != null)
                    floatXValues[i++] = (f);
            }
            int j = 0;
            float [] floatYValues = new float [yValList.size()];
            for (Float f : yValList)
            {
                if (f != null)
                    floatYValues[j++] = (f);
            }
            int k = 0;
            float [] floatZValues = new float [zValList.size()];
            for (Float f : zValList)
            {
                if (f != null)
                    floatZValues[k++] = (f);
            }
            //get warping distance values for each axis
            double xWD = displayReading(floatXValues, storedX);
            double yWD =  displayReading(floatYValues, storedY);
            double zWD =  displayReading(floatZValues, storedZ);

            //if it's below 3 then it's a clear match
            if(xWD < 3 && yWD < 3 && zWD < 3)
            {
                action.setText("MATCHED FORWARD");
                spinner.setVisibility(View.INVISIBLE);
            }
            else{
                action.setText("No Match..");
                spinner.setVisibility(View.INVISIBLE);
            }




        }

    }
    public double displayReading(float [] listIn, float [] storedGesture)//change name
    {
        //record the array of values
        //System.out.print("**READING : " + new DTW(yVals, storedY));
        dtw = new DTW(listIn, storedGesture);
        distance = dtw.getDistance();
        //txtWarping.setText("Y-WD =" + yDistance);

        //String wd = String.valueOf(yDistance);

        return distance;
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

    public void openDegree(View view) {
        Intent intent = new Intent(this, Orientation.class);
        startActivity(intent);
    }

}

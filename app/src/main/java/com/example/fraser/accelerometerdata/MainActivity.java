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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Sensor mySensor;
    private SensorManager SM;

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

    ArrayList <Float> yValList;
    float[] yValues;

    float [] storedX = v.getxValues();
    float [] storedY = v.getyValues();
    float [] storedZ = v.getzValues();

    double xDistance, yDistance, zDistance;


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
        txtWarping = (TextView)findViewById(R.id.txtWarping);

        image = (ImageView) findViewById(R.id.imgDirection);
        image.setVisibility(View.GONE);

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
            yValList.add(y1);
            System.out.println(y1);
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
        yValList =  new ArrayList<>();//new array each time we start recording
        clickedFlag = true;//will change flag to true, will start recording
        //change to false when stop clicked
    }

    public void compareValues(View view){
        clickedFlag = false;

        for(Float f : yValList)
        {
            WriteBtn(""+f);//write it
        }


        System.out.println("Compare Values is running...");

        int i = 0;//counter
        yValList = ReadBtn();//read it back
        yValues = new float [] {yValList.size()};
        action.setText("Array Size = " + yValList.size()); //*****setting it to 1 ?!?!?!?!
        //convert it to float [] array
        for (Float f : yValList)
        {
            if (f != null)
                yValues[i++] = (f);
            //yValues = new float[yValList.size()];//new each time (important) - diff gesture each time
        }

        displayReading();
    }

    // write text to file
    public void WriteBtn(String f) {
        // add-write text into file
        try {
            FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(f);
            outputWriter.close();

            //display file saved message**** it displays
            //Toast.makeText(getBaseContext(), "File saved successfully!",
             //       Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read text from file
    public ArrayList<Float> ReadBtn() {
        //reading text from file
        ArrayList<Float> floatVal = new ArrayList<>();

        try {
            FileInputStream fileIn=openFileInput("mytextfile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100000];
            String s="";
            int charRead;


            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
                //************add each char to the arraylist **********
                floatVal.add(Float.parseFloat(s));
            }

            InputRead.close();
            //.makeText(getBaseContext(), s ,Toast.LENGTH_SHORT).show();

            //action.setText("Value =  " + s);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return floatVal;
    }

    public void displayReading()//change name
    {
        //record the array of values
        //System.out.print("**READING : " + new DTW(yVals, storedY));
        dtw = new DTW(yValues, storedY);
        yDistance = dtw.getDistance();
        txtWarping.setText("Y-WD =" + yDistance);

        String wd = String.valueOf(yDistance);
       // Context context = getApplicationContext();
       // int duration = Toast.LENGTH_SHORT;
        //Toast toast = Toast.makeText(context, wd, duration);
       // toast.show();

                //+ "/n Z WD = " + new DTW(zVals, storedZ));
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

    public void clearArray()
    {
        for(float f : yVals)
        {
            f = 0f;
        }


    }

    public void start2(float [] y )
    {
        float [] storedY = v.getyValues();

        System.out.println("***********Y Reading Similarity = ******************** =   ");
        System.out.print(new DTW(y, storedY));


    }

    public void stop()
    {

    }




}

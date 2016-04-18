package com.example.fraser.accelerometerdata;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GestureRecognition extends AppCompatActivity implements SensorEventListener{

    private Sensor mySensor;
    private SensorManager SM;
    private ProgressBar spinner;

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
    List<Float> yValList;//y
    List<Float> zValList;//z


    float [] storedX = v.getxValues();
    float [] storedY = v.getyValues();
    float [] storedZ = v.getzValues();

    float x1;
    float y1;
    float z1;

    double distance;

    Boolean clickedFlag = false;

    private TextView x,y,z, action, txtTimer;

    CountDownTimer myCountDownTimer;
    ListView listView;
    ArrayAdapter adapter;
    String[] gestures = {"Balance \t\t\t\t\t Forward tilt", "Transactions \t\t\t\t\t Right tilt" , "Customer Support \t\t\t\t\t Left tilt"};
    public final static String EXTRA_MESSAGE = "com.example.fraser.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_recognition);

        //create sensor manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        //accelerometer
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register sensor to listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        action = (TextView)findViewById(R.id.txtAction);
        txtTimer = (TextView)findViewById(R.id.txtTimer);

        //image = (ImageView) findViewById(R.id.imgDirection);
        //image.setVisibility(View.GONE);

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        action.setText("Gesture detection");

        //warning timer - alerts user when gesture recording will begin
        //5 seconds
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, gestures);

        myCountDownTimer = new CountDownTimer(10000, 100) {
            @Override
            //on tick let values be recorded
            //tell
            public void onTick(long millisUntilFinished) {
                //System.out.println("seconds remaining: " + millisUntilFinished / 1000);
                txtTimer.setText("Speech failed \nRecording Gesture in " + millisUntilFinished / 1000);
                listView = (ListView) findViewById(R.id.listViewGestures);
                listView.setAdapter(adapter);
            }

            @Override
            //when finished stop recording and hide spinner
            public void onFinish() {
                txtTimer.setText("");
                listView.setVisibility(View.INVISIBLE);
                startRecording();
            }
        };
        myCountDownTimer.start();
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

        x1 =  linear_acceleration[0];
        y1 =  linear_acceleration[1];
        z1 =  linear_acceleration[2];

        //store gesture when input
        //storing should begin when user beings to move device
        if(clickedFlag)
        {
            v.displayGestures();

            xValList.add(x1);
            yValList.add(y1);
            zValList.add(z1);
            //System.out.println("X: " +x1 + " Y: " +y1 + " Z: " +z1 );
        }
    }

    public void startRecording(){

        xValList =  new ArrayList<>();//new array each time we start recording
        yValList =  new ArrayList<>();//new array each time we start recording
        zValList =  new ArrayList<>();//new array each time we start recording
        action.setText("Waiting for Match..");

        myCountDownTimer = new CountDownTimer(3000, 100) {
            @Override
            //on tick let values be recorded
            public void onTick(long millisUntilFinished) {
                clickedFlag = true;
                spinner.setVisibility(View.VISIBLE);
                System.out.println("seconds remaining: " + millisUntilFinished / 1000);
            }

            @Override
            //when finished stop recording and hide spinner
            public void onFinish() {
                clickedFlag = false;//will change flag to false, will stop recording
                spinner.setVisibility(View.INVISIBLE);
                compareValues();
            }
        };
        myCountDownTimer.start();
    }
    public void test()
    {

    }

    public void compareValues()
    {
        clickedFlag = false;
        //get all stored gestures
        ArrayList<Values> allGestures  = v.getStored();

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

            //compare input gesture with all stored gestures
            //get warping distance values for each axis
            double xWD;
            double yWD;
            double zWD;

            Context context = getApplicationContext();
            CharSequence text = "";
            int duration = Toast.LENGTH_SHORT;

            for(Values v : allGestures)
            {
                float [] testX = v.getxVals();
                float [] testY = v.getyVals();
                float [] testZ = v.getzVals();

                xWD = displayReading(testX, floatXValues);
                zWD = displayReading(testY, floatYValues);
                yWD = displayReading(testZ, floatZValues);

                //get avg
                double avg = (xWD + yWD + zWD) / 3;
                String gestureName = "";
                System.out.println("WD = " + xWD +" " +yWD +" " +yWD);
                //xWD < 1.0f && yWD < 1.0f && zWD < 1.0f
                //show Toast telling user that 'gesture' has been stored
                text = "Warping Distance = " + avg;

                if (avg <= 1)
                {
                    action.setText("MATCHED - " +v.getName()+" gesture!");
                    gestureName = v.getName();//get the gesture name
                    //flag = false;
                    //spinner.setVisibility(View.INVISIBLE);
                }
                else
                   System.out.println("No Match..");

                //send user to their query
                if(gestureName.equalsIgnoreCase("forward"))
                {
                    goNext(0);
                }
                else if(gestureName.equalsIgnoreCase("right"))
                {
                    goNext(1);
                }
                else if(gestureName.equalsIgnoreCase("left"))
                {
                    goNext(2);
                }

            }
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            /*//get warping distance values for each axis
            double xWD = displayReading(floatXValues, storedX);
            double yWD =  displayReading(floatYValues, storedY);
            double zWD =  displayReading(floatZValues, storedZ);

            //if it's below 3 then it's a clear match
            if (xWD < 3 && yWD < 3 && zWD < 3)
            {
                action.setText("MATCHED FORWARD");
                //spinner.setVisibility(View.INVISIBLE);
            }
            else{
                action.setText("No Match..");
               // spinner.setVisibility(View.INVISIBLE);
            }*/

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
    public void repeatRecord(View view) {
        startRecording();
    }

    public void openVoice(View view) {
        Intent intent = new Intent(this, SpeechTester.class);
        startActivity(intent);
    }
    public void openRecord(View view) {
        Intent intent = new Intent(this, RecordGesture.class);
        startActivity(intent);
    }
    public void goNext(int index) {
        Intent intent = new Intent(this, SpeechRecognised.class);
        intent.putExtra("index", index);
        startActivity(intent);

    }

}

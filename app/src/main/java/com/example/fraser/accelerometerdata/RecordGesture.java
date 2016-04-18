package com.example.fraser.accelerometerdata;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecordGesture extends AppCompatActivity  implements SensorEventListener {

    private Sensor mySensor;
    private SensorManager SM;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];

    List<Float> xValList;//x
    List<Float> yValList;//y
    List<Float> zValList;//z

    float [] floatXValues;
    float [] floatYValues;
    float [] floatZValues;

    Values v = new Values();

    CountDownTimer myCountDownTimer;
    private EditText gestName;

    Boolean clickedFlag = false;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_gesture);
        //create sensor manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        //acceleremoter
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register sensor to listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        gestName = (EditText)findViewById(R.id.gestureName);

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

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
        }
    }

    public void startRecording(View view){

        //View view = this.getCurrentFocus();
        hideKeyboard(view);

        xValList =  new ArrayList<>();//new array each time we start recording
        yValList =  new ArrayList<>();//new array each time we start recording
        zValList =  new ArrayList<>();//new array each time we start recording

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
               // convertArray();//convert them to float []
                storeGesture();//store the gesture with the name
                //show Toast telling user that 'gesture' has been stored
                Context context = getApplicationContext();
                String customGesture = gestName.getText().toString();
                CharSequence text = "Gesture '" +customGesture +"' Recorded!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                //set back to blank
                gestName.setText("");
            }
        };
        myCountDownTimer.start();
    }

    private void storeGesture( )
    {
        int i = 0;
        floatXValues = new float [xValList.size()];
        for (Float f : xValList)
        {
            if (f != null)
                floatXValues[i++] = (f);
        }
        int j = 0;
        floatYValues = new float [yValList.size()];
        for (Float f : yValList)
        {
            if (f != null)
                floatYValues[j++] = (f);
        }
        int k = 0;
        floatZValues = new float [zValList.size()];
        for (Float f : zValList)
        {
            if (f != null)
                floatZValues[k++] = (f);
        }


        //v = new Values( gestName.getText().toString(), x , y, y);
        v.addGesture(new Values(gestName.getText().toString(), floatXValues, floatYValues, floatZValues));

        v.displayGestures();

    }
    /*private void convertArray()
    {
        int i = 0;
        floatXValues = new float [xValList.size()];
        for (Float f : xValList)
        {
            if (f != null)
                floatXValues[i++] = (f);
        }
        int j = 0;
        floatYValues = new float [yValList.size()];
        for (Float f : yValList)
        {
            if (f != null)
                floatYValues[j++] = (f);
        }
        int k = 0;
        floatZValues = new float [zValList.size()];
        for (Float f : zValList)
        {
            if (f != null)
                floatZValues[k++] = (f);
        }

        storeGesture();//store the gesture with the name

        v.displayGestures();
    }
*/



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
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

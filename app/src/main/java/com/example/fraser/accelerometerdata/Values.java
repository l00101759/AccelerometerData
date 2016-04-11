package com.example.fraser.accelerometerdata;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Fraser on 08/03/2016.
 */
public class Values {

    private float x, y, z;
    private float [] xVals, yVals, zVals;
    private static ArrayList<Values> storedValues;
    private String gesture;

    //forward gesture
    float[] xValuesForward = {-0.010496855f, -0.023720384f , -0.73149073f, -0.20212032f, 0.5923512f ,0.044839993f  };
    float[] yValuesForward  = {0.30573654f, 0.41314125f , -1.6997705f, -6.8530726f ,-0.44122744f , 1.485765f, 1.7632208f };
    float[] zValuesForward  = {-0.062433958f, -0.4176966f , 2.063875f, 7.512106f , 3.3664865f , -2.7387757f , -1.8156099f};

    //right gesture
    float[] xValuesRight = {-0.034043506f, -0.07320348f, 0.0027287751f, -0.043785647f, -2.9540393f, -7.871811f, -5.8837304f, -1.5887761f, 3.6782727f, 3.233753f, 2.426112f, 1.8412907f, 1.5036784f, 1.1876198f, 0.9654188f};
    float[] yValuesRight = {0.2267561f, 0.16608238f, 0.1098814f, 0.44799232f, -2.9436884f, -5.0058107f, -4.349414f, 0.35119152f, 0.69467115f, 2.3408542f, 1.8420377f, 1.47363f, 1.1712427f, 0.87570286f, 0.74653053f};
    float[] zValuesRight = {-0.37709594f, -0.22506237f, -0.27964878f, 0.013785839f, 0.011028767f, 0.20802069f, 0.082140446f, -0.36332846f, -1.5471399f, -0.9082697f, -0.558064f, -0.28556073f, -0.3970003f, -0.21800148f, -0.15907836f};

    //left gesture
    float[] xValuesLeft = {-0.043211848f, -0.11118393f, 0.35541672f, 4.651357f, 6.5021906f, 3.3400214f, -0.109087706f, -3.5885508f, -2.2962325f, -1.821663f, -1.4190233f, -1.1428801f, -0.8683354f, -0.72531414f, -0.5725899f};
    float[] yValuesLeft = {0.17726326f, 0.24907112f, 0.7738657f, -3.816885f, -3.865621f, -2.3799825f, 1.8884296f, 2.368826f, 1.1825466f, 0.9307146f, 0.7828789f, 0.58033466f, 0.48725224f, 0.38214016f, 0.25208187f};
    float[] zValuesLeft =  {0.09119296f, 0.026985884f, 2.320022f, -1.1702533f, -0.61442196f, -0.039512396f, -1.1118737f, -0.78223884f, -0.61046815f, -0.4653902f, -0.63280123f, -0.28405905f, -0.20426285f, -0.15574884f, -0.10161483f};

    //towards face
    float[] xValuesBack = {-0.05479654f, 0.06342301f, -0.22507364f, -0.057475783f, -0.19920954f, -0.04444595f, -0.54887366f, 0.12784807f, 0.1252628f, 0.0925488f, 0.18129927f, 0.007133402f, 0.105305515f, 0.045937184f, 0.059734084f};
    float[] yValuesBack = {-0.009454727f, 9.727478E-5f, 0.26822758f, 0.6895914f, -1.8770051f, -1.4632974f, -1.101685f, -0.98094606f, 1.9810247f, 0.40495777f, 0.515502f, 0.35877132f, 0.31000137f, 0.2250166f, 0.15702915f};
    float[] zValuesBack ={-0.389019f, -0.20395505f, -0.2627629f, 0.30310655f, -1.9563497f, -9.617259f, -5.732477f, 1.8649553f, 5.169458f, 1.9137473f, 1.8451171f, 1.4071407f, 1.0490981f, 0.777987f, 0.33891606f};


    public Values()
    {
        storedValues  = new ArrayList<Values>(1000);

        //creat a list with pre recorded gestures
        storedValues.add(new Values("forward", xValuesForward, yValuesForward, zValuesForward));
        storedValues.add(new Values("right", xValuesRight, yValuesRight, zValuesRight));
        storedValues.add(new Values("left", xValuesLeft, yValuesLeft, zValuesLeft));
        storedValues.add(new Values("back", xValuesBack, yValuesBack, zValuesBack));
    }
    //creates a gesture with a name eg "forward"
    public Values(String gestureName, float [] x, float [] y, float [] z )
    {
        gesture = gestureName;
        xVals = x;
        yVals= y;
        zVals = z;
        //storedValues.add((new Values(gestureName, x, y, z)));
    }

    public Values(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    protected void addGesture(Values v)
    {
        storedValues.add(v);
    }
    protected void displayGestures()
    {
        for(Values v : storedValues)
        {
            System.out.println("**Name = " + v.getName() + "*** --> X " + Arrays.toString(v.getxVals()) + "\n*** --> Y" + Arrays.toString(v.getyVals()) + "\n*** --> Z " + Arrays.toString(v.getzVals())  );
        }
    }
    public ArrayList<Values> getStored() {
        return storedValues;
    }

    public float[] getxValues() {
        return xValuesForward ;
    }

    public float[] getyValues() {
        return yValuesForward ;
    }
    public float[] getzValues() {
        return zValuesForward ;
    }

    public String getName() {
        return gesture;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float[] getxVals() {
        return xVals;
    }

    public float[] getyVals() {
        return yVals;
    }

    public float[] getzVals() {
        return zVals;
    }

}


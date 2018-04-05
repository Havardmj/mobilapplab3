package com.example.havardmj.lab3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //sensor obj from sensorEventListner class
    SensorManager thisSensor;
    Sensor sensor;

    ImageView objectb;  //ball obj
    int gamewidth = 0;   //width
    int gameheight = 0;   //height
    int rad = 0;        //radius

    ToneGenerator tg;   //for sound effect
    DisplayMetrics dm; //screen height & width
    int height = 0;
    int width = 0;

    @Override
    protected  void onResume(){ //add listener on g-sensor when activated again
        super.onResume();
        thisSensor.registerListener(this,sensor,thisSensor.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause(){   //remove listener on sensor when not in use
        super.onPause();
        thisSensor.unregisterListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get elements from Imageview
        ImageView currentFrame = findViewById(R.id.currentFrame);
        ImageView objectb = findViewById(R.id.obejctb);
        //init sound
        tg = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.TONE_CDMA_ANSWER);

        gameheight=currentFrame.getHeight();    //get height and width from screen
        gamewidth=currentFrame.getWidth();
        rad = 25;

        //get display screen data
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
        width = dm.widthPixels;

        //get sensor
        thisSensor = (SensorManager)getSystemService(SENSOR_SERVICE);
        //don't want sensor to get a nullpointer exception
        assert thisSensor != null;
        sensor = thisSensor.getDefaultSensor(Sensor.TYPE_GRAVITY);
        thisSensor.registerListener(this, sensor, thisSensor.SENSOR_DELAY_GAME);

    }

    @Override   //not used
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x=objectb.getX(); //get current position
        float y=objectb.getY();
        float xx = x+event.values[1];   //get next position
        float yy = y+event.values[0];
        Vibrator vb = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        assert  vb != null;

        if((xx-rad)>=(gamewidth/2)){    //move obj left
            objectb.setX(xx);
        }else{                  //on boarder hit, set: vibrate and sound
            vb.vibrate(200);
            tg.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT);
            objectb.setX(xx+35);    //bounce back obj
        }
        if((yy-rad)>=(gameheight/2)){   //move obj up
            objectb.setY(yy);
        }else{
            vb.vibrate(200);    //on boarder hit, set: vibrate and sound
            tg.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT);
            objectb.setY(yy+35);
        }
        if((xx+rad)>width-(gamewidth/2)-60){    //move obj right
            vb.vibrate(200);
            tg.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT);
            objectb.setX((xx-35));
        }
        if((yy+rad)>height-(gameheight/2)-320){ //move obj down
            vb.vibrate(200);
            tg.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT);
            objectb.setY(yy-35);
        }
    }
}

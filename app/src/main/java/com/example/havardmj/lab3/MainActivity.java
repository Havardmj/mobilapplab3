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
    private SensorManager thisSensor;
    private Sensor sensor;

    private ImageView objectb;  //ball obj
    private int gamewidth = 0;   //width-of-frame
    private int gameheight = 0;   //height-of-frame
    private int rad = 0;        //radius

    private ToneGenerator tg;   //for sound effect

    private int height = 0;
    private int width = 0;

    @Override
    protected  void onResume(){ //add listener on g-sensor when activated again
        super.onResume();
        thisSensor.registerListener(this, sensor , SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause(){   //remove listener on sensor when not in use
        super.onPause();
        thisSensor.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float positionY;
        float positionX;

        positionX = objectb.getX(); //get current position
        positionY = objectb.getY();

        float newPositionX = positionX + event.values[1];
        float newPositionY = positionY + event.values[0];

        Vibrator vb = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        assert  vb != null;

        if( (newPositionX - rad ) >=  gamewidth / 2  ){    //move obj left

            objectb.setX(newPositionX);
        }else{                  //on boarder hit, set: vibrate and sound

            vb.vibrate(200);
            tg.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT);
            objectb.setX( newPositionX + 35 );    //bounce back obj
        }


        if( (newPositionY - rad) >= (gameheight / 2) + 20 ){   //move obj up

            objectb.setY(newPositionY);
        }else{

            vb.vibrate(200);    //on boarder hit, set: vibrate and sound
            tg.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT);
            objectb.setY(newPositionY + 35);
        }


        if( (newPositionX + rad) > width - (gamewidth / 2) - 60){    //move obj right

            vb.vibrate(200);
            tg.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT);
            objectb.setX((newPositionX - 35 ));
        }


        if( (newPositionY + rad) > height - (gameheight / 2) - 320){ //move obj down

            vb.vibrate(200);
            tg.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT);
            objectb.setY(newPositionY - 35);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get elements from Imageview
        ImageView currentFrame = findViewById(R.id.currentFrame);
        objectb = findViewById(R.id.obejctb);
        //init sound
        tg = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.TONE_CDMA_ANSWER);

        gamewidth=currentFrame.getWidth();
        gameheight=currentFrame.getHeight();    //get height and width from screen
        rad = 25;

        //get display screen data
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
        width = dm.widthPixels;

        //get sensor
        thisSensor = (SensorManager)getSystemService(SENSOR_SERVICE);
        //don't want sensor to get a nullpointer exception
        assert thisSensor != null;
        sensor = thisSensor.getDefaultSensor(Sensor.TYPE_GRAVITY);
        thisSensor.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

    }


    @Override   //not used - but class implementation request Override method.
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


}

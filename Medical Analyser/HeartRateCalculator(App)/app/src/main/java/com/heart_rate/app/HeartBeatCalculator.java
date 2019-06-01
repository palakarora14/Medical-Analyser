package com.heart_rate.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.heart_rate.app.User.SymptomsActivity;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;


public class HeartBeatCalculator extends Activity implements TextureView.SurfaceTextureListener,View.OnClickListener{

    private static Context context = null;
    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    //TEXTURE VIEW
    private static TextureView previewSurface = null;

    private static Camera camera = null;
    private static View image = null;
    private static TextView text = null;
    private static TextView timer = null;
    private TextView beatsCounter = null;

    private static PowerManager.WakeLock wakeLock = null;

    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];


    public static enum TYPE {
        BLACK, COLORED
    };

    private static TYPE currentType = TYPE.BLACK;
    public static TYPE getCurrent() { return currentType; }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;
    private static long timerStart = 0;
    private static long timerEnd = 0;
    private static long elapsedTime = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heart_beat_calculator);

        //Instructions Dialog Box
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("1. Put Your Index Finger covering the camera and the flash light.\n2. Don\'t press too tightly on camera , this may bring in error. \n3. Keep your finger for 30 seconds.")
                        .setCancelable(false)
                        .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

AlertDialog alertDialog=builder.create();
alertDialog.setTitle("INSTRUCTIONS !!!");
alertDialog.show();

        //FOR USING TEXTURE VIEW
        previewSurface = (TextureView) findViewById(R.id.previewSurface);
        previewSurface.setSurfaceTextureListener(this);

        image = findViewById(R.id.image);
        text = (TextView) findViewById(R.id.beats_count);
        timer = (TextView) findViewById(R.id.timer);
        beatsCounter = (TextView) findViewById(R.id.beats_count);
        context = this;

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AppName:DoNotDimScreen");

        //ONCLICKLISTENER SETUP
        image.setOnClickListener(this);
    }



    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        previewSurface.setVisibility(View.VISIBLE);
    }
    @Override
    public void onResume() {
        super.onResume();
        text.setText("");
        createToast("Tap To Start");

        //ACQUIRING WAKE LOCK AND OPENING THE CAMERA
        wakeLock.acquire();
        camera = Camera.open();
    }
    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();

        try {
            stopHeartRateCalculator();
            camera.release();
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.image){
            if(timerStart == 0 && timerEnd == 0) {
                beatsCounter.setText("");
                timerStart = System.currentTimeMillis() / 1000;
                timerEnd = timerStart;

                //SETTING PREVIEW DISPLAY(WILL SHOW PREVIEW ON SURFACE VIEW)
                try {
                    camera.setPreviewTexture(previewSurface.getSurfaceTexture());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //SETTING CAMERA PARAMETERS(SETTING FLASH_MODE AS TORCH)
                if (camera != null) {
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                }

                //SETTING CAMERA PREVIEW CALLBACK AND STARTING PREVIEW
                camera.setPreviewCallback(previewCallback);
                camera.startPreview();
                startTime = System.currentTimeMillis();
            }
        }
    }


    private static Camera.PreviewCallback previewCallback;
    {
        previewCallback = new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera cam) {
                if (data == null) throw new NullPointerException();
                Camera.Size size = cam.getParameters().getPreviewSize();
                if (size == null) throw new NullPointerException();

                //HANDLING TIMER
                if(elapsedTime <= 0) {
                    stopHeartRateCalculator();
                    createToast(text.getText().toString());
                    Log.e("test",text.getText().toString());

                    //Pass final heart rate to user
                    /*Bundle bundle=new Bundle();
                    Intent intent = new Intent(HeartBeatCalculator.this, SymptomsActivity.class);
                    if(text.getText().toString()!=null) {
                        bundle.putString("Heart Rate", text.getText().toString());
                    }
                    else {
                        bundle.putString("Heart Rate", "You have not calculated your Heart Rate yet");
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
*/
                    return;
                }
                timerEnd = System.currentTimeMillis() / 1000;
                elapsedTime = 30 - (timerEnd - timerStart);
                timer.setText("MEASURING IN" + "\n         " + String.valueOf(elapsedTime) + "s");


                if (!processing.compareAndSet(false, true)) return;

                int width = size.width;
                int height = size.height;

                int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
                if (imgAvg == 0 || imgAvg == 255) {
                    processing.set(false);
                    return;
                }

                int averageArrayAvg = 0;
                int averageArrayCnt = 0;
                for (int i = 0; i < averageArray.length; i++) {
                    if (averageArray[i] > 0) {
                        averageArrayAvg += averageArray[i];
                        averageArrayCnt++;
                    }
                }

                int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
                TYPE newType = currentType;
                if (imgAvg < rollingAverage) {
                    newType = TYPE.COLORED;
                    if (newType != currentType) {
                        beats++;
                    }
                } else if (imgAvg > rollingAverage) {
                    newType = TYPE.BLACK;
                }

                if (averageIndex == averageArraySize) averageIndex = 0;
                averageArray[averageIndex] = imgAvg;
                averageIndex++;

                // Transitioned from one state to another to the same
                if (newType != currentType) {
                    currentType = newType;
                    image.postInvalidate();
                }

                long endTime = System.currentTimeMillis();
                double totalTimeInSecs = (endTime - startTime) / 1000d;
                if (totalTimeInSecs >= 10) {
                    double bps = (beats / totalTimeInSecs);
                    int dpm = (int) (bps * 60d);
                    if (dpm < 30 || dpm > 180) {
                        startTime = System.currentTimeMillis();
                        beats = 0;
                        processing.set(false);
                        return;
                    }

                    if (beatsIndex == beatsArraySize) beatsIndex = 0;
                    beatsArray[beatsIndex] = dpm;
                    beatsIndex++;

                    int beatsArrayAvg = 0;
                    int beatsArrayCnt = 0;
                    for (int i = 0; i < beatsArray.length; i++) {
                        if (beatsArray[i] > 0) {
                            beatsArrayAvg += beatsArray[i];
                            beatsArrayCnt++;
                        }
                    }
                    int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                    text.setText(String.valueOf(beatsAvg));
                    startTime = System.currentTimeMillis();
                    beats = 0;
                }
                processing.set(false);
            }
        };
    }



    //SURFACE TEXTURE VIEW LISTENER METHODS
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = getSmallestPreviewSize(width, height, parameters);


        if (size != null) {
            parameters.setPreviewSize(size.width, size.height);
            Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
        }
        camera.setParameters(parameters);
        previewSurface.setVisibility(View.INVISIBLE); // Make the surface invisible as soon as it is created
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Put code here to handle texture size change if you want to
    }
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Update your view here!
    }



    //STATIC METHODS
    public static void createToast(CharSequence message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    private static void insertIntoDatabase(String heartbeat){
        Intent intent = new Intent(context, History.class);
        intent.putExtra("heartbeat", heartbeat);
        context.startActivity(intent);
    }
    private static void stopHeartRateCalculator(){
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);

            camera.setPreviewCallback(null);
            camera.stopPreview();
            timerEnd = 0;
            timerStart = 0;
            elapsedTime = 30;
            beats = 0;
            timer.setText("");
            for(int value : beatsArray) value = 0;
            for(int value : averageArray) value = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }
}

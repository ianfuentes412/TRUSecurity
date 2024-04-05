package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.security.Policy;

public class MainActivity extends AppCompatActivity {

    private ToggleButton toggleFlashLightOnOff;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private CameraManager cameraManager;
    private String getCameraID;

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleFlashLightOnOff = findViewById(R.id.toggle_btn);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            // O means back camera unit,
            // 1 means front camera unit
            getCameraID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void OnToggleClicked(View view) {

        if (toggleFlashLightOnOff.isChecked()) {
            Intent intent = new Intent(this, AlarmReceiver.class);

            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 10000, pendingIntent);

            Toast.makeText(MainActivity.this, "SOS ON", Toast.LENGTH_SHORT).show();
        }
        else{
            alarmManager.cancel(pendingIntent);
            Toast.makeText(MainActivity.this, "SOS OFF", Toast.LENGTH_SHORT).show();
        }

    }
    
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void toggleFlashLight(View view){
        String myString = "010101010101";
        long blinkDelay =50; //Delay in ms

        if (toggleFlashLightOnOff.isChecked()) {
            // Exception is handled, because to check
            // whether the camera resource is being used by
            // another service or not.
            try {

                for(int i = 0; i < myString.length(); i++){
                    if (myString.charAt(i) == '0'){
                        // true sets the torch in ON mode
                        cameraManager.setTorchMode(getCameraID, true);
                    }
                    else{
                        cameraManager.setTorchMode(getCameraID, false);
                    }
                }
                // Inform the user about the flashlight
                // status using Toast message
                Toast.makeText(MainActivity.this, "Flashlight is turned ON", Toast.LENGTH_SHORT).show();
            } catch (CameraAccessException e) {
                // prints stack trace on standard error
                // output error stream
                e.printStackTrace();
            }
        } else {
            // Exception is handled, because to check
            // whether the camera resource is being used by
            // another service or not.
            try {
                // true sets the torch in OFF mode
                cameraManager.setTorchMode(getCameraID, false);

                // Inform the user about the flashlight
                // status using Toast message
                Toast.makeText(MainActivity.this, "Flashlight is turned OFF", Toast.LENGTH_SHORT).show();
            } catch (CameraAccessException e) {
                // prints stack trace on standard error
                // output error stream
                e.printStackTrace();
            }
        }
    }



}
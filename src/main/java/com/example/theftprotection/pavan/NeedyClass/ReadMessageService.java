package com.example.pavan.theftprotection.NeedyClass;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.pavan.theftprotection.Activity.HomeActivity;
import com.example.pavan.theftprotection.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;

public class ReadMessageService extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //Toast.makeText(getApplicationContext(),"Service Background Detecting Messages",Toast.LENGTH_SHORT).show();
        SmsReceiver.bindListener(new SmsListener() {
            @Override

            public void messageReceived(final String messageText) {

                Toast.makeText(getApplicationContext(),"On Background"+messageText,Toast.LENGTH_SHORT).show();

                startService(new Intent(getBaseContext(),UpdateLocationService.class));


            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        startService(new Intent(getBaseContext(),ReadMessageService.class));
        super.onDestroy();
        Log.i("MAINACT", "onDestroy!");
    }



    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}

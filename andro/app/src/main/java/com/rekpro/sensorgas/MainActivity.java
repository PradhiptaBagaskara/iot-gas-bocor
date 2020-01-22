package com.rekpro.sensorgas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView sensorgas, stts;
    Notification.Builder noti;
    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "com.rekpro.sensorgas";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int notid=001;
        noti=
                new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                        .setContentTitle("Sensor Gas")
                        .setContentText("Gas Bocor, Segera Periksa Rumah Anda")
                        .setSmallIcon(android.R.drawable.stat_notify_more)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendindIntent=
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        noti.setContentIntent(resultPendindIntent);

        final NotificationManager notima=
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createChannels();
        sensorgas = findViewById(R.id.txtGas);
        stts = findViewById(R.id.textView3);

        FirebaseDatabase Datab=FirebaseDatabase.getInstance();
        DatabaseReference ref=Datab.getReference("gasensor");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int h = dataSnapshot.getValue(Integer.class);
                int sen = h * 100;
                sensorgas.setText(Integer.toString(sen) + " PPM");
                if (h >= 50){
                    notima.notify(notid, noti.build());
                    stts.setText("Bahaya Ada Kebocoran");
                    stts.setTextColor(getResources().getColor(R.color.colorAccent));
                    sensorgas.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    stts.setText("Gass LPG Aman!");
                    sensorgas.setTextColor(getResources().getColor(R.color.colorPrimary));
                    stts.setTextColor(getResources().getColor(R.color.colorPrimary));


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", databaseError.getMessage());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels() {

        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        androidChannel.enableLights(true);
        androidChannel.enableVibration(true);
        androidChannel.setLightColor(Color.BLUE);
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);


    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
}
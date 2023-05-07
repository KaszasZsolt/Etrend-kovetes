package com.example.etrend_kovetes;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class profil_setting extends AppCompatActivity {
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

    public static String NOTIFICATION_CHANNEL_ID="1001";
    public static String default_notification_id="default";
    Button emlekezteto;
    TextInputEditText ido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_setting);
        ido=findViewById(R.id.idomezo);
        emlekezteto = findViewById(R.id.emlekezteto);
        emlekezteto.setOnClickListener(v->schudleNotification(getNotification("Ideje folyadékot, ételt fogyasztanod!"),Integer.parseInt(String.valueOf(ido.getText())) *1000));

    }

    private void schudleNotification(Notification notification, int delay){
        Intent notificationIntent=new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID,1);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION,notification);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        long futureMilis= SystemClock.elapsedRealtime()+delay;
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager !=null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,futureMilis,pendingIntent);
    }

    private Notification getNotification(String content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_id)
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentTitle("Táplálkozás követés.")
                .setContentText(content).setAutoCancel(true).setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }




    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    notificationManagerCompat.notify(1, notification);
                } else {
                    Log.d(TAG, "Engedély nem került megadásra");
                }
                return;
            }

        }
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(this, "Kijelentkezés", Toast.LENGTH_SHORT).show();

                AuthService authService = new AuthService();
                authService.logout();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_main:
                Toast.makeText(this, "main", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getApplicationContext(), Main.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.menu_profile:
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(getApplicationContext(), profil_setting.class);
                startActivity(intent3);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.latenightcode.write.writer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;


import com.latenightcode.write.writer.Config.Config;
import com.latenightcode.write.writer.model.AlertReceiver;
import com.latenightcode.write.writer.model.TimePickerFragment;

import java.util.Calendar;

import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    RichEditor richEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final String document = getIntent().getStringExtra("text");
//
//        richEditor  = findViewById(R.id.main2Editor);
//        richEditor.setHtml(document);
//
//        Spanned t = Html.fromHtml(document);
//        String tv = t.toString();
//        Toast.makeText(this, tv, Toast.LENGTH_SHORT).show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_alarmTime){

            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "timePicker");


            return true;
        }

        if(id == R.id.action_logout){

            //...TODO change cancel Alarm button from logout to others
            cancelAlarm();

            return true;
        }

        return super.onOptionsItemSelected(item);

    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        startAlarm(calendar);

        Toast.makeText(this, hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();

    }

    private void startAlarm(Calendar calendar) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Config.NOTIFICATION_REQUEST_CODE, intent, 0);

        //Repeat every 24 hours
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
    }

    public void cancelAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Config.NOTIFICATION_REQUEST_CODE, intent, 0);

        alarmManager.cancel(pendingIntent);

        Toast.makeText(this, "alarm canceled", Toast.LENGTH_SHORT).show();
    }


}

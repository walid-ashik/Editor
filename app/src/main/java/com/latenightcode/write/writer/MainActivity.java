package com.latenightcode.write.writer;

import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.latenightcode.write.writer.Config.Config;
import com.latenightcode.write.writer.model.AlertReceiver;
import com.latenightcode.write.writer.model.TimePickerFragment;

import java.util.Calendar;

import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    RichEditor richEditor;
    private Menu mMenu;
    private TextSwitcher mTotalWordsSoFarTextSwitcher;
    private TextView mTotalWrittenWordsTextView;
    private DatabaseReference totalWordsSoFarWrittenDataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        totalWordsSoFarWrittenDataRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("uid_of_user");
        mTotalWordsSoFarTextSwitcher = findViewById(R.id.text_switcher_to_animate_written_words);
        mTotalWrittenWordsTextView = findViewById(R.id.total_written_words_textView);
        findViewById(R.id.floating_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TextEditorActivity.class));
            }
        });
        showTotalWordsSoFar(mTotalWrittenWordsTextView);
    }

    private void showTotalWordsSoFar(final TextView mTotalWrittenWordsTextView) {
        totalWordsSoFarWrittenDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Config.TOTAL_WORDS_SO_FAR_CHILD_NAME)){
                    int total_words_so_far = Integer.parseInt(dataSnapshot.child(Config.TOTAL_WORDS_SO_FAR_CHILD_NAME).getValue().toString());
                    totalWordsSoFarWrittenDataRef.keepSynced(true   );
                    animateTextView(0, total_words_so_far, mTotalWrittenWordsTextView);
                }else {
                    mTotalWrittenWordsTextView.setText("00");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void animateTextView(int zero, final int total_words_so_far, final TextView mTotalWrittenWordsTextView) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(zero, total_words_so_far);
        valueAnimator.setDuration(1500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                if(total_words_so_far < 9)
                    mTotalWrittenWordsTextView.setText("000" + valueAnimator.getAnimatedValue().toString());
                else if(total_words_so_far < 99)
                    mTotalWrittenWordsTextView.setText("00" + valueAnimator.getAnimatedValue().toString());
                else if(total_words_so_far < 999)
                    mTotalWrittenWordsTextView.setText("0" + valueAnimator.getAnimatedValue().toString());
                else
                    mTotalWrittenWordsTextView.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void changeAlarmIconIfTimeSaved(Menu mMenu) {
        SharedPreferences prefs = getSharedPreferences(Config.ALARM_TIME_SAVED, MODE_PRIVATE);
        boolean isAlarmSaveld = prefs.getBoolean(Config.ALARM_TIME_SAVED, true);
        if (isAlarmSaveld == true) {
            mMenu.getItem(0).setIcon(R.drawable.ic_alarm_time_selected);
        } else {
            mMenu.getItem(0).setIcon(R.drawable.ic_alarm);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_alarmTime) {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "timePicker");
            return true;
        }
        if (id == R.id.action_logout) {
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

        //...whether user saved alarm track that -> change every time actionBar icon if saved alarm time
        SharedPreferences.Editor editor = getSharedPreferences(Config.ALARM_TIME_SAVED, MODE_PRIVATE).edit();
        editor.putBoolean(Config.ALARM_TIME_SAVED, true);
        editor.commit();

        Toast.makeText(this, hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();

    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Config.NOTIFICATION_REQUEST_CODE, intent, 0);
        //Repeat every 24 hours
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
    }

    public void cancelAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Config.NOTIFICATION_REQUEST_CODE, intent, 0);

        alarmManager.cancel(pendingIntent);

        Toast.makeText(this, "alarm canceled", Toast.LENGTH_SHORT).show();
    }


}

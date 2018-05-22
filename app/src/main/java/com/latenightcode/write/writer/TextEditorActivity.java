package com.latenightcode.write.writer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.latenightcode.write.writer.Config.Config;
import com.latenightcode.write.writer.model.AlertReceiver;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.richeditor.RichEditor;

public class TextEditorActivity extends AppCompatActivity {

    private RichEditor editor;
    String doc;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);


        editor = findViewById(R.id.editor);

        customizeRichEditor(editor);


//        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doc = editor.getHtml();
//
//                Intent i = new Intent(MainActivity.this, Main2Activity.class);
//                i.putExtra("text", doc);
//                startActivity(i);
//
//            }
//        });


    }

    public String getTodaysDate() {
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateFormat.format(calendar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.text_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_alarm_snooze){

            String nextAlarmTime =  new SimpleDateFormat("hh:mm").format(new Date(Calendar.getInstance().getTimeInMillis() + 5 * 60000));
            //..Todo remove toast
            Toast.makeText(this, "Next notification will be at " + nextAlarmTime, Toast.LENGTH_SHORT).show();


            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    snoozeAlarm();

                }
            }, 2500);

        }

        if(id == R.id.action_save_icon || id == R.id.action_save_hidden_menu){
            //...Todo Save writing here
        }

        return super.onOptionsItemSelected(item);
    }

    private void snoozeAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Config.NOTIFICATION_REQUEST_CODE, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().getTimeInMillis() + 5 * 60000, //...start after 5 * 60000 milis = 5 minute
                pendingIntent);

        finish();
        System.exit(0);
    }



    private void customizeRichEditor(final RichEditor mEditor) {

        final RichEditor richEditor = mEditor;

        richEditor.setEditorHeight(200);
        richEditor.setEditorFontSize(18);
        richEditor.setPadding(10, 10, 10, 10);
        richEditor.setPlaceholder("Make this day a history...");
        richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {

            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBold();
            }
        });
        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setItalic();
            }
        });
        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBlockquote();
            }
        });
        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setUnderline();
            }
        });
        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignLeft();
            }
        });
        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setNumbers();
            }
        });
        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richEditor.setHeading(4);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


}

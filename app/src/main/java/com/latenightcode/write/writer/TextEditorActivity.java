package com.latenightcode.write.writer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.latenightcode.write.writer.Config.Config;
import com.latenightcode.write.writer.model.AlertReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.richeditor.RichEditor;

public class TextEditorActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RichEditor editor;
    private ProgressDialog progressDialog;

    private int totalWordsSoFarUserWritten;
    private String mWrittenTextWithHtml;
    private String currentDate, currentTime;
    private String timeStamp;
    private DatabaseReference mDatabase;
    private DatabaseReference totalWordsSoFarWrittenDataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editor = findViewById(R.id.editor);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("      loading...");
        customizeRichEditor(editor);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        totalWordsSoFarWrittenDataRef = mDatabase.child("Users").child("uid_of_user");
        getTotalWordsSoFar(totalWordsSoFarWrittenDataRef);
        timeStamp = String.valueOf(ServerValue.TIMESTAMP);
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate = dateFormat.format(c);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        currentTime = timeFormat.format(c);
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

    private void getTotalWordsSoFar(DatabaseReference totalWordsSoFarWrittenDataRef) {
        totalWordsSoFarWrittenDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Config.TOTAL_WORDS_SO_FAR_CHILD_NAME)){
                    totalWordsSoFarUserWritten = Integer.parseInt(dataSnapshot.child(Config.TOTAL_WORDS_SO_FAR_CHILD_NAME).getValue().toString());
                }else{
                    totalWordsSoFarUserWritten = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
            }, 3000);
        }

        if(id == R.id.action_save_icon || id == R.id.action_save_hidden_menu){
            
            if(!isTextEditorEmpty()){
                saveTodaysStoryToDatabase();    
            }else{
                Toast.makeText(this, "nothing will be saved!", Toast.LENGTH_SHORT).show();
            }
            
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isTextEditorEmpty() {
        if(editor.getHtml() == null)
            return true;
        else
            return false;
    }

    private void snoozeAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                                      Config.NOTIFICATION_REQUEST_CODE, intent, 0);
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
        if(!isTextEditorEmpty()){
           saveTodaysStoryToDatabase();
        }else{
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void saveTodaysStoryToDatabase() {
        progressDialog.show();
        mWrittenTextWithHtml = editor.getHtml();
        Spanned spannedTextFromHTML = Html.fromHtml(mWrittenTextWithHtml);
        String writtentTextWithoutHtmlTag = spannedTextFromHTML.toString();

        final int totalWordsWritten = Config.wordCount(writtentTextWithoutHtmlTag);
        int readTimes = Config.wordPerMinuteReadTime(totalWordsWritten);

        //..TODO check if text is minimum 100 words or not for snoozing the alarm
        Map map = new HashMap();
        map.put("every_day_writing", writtentTextWithoutHtmlTag);
        map.put("every_day_writing_with_html", mWrittenTextWithHtml);
        map.put("date", currentDate);
        map.put("time", currentTime);
        map.put("words", totalWordsWritten);
        map.put("reading_minutes", readTimes);

        String push_key = mDatabase.push().getKey();

        mDatabase.child("Users").child("uid_of_user").child("story_of_days").child(push_key)
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d(TAG, "onComplete: " + "some error occured!");
                    }else{
                        addTotalWordsSoFar(totalWordsWritten);
                    }
                }
        });
    }

    private void addTotalWordsSoFar(int totalWordsWritten) {

        totalWordsSoFarUserWritten += totalWordsWritten;

        totalWordsSoFarWrittenDataRef.child(Config.TOTAL_WORDS_SO_FAR_CHILD_NAME).setValue(totalWordsSoFarUserWritten)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            progressDialog.cancel();
                            Toast.makeText(TextEditorActivity.this, "error..! Please Write Again!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onTotalWordsUpdateComplete: " + totalWordsSoFarUserWritten);
                        }else{
                            if(progressDialog != null && progressDialog.isShowing()){
                                progressDialog.cancel();
                            }
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }
                });
    }

}

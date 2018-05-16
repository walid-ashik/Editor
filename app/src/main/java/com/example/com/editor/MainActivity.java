package com.example.com.editor;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity {

    private RichEditor editor;
    private TextView mPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = findViewById(R.id.editor);
        editor.setEditorHeight(200);
        editor.setEditorFontSize(22);
        editor.setPadding(10, 10, 10, 10);
        editor.setPlaceholder("Insert text here...");
        editor. setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
               // mPreview.setText(text);
            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.setBold();
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editor.insertTodo();
            }
        });

    }
}

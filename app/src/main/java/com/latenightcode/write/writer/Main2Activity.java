package com.latenightcode.write.writer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;
import jp.wasabeef.richeditor.RichEditor;

public class Main2Activity extends AppCompatActivity {

    RichEditor richEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final String document = getIntent().getStringExtra("text");

        richEditor  = findViewById(R.id.main2Editor);
        richEditor.setHtml(document);

        Spanned t = Html.fromHtml(document);
        String tv = t.toString();
        Toast.makeText(this, tv, Toast.LENGTH_SHORT).show();

    }
}

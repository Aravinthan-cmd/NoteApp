package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotedetailsActivity extends AppCompatActivity {

    private TextView mtitledetail,mcontentdetail;
    FloatingActionButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notedetails);

        mtitledetail = findViewById(R.id.detailstitle);
        mcontentdetail = findViewById(R.id.detailscontent);
        backbtn = findViewById(R.id.detailsbackbutton);

        Intent data = getIntent();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NotedetailsActivity.this,NoteMenu.class);
                startActivity(i);
            }
        });

        mtitledetail.setText(data.getStringExtra("title"));
        mcontentdetail.setText(data.getStringExtra("content"));

    }
}
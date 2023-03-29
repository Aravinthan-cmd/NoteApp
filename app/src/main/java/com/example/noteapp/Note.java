package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Note extends AppCompatActivity {

    EditText mcreate_title,mcreate_content;
    FloatingActionButton msave;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FloatingActionButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mcreate_title = findViewById(R.id.createtitle);
        mcreate_content = findViewById(R.id.createcontent);
        msave = findViewById(R.id.createsavebutton);

        //firebase instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        backBtn = findViewById(R.id.createbackbutton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Note.this,NoteMenu.class);
                startActivity(i);
            }
        });

        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mcreate_title.getText().toString().trim();
                String content = mcreate_content.getText().toString();
                if (title.isEmpty() || content.isEmpty()){
                    Toast.makeText(Note.this,"Both Fields are required!",Toast.LENGTH_LONG).show();
                }else {
                    //Firestore path create
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Note.this,"Notes Add Successfully",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Note.this,NoteMenu.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Note.this,"Failed to Create Note!",Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

    }
}
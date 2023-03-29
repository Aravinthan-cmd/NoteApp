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

public class EditnoteActivity extends AppCompatActivity {

    EditText medittile,meditcontent;
    FloatingActionButton editsavebtn;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editnote);

        medittile = findViewById(R.id.edittitle);
        meditcontent = findViewById(R.id.editcontent);
        editsavebtn = findViewById(R.id.editsavebutton);

        Intent data = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String notetitle = data.getStringExtra("title");
        String notecontent = data.getStringExtra("content");
        medittile.setText(notetitle);
        meditcontent.setText(notecontent);

        editsavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newtitle = medittile.getText().toString();
                String newcontent = meditcontent.getText().toString();
                if (newtitle.isEmpty() || newcontent.isEmpty()){
                    Toast.makeText(EditnoteActivity.this, "Something is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditnoteActivity.this, "Note is Updated", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(EditnoteActivity.this,NoteMenu.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditnoteActivity.this, "Failed to Update", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}